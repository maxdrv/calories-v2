package com.home.calories.repository;

import com.home.calories.model.Portion;
import com.home.calories.model.PortionInsert;
import com.home.calories.model.PortionUpdate;
import one.util.streamex.StreamEx;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Repository
public class JdbcPortionRepository extends JdbcRepository implements PortionRepository {

    private final BaseProductRepository baseProductRepository;

    public JdbcPortionRepository(NamedParameterJdbcTemplate jdbcTemplate, BaseProductRepository baseProductRepository) {
        super(jdbcTemplate);
        this.baseProductRepository = baseProductRepository;
    }

    @Override
    public int count() {
        return super.count();
    }

    @Override
    public Optional<Portion> findById(Long portionId) {
        if (portionId == null) {
            throw new RuntimeException("id should be not null");
        }

        Portion.PortionRecord record = jdbcTemplate.queryForObject(
                "select * from portion where id = :id;",
                Map.of("id", portionId),
                this::mapToRecord
        );

        if (record == null) {
            return Optional.empty();
        }

        var baseProduct = baseProductRepository.findById(record.baseProductId()).orElseThrow();
        return Optional.of(new Portion(record.id(), record.grams(), baseProduct));
    }

    @Override
    public void insertPortions(List<PortionInsert> portions) {
        if (portions.isEmpty()) {
            return;
        }

        String sql = """
                WITH portion_key AS
                        (INSERT INTO portion (id, grams, base_product_id) VALUES (nextval('portion_seq'), :grams, :base_product_id) RETURNING id)
                INSERT INTO dish_portion_mapping (dish_id, portion_id)
                   SELECT :dish_id, portion_key.id
                   FROM portion_key
                """;

        var sqlParameterSourceArray = StreamEx.of(portions)
                .map(portion -> Map.of(
                        "grams", portion.grams(),
                        "base_product_id", portion.baseProductId(),
                        "dish_id", portion.dishId()
                ))
                .map(MapSqlParameterSource::new)
                .toArray(new SqlParameterSource[0]);

        jdbcTemplate.batchUpdate(sql, sqlParameterSourceArray);
    }

    @Override
    public Portion insertPortion(PortionInsert portionInsert) {
        String sql = """
                WITH portion_key AS
                        (INSERT INTO portion (id, grams, base_product_id) VALUES (nextval('portion_seq'), :grams, :base_product_id) RETURNING id)
                INSERT INTO dish_portion_mapping (dish_id, portion_id)
                   SELECT :dish_id, portion_key.id
                   FROM portion_key
                   RETURNING id
                """;

        var createdId = jdbcTemplate.queryForObject(
                sql,
                Map.of(
                        "grams", portionInsert.grams(),
                        "base_product_id", portionInsert.baseProductId(),
                        "dish_id", portionInsert.dishId()
                ),
                Long.class
        );

        return findById(createdId).orElseThrow();
    }

    @Override
    public Portion updatePortion(PortionUpdate update) {
        var updatedId = jdbcTemplate.queryForObject(
                """
                update portion set grams=:grams, base_product_id=:baseProductId
                where id=:portionId
                    and (select count(*) from dish_portion_mapping where dish_id=:dishId and portion_id=:portionId) > 1
                returning id;
                """,
                Map.of(
                        "dishId", update.dishId(),
                        "portionId", update.portionId(),
                        "grams", update.grams(),
                        "baseProductId", update.baseProductId()
                ),
                Long.class
        );

        return findById(updatedId).orElseThrow();
    }

    @Override
    public void deletePortions(Long dishId, List<Long> portionIds) {
        if (dishId == null) {
            throw new RuntimeException("id should be not null");
        }
        if (portionIds.isEmpty()) {
            return;
        }

        String deleteMappingsSql = "delete from dish_portion_mapping where dish_id=:dishId";
        String deletePortionsSql = "delete from portion where id in (:portionIds)";

        jdbcTemplate.update(deleteMappingsSql, Map.of("dishId", dishId));
        jdbcTemplate.update(deletePortionsSql, Map.of("portionIds", portionIds));
    }

    @Override
    protected String table() {
        return "portion";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

    private Portion.PortionRecord mapToRecord(ResultSet rs, int i) throws SQLException {
        return new Portion.PortionRecord(
                rs.getLong("id"),
                rs.getInt("name"),
                rs.getLong("base_product_id")
        );
    }

}
