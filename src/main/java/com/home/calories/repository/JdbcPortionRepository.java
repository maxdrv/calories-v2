package com.home.calories.repository;

import com.home.calories.model.Portion;
import com.home.calories.model.PortionInsert;
import com.home.calories.model.PortionRecord;
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

        PortionRecord record = jdbcTemplate.queryForObject(
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
                   RETURNING portion_id
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
                    and (select count(*) from dish_portion_mapping where dish_id=:dishId and portion_id=:portionId) > 0
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
    public void deletePortion(Long dishId, Long portionId) {
        if (dishId == null || portionId == null) {
            throw new RuntimeException("id should be not null");
        }

        List<Long> portionIds = getPortionsByDishId(dishId);
        if (portionIds.contains(portionId)) {
            String deleteMappingsSql = "delete from dish_portion_mapping where portion_id=:portionId";
            String deletePortionsSql = "delete from portion where id=:portionId";

            jdbcTemplate.update(deleteMappingsSql, Map.of("portionId", portionId));
            jdbcTemplate.update(deletePortionsSql, Map.of("portionId", portionId));
        }
    }

    @Override
    public void deletePortionsByDish(Long dishId) {
        if (dishId == null) {
            throw new RuntimeException("id should be not null");
        }

        List<Long> portionIds = getPortionsByDishId(dishId);
        if (!portionIds.isEmpty()) {
            String deleteMappingsSql = "delete from dish_portion_mapping where dish_id=:dishId";
            String deletePortionsSql = "delete from portion where id in (:portionIds)";

            jdbcTemplate.update(deleteMappingsSql, Map.of("dishId", dishId));
            jdbcTemplate.update(deletePortionsSql, Map.of("portionIds", portionIds));
        }
    }

    private List<Long> getPortionsByDishId(Long dishId) {
        if (dishId == null) {
            throw new RuntimeException("id should be not null");
        }
        String selectPortionsToDeleteSql = "select portion_id from dish_portion_mapping where dish_id=:dishId";
        return jdbcTemplate.queryForList(selectPortionsToDeleteSql, Map.of("dishId", dishId), Long.class);
    }

    @Override
    protected String table() {
        return "portion";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

    private PortionRecord mapToRecord(ResultSet rs, int i) throws SQLException {
        return new PortionRecord(
                rs.getLong("id"),
                rs.getInt("grams"),
                rs.getLong("base_product_id")
        );
    }

}
