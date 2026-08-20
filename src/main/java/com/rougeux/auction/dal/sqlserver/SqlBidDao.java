package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.BidDao;
import com.rougeux.auction.dal.sqlserver.mapper.BidRowMapper;
import com.rougeux.auction.dal.sqlserver.mapper.projection.BidProjectionRowMapper;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.projection.BidPointProjection;
import com.rougeux.auction.domain.projection.BidProjection;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlBidDao implements BidDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as bid_id, time, amount, sale_id, user_id
            FROM BIDS b
            """;
    private static final String READ_ALL_PROJECTED = """
            SELECT b.id as bid_id, b.time, b.amount, b.sale_id, b.user_id, s.slug as sale_slug,
                   u.slug as user_slug, u.username, u.firstname, u.lastname, u.credit, u.created_at,
                   i.filename, i.width, i.height
            FROM BIDS b
            JOIN SALES s ON s.id = b.sale_id
            JOIN USERS u ON u.id = b.user_id
            JOIN IMAGES i ON i.id = u.image_id
            """;

    @Override
    public List<Bid> findAll() {
        return template.query(READ_ALL + "\nORDER BY TIME DESC", BidRowMapper::mapBid);
    }

    @Override
    public List<BidProjection> findAllProjectedBySale(int page, int limit, String slug) {
        String query = READ_ALL_PROJECTED + "\nWHERE s.slug = :slug ORDER BY b.time DESC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";

        return template.query(query, new MapSqlParameterSource()
                        .addValue("slug", slug)
                        .addValue("offset", (long) (page - 1) * limit)
                        .addValue("limit", limit),
                BidProjectionRowMapper::mapProjection);
    }

    @Override
    public List<BidProjection> findLatestProjected(int limit) {
        String query = READ_ALL_PROJECTED + "\nORDER BY b.time DESC OFFSET 0 ROWS FETCH NEXT :limit ROWS ONLY";

        return template.query(query, new MapSqlParameterSource()
                        .addValue("limit", limit),
                BidProjectionRowMapper::mapProjection);
    }

    @Override
    public List<BidPointProjection> findAllPointsBySale(String slug) {
        return template.query("""
                        SELECT b.amount, b.time, u.slug as user_slug, s.slug as sale_slug, u.firstname, u.lastname
                        FROM BIDS b
                        JOIN USERS u ON u.id = b.user_id
                        JOIN SALES s ON s.id = b.sale_id
                        WHERE s.slug = :slug
                        ORDER BY b.time ASC
                        """, new MapSqlParameterSource().addValue("slug", slug),
                BidProjectionRowMapper::mapPointProjection);
    }

    @Override
    public Optional<BidProjection> findFirstBySale(String slug) {
        return template.query(READ_ALL_PROJECTED + "\nWHERE s.slug = :slug ORDER BY b.amount DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY",
                        new MapSqlParameterSource().addValue("slug", slug),
                        BidProjectionRowMapper::mapProjection).stream().findFirst();
    }

    @Override
    public long count(@Nullable String slug) {
        String query = slug != null
                ? "\nSELECT COUNT(*) FROM BIDS b JOIN SALES s ON s.id = b.sale_id WHERE s.slug = :slug"
                : "\nSELECT COUNT(*) FROM BIDS";

        return Objects.requireNonNullElse(
                template.query(query,
                        new MapSqlParameterSource().addValue("slug", slug),
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    @Transactional
    public void save(Bid bid) {
        String query = """
                MERGE INTO BIDS AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        amount      = :amount,
                        time        = :time,
                        sale_id     = :sale_id,
                        user_id     = :user_id
                WHEN NOT MATCHED THEN
                    INSERT (amount, time, sale_id, user_id)
                    VALUES (:amount, :time, :sale_id, :user_id);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id", bid.getId())
                .addValue("amount", bid.getAmount())
                .addValue("time", bid.getTime()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("sale_id", bid.getSaleId())
                .addValue("user_id", bid.getUserId()));
    }
}
