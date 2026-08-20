package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.CategoryDao;
import com.rougeux.auction.dal.sqlserver.mapper.CategoryRowMapper;
import com.rougeux.auction.domain.bo.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlCategoryDao implements CategoryDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as category_id, slug as category_slug, label FROM CATEGORIES
            """;

    @Override
    public List<Category> findAll() {
        return template.query(READ_ALL, CategoryRowMapper::mapCategory);
    }

    @Override
    public List<Category> findAllWithCount() {
        String query= """
                SELECT c.id as category_id, c.slug as category_slug,
                       c.label, COUNT(i.id) AS count
                FROM CATEGORIES c
                LEFT JOIN ITEMS i ON i.category_id = c.id
                GROUP BY c.id, c.slug, c.label
                HAVING COUNT(i.id) > 0
                """;

        return template.query(query, CategoryRowMapper::mapCategoryCount);
    }

    @Override
    public Optional<Category> findById(String id) {
        return template.query(READ_ALL + "\nWHERE id = :id",
                        new MapSqlParameterSource().addValue("id", id),
                        CategoryRowMapper::mapCategory).stream().findFirst();
    }

    @Override
    public long count() {
        return Objects.requireNonNullElse(
                template.query("SELECT COUNT(*) FROM CATEGORIES",
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    @Transactional
    public void save(Category category) {
        String query= """
                MERGE INTO CATEGORIES AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        slug        = :slug,
                        label       = :label
                WHEN NOT MATCHED THEN
                    INSERT (slug, label) VALUES (:slug, :label);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id",          category.getId())
                .addValue("slug",    category.getSlug())
                .addValue("label",   category.getLabel()));
    }
}
