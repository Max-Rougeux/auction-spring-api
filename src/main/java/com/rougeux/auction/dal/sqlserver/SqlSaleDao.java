package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.dal.sqlserver.mapper.SaleRowMapper;
import com.rougeux.auction.dal.sqlserver.mapper.projection.SaleProjectionRowMapper;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.domain.projection.SaleProjection;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlSaleDao implements SaleDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as sale_id, slug, created_at, started_at, ended_at,
                   starting_price, current_price, likes, state, item_id
            FROM SALES
            """;
    private static final String READ_ALL_PROJECTED = """
             SELECT s.id AS sale_id, s.slug, s.created_at, s.started_at, s.ended_at, s.starting_price,
                    s.current_price, s.likes, s.state, s.item_id,
                    i.model, i.description, i.condition, i.year, i.is_gem, i.brand_id, i.category_id, i.user_id, i.image_id,
                    b.name, c.slug AS category_slug, c.label, it.filename, it.width,
                    it.height, u.slug AS user_slug, u.firstname, u.lastname, u.created_at AS user_created_at,
                    ui.filename as user_filename, ui.width as user_width, ui.height as user_height
            FROM SALES s
            JOIN ITEMS i        ON i.id         = s.item_id
            JOIN BRANDS b       ON b.id         = i.brand_id
            JOIN CATEGORIES c   ON c.id         = i.category_id
            JOIN IMAGES it      ON it.id        = i.image_id
            JOIN USERS u        ON u.id         = i.user_id
            JOIN IMAGES ui      ON ui.id        = u.image_id
            """;

    private static final String COUNT = "SELECT COUNT(*) FROM SALES s";

    @Override
    public List<Sale> findAll() {
        return template.query(READ_ALL, SaleRowMapper::mapSale);
    }

    @Override
    public Optional<Sale> findBySlugForUpdate(String slug) {
        return template.query("""
                        SELECT s.id AS sale_id, s.slug, s.created_at, s.started_at, s.ended_at, s.starting_price,
                               s.current_price, s.likes, s.state, s.item_id
                        FROM SALES s WITH (UPDLOCK, ROWLOCK) WHERE s.slug = :slug
                        """,
                new MapSqlParameterSource().addValue("slug", slug),
                SaleRowMapper::mapSale).stream().findFirst();
    }

    @Override
    public List<SaleProjection> findAllProjected(int page, int limit, @Nullable String category) {
        String query = READ_ALL_PROJECTED
                + (category != null ? "\nWHERE c.slug = :category" : "")
                + "\nORDER BY s.ended_at DESC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";

        return template.query(query, new MapSqlParameterSource()
                        .addValue("category", category)
                        .addValue("offset", (long) (page - 1) * limit)
                        .addValue("limit", limit),
                SaleProjectionRowMapper::mapProjection);
    }

    @Override
    public Optional<SaleProjection> findProjectedBySlug(String slug) {
        return template.query(READ_ALL_PROJECTED + "\nWHERE s.slug = :slug", new MapSqlParameterSource()
                        .addValue("slug", slug),
                SaleProjectionRowMapper::mapProjection).stream().findFirst();
    }

    @Override
    public long count(@Nullable String category) {
        String query = category != null
                ? COUNT + "\nJOIN ITEMS i ON i.id = s.item_id JOIN CATEGORIES c ON c.id = i.category_id WHERE c.slug = :category "
                : COUNT;

        return Objects.requireNonNullElse(
                template.query(query,
                        new MapSqlParameterSource().addValue("category", category),
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    public void save(Sale sale) {
        String query = """
                MERGE INTO SALES AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        slug            = :slug,
                        created_at      = :created_at,
                        started_at      = :started_at,
                        ended_at        = :ended_at,
                        starting_price  = :starting_price,
                        current_price   = :current_price,
                        likes           = :likes,
                        state           = :state,
                        item_id         = :item_id
                WHEN NOT MATCHED THEN
                    INSERT (slug, created_at, started_at, ended_at, starting_price,
                            current_price, likes, state, item_id)
                    VALUES (:slug, :created_at, :started_at, :ended_at, :starting_price,
                            :current_price, :likes, :state, :item_id);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id", sale.getId())
                .addValue("slug", sale.getSlug())
                .addValue("created_at", sale.getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("started_at", sale.getStartedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("ended_at", sale.getEndedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("starting_price", sale.getStartingPrice())
                .addValue("current_price", sale.getCurrentPrice())
                .addValue("likes", sale.getLikes())
                .addValue("state", sale.getState().name())
                .addValue("item_id", sale.getItemId()));

    }
}
