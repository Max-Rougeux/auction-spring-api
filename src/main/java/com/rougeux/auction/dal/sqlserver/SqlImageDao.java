package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.ImageDao;
import com.rougeux.auction.dal.sqlserver.mapper.ImageRowMapper;
import com.rougeux.auction.domain.bo.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlImageDao implements ImageDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL="""
                SELECT id as image_id, filename, directory, width, height, upload_at, type
                FROM IMAGES
                """;

    @Override
    public List<Image> findAll() {
        return template.query(READ_ALL, ImageRowMapper::mapImage);
    }

    @Override
    public List<Image> findAllUserThumbnails() {
        return template.query(READ_ALL + "\nWHERE type = 'USER'", ImageRowMapper::mapImage);
    }

    @Override
    public List<Image> findAllItemThumbnails() {
        return template.query(READ_ALL + "\nWHERE type = 'ITEM'", ImageRowMapper::mapImage);
    }

    @Override
    public Optional<Image> findById(String id) {
        return template.query(READ_ALL + "\nWHERE id = :id",
                        new MapSqlParameterSource().addValue("id", id),
                        ImageRowMapper::mapImage).stream().findFirst();
    }

    @Override
    public Optional<Image> findByFilename(String filename) {
        return template.query(READ_ALL + "\nWHERE filename = :filename",
                        new MapSqlParameterSource().addValue("filename", filename),
                        ImageRowMapper::mapImage).stream().findFirst();
    }

    @Override
    public long count() {
        return Objects.requireNonNullElse(
                template.query("SELECT COUNT(*) FROM IMAGES",
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    @Transactional
    public void save(Image image) {
        String query= """
                MERGE INTO IMAGES AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        filename    = :filename,
                        directory   = :directory,
                        width       = :width,
                        height      = :height,
                        type        = :type
                WHEN NOT MATCHED THEN
                    INSERT (filename, directory, width, height, type, upload_at)
                    VALUES (:filename, :directory, :width, :height, :type, :upload_at);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id",          image.getId())
                .addValue("filename",    image.getFilename())
                .addValue("directory",   image.getDirectory())
                .addValue("width",       image.getWidth())
                .addValue("height",      image.getHeight())
                .addValue("type",        image.getType().name())
                .addValue("upload_at",   image.getUploadAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime()));
    }

    @Override
    public void saveAll(List<Image> images) {
        template.batchUpdate("""
                INSERT INTO IMAGES(filename, directory, width, height, type, upload_at)
                VALUES (:filename, :directory, :width, :height, :type, :upload_at);
                """, images.stream().map(
                        i -> new MapSqlParameterSource()
                                    .addValue("id",          i.getId())
                                    .addValue("filename",    i.getFilename())
                                    .addValue("directory",   i.getDirectory())
                                    .addValue("width",       i.getWidth())
                                    .addValue("height",      i.getHeight())
                                    .addValue("type",        i.getType().name())
                                    .addValue("upload_at",   i.getUploadAt()
                                        .atZone(ZoneId.systemDefault()).toLocalDateTime()))
                                    .toArray(SqlParameterSource[]::new));
    }
}
