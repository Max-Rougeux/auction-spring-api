package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.BrandDao;
import com.rougeux.auction.dal.sqlserver.mapper.BrandRowMapper;
import com.rougeux.auction.domain.bo.Brand;
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
public class SqlBrandDao implements BrandDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as brand_id, name FROM BRANDS
            """;

    @Override
    public List<Brand> findAll() {
        return template.query(READ_ALL, BrandRowMapper::mapBrand);
    }

    @Override
    public Optional<Brand> findById(String id) {
        return template.query(READ_ALL + "\nWHERE id = :id",
                        new MapSqlParameterSource().addValue("id", id),
                        BrandRowMapper::mapBrand).stream().findFirst();
    }

    @Override
    public long count() {
        return Objects.requireNonNullElse(
                template.query("SELECT COUNT(*) FROM BRANDS",
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    public void save(Brand brand) {
        String query= """
                MERGE INTO BRANDS AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        name = :name
                WHEN NOT MATCHED THEN
                    INSERT (name) VALUES (:name);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id",          brand.getId())
                .addValue("name",        brand.getName()));
    }
}
