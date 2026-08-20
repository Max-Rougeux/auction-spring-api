package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.ItemDao;
import com.rougeux.auction.dal.sqlserver.mapper.ItemRowMapper;
import com.rougeux.auction.domain.bo.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlItemDao implements ItemDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as item_id, model, description, condition, year, is_gem,
                   image_id, user_id, brand_id, category_id
            FROM ITEMS
            """;

    @Override
    public List<Item> findAll() {
        return template.query(READ_ALL, ItemRowMapper::mapItem);
    }

    @Override
    public Optional<Item> findById(String id) {
        return template.query(READ_ALL + "\nWHERE id = :id",
                        new MapSqlParameterSource().addValue("id", id),
                        ItemRowMapper::mapItem).stream().findFirst();
    }

    @Override
    public long count() {
        return Objects.requireNonNullElse(
                template.query("SELECT COUNT(*) FROM ITEMS",
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    public void save(Item item) {
        String query = """
                MERGE INTO ITEMS AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        model       = :model,
                        description = :description,
                        condition   = :condition,
                        year        = :year,
                        user_id     = :user_id,
                        image_id    = :image_id,
                        brand_id    = :brand_id,
                        category_id = :category_id
                WHEN NOT MATCHED THEN
                    INSERT (id, model, description, condition, year, is_gem,
                            image_id, brand_id, category_id, user_id)
                    VALUES (:id, :model, :description, :condition, :year, :isGem,
                            :image_id, :brand_id, :category_id, :user_id);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id",          item.getId())
                .addValue("model",       item.getModel())
                .addValue("description", item.getDescription())
                .addValue("condition",   item.getCondition().name())
                .addValue("year",        item.getYear())
                .addValue("isGem",       item.isGem())
                .addValue("image_id",    item.getImageId())
                .addValue("brand_id",    item.getBrandId())
                .addValue("category_id", item.getCategoryId())
                .addValue("user_id",     item.getUserId()));
    }
}
