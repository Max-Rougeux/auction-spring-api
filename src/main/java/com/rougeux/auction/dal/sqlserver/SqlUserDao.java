package com.rougeux.auction.dal.sqlserver;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.dal.sqlserver.mapper.UserRowMapper;
import com.rougeux.auction.dal.sqlserver.mapper.projection.UserProjectionRowMapper;
import com.rougeux.auction.domain.bo.User;
import com.rougeux.auction.domain.enums.Role;
import com.rougeux.auction.domain.projection.UserProjection;
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
import java.util.stream.Collectors;

@Repository
@Profile("sqlserver")
@RequiredArgsConstructor
public class SqlUserDao implements UserDao {

    private final NamedParameterJdbcTemplate template;

    private static final String READ_ALL = """
            SELECT id as user_id, username, slug, firstname, lastname, phone,
                   credit, roles, enabled, created_at, image_id
            FROM USERS
            """;

    private static final String READ_PRINCIPAL = """
            SELECT id as user_id, username, password, slug, credit, roles, enabled
            FROM USERS
            """;

    private static final String READ_ALL_PROJECTED = """
            SELECT u.id as user_id, u.username, u.slug, u.firstname, u.lastname, u.phone,
                   u.credit, u.roles, u.enabled, u.created_at, u.image_id,
                   i.filename, i.width, i.height
            FROM USERS as u
            JOIN IMAGES i ON i.id = u.image_id
            """;

    @Override
    public List<User> findAll() {
        return template.query(READ_ALL, UserRowMapper::mapUser);
    }

    @Override
    public List<UserProjection> findAllProjected(int page, int limit) {
        return template.query(READ_ALL_PROJECTED + "\nORDER BY u.created_at DESC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY",
                new MapSqlParameterSource()
                        .addValue("offset", (long) (page - 1) * limit)
                        .addValue("limit", limit),
                UserProjectionRowMapper::mapProjection);
    }

    @Override
    public Optional<User> findById(String id) {
        return template.query(READ_ALL + "\nWHERE id = :id",
                        new MapSqlParameterSource().addValue("id", id),
                        UserRowMapper::mapUser).stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return template.query(READ_PRINCIPAL + "\nWHERE username = :username",
                        new MapSqlParameterSource().addValue("username", username),
                        UserRowMapper::mapPrincipal).stream().findFirst();
    }

    @Override
    public Optional<UserProjection> findProjectedByUsername(String username) {
        return template.query(READ_ALL_PROJECTED + "\nWHERE u.username = :username",
                        new MapSqlParameterSource().addValue("username", username),
                        UserProjectionRowMapper::mapProjection).stream().findFirst();
    }

    @Override
    public Optional<User> findBySlug(String slug) {
        return template.query(READ_ALL + "\nWHERE slug = :slug",
                        new MapSqlParameterSource().addValue("slug", slug),
                        UserRowMapper::mapUser).stream().findFirst();
    }

    @Override
    public Optional<UserProjection> findProjectedBySlug(String slug) {
        return template.query(READ_ALL_PROJECTED + "\nWHERE u.slug = :slug",
                        new MapSqlParameterSource().addValue("slug", slug),
                        UserProjectionRowMapper::mapProjection).stream().findFirst();
    }


    @Override
    public long count() {
        return Objects.requireNonNullElse(
                template.query("SELECT COUNT(*) FROM USERS",
                        rs -> rs.next() ? rs.getLong(1) : 0L),
                0L);
    }

    @Override
    @Transactional
    public void save(User user) {
        String query= """
                MERGE INTO USERS AS target
                USING (SELECT :id AS id) as source
                ON target.id = source.id
                WHEN MATCHED THEN
                    UPDATE SET
                        username     = :username,
                        slug         = :slug,
                        firstname    = :firstname,
                        lastname     = :lastname,
                        phone        = :phone,
                        credit       = :credit,
                        roles        = :roles,
                        enabled      = :enabled,
                        created_at   = :created_at,
                        image_id     = :image_id
                WHEN NOT MATCHED THEN
                    INSERT (username, password, slug, firstname, lastname, phone,
                            credit, roles, enabled, created_at, image_id)
                    VALUES (:username, :password, :slug, :firstname, :lastname, :phone,
                            :credit, :roles, :enabled, :created_at, :image_id);
                """;

        template.update(query, new MapSqlParameterSource()
                .addValue("id",          user.getId())
                .addValue("username",    user.getUsername())
                .addValue("password",    user.getPassword())
                .addValue("slug",        user.getSlug())
                .addValue("firstname",   user.getFirstname())
                .addValue("lastname",    user.getLastname())
                .addValue("phone",       user.getPhone())
                .addValue("credit",      user.getCredit())
                .addValue("roles",       user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.joining(",")))
                .addValue("enabled",     user.isEnabled())
                .addValue("created_at",   user.getCreatedAt()
                        .atZone(ZoneId.systemDefault()).toLocalDateTime())
                .addValue("image_id",     user.getImageId()));
    }

    @Override
    public void updateCredit(String id, int credit) {
        template.update(
                "UPDATE USERS SET credit = :credit WHERE id = :id",
                new MapSqlParameterSource()
                        .addValue("credit", credit)
                        .addValue("id", id));
    }

    @Override
    public void debit(String id, int amount) {
        template.update("UPDATE USERS SET credit = credit - :amount WHERE id = :id", new MapSqlParameterSource()
                .addValue("amount", amount)
                .addValue("id", id));
    }

    @Override
    public void refund(String id, int amount) {
        template.update("UPDATE USERS SET credit = credit + :amount WHERE id = :id", new MapSqlParameterSource()
                .addValue("amount", amount)
                .addValue("id", id));
    }
}
