package com.home.calories.repository;

import com.home.calories.model.BaseProduct;
import com.home.calories.model.Dish;
import com.home.calories.model.DishIdentity;
import com.home.calories.model.Portion;
import one.util.streamex.StreamEx;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;

@Repository
public class JdbcDishRepositoryImpl extends JdbcRepository implements DishRepository {

    private static final String QUERY_DISHES_BY_IDS = """
                select
                    dish.id       dish_id,
                    dish.name     dish_name,
                    portion.id    portion_id,
                    portion.grams portion_grams,
                    bp.id         base_product_id,
                    bp.name       base_product_name,
                    bp.kcal       base_product_kcal,
                    bp.proteins   base_product_proteins,
                    bp.fats       base_product_fats,
                    bp.carbs      base_product_carbs,
                from dish
                    left join dish_portion_mapping dpm on dpm.dish_id = dish.id
                    join portion on portion.id = dpm.portion_id
                    join base_product bp on bp.id = portion.base_product_id
                where dish.id in (:dishIds)
            """;

    private static final String QUERY_DISH_IDENTITIES_BY_ID = """
                select
                    dish.id   id,
                    dish.name name
                from dish
                where dish.id in (:dishIds)
            """;

    public JdbcDishRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Page<Dish> find(DishFilter filter, Pageable pageable) {
        Page<Long> dishIdsPage = findIds(filter, pageable);
        List<Long> dishIds = dishIdsPage.getContent();
        Map<Long, Dish> dishMap = findByIds(dishIds);
        return dishIdsPage.map(dishMap::get);
    }

    @Override
    public Page<Long> findIds(DishFilter filter, Pageable pageable) {
        String select = "SELECT id FROM " + table();
        String count = "SELECT count(*) FROM " + table();

        Map<String, Object> params = new HashMap<>();

        if (filter.getName() != null) {
            String namePattern = "%" + filter.getName() + "%";
            params.put("namePattern", namePattern);

            String where = " WHERE name ilike :namePattern";

            select += where;
            count += where;
        }

        Integer totalElements = jdbcTemplate.queryForObject(count, params, Integer.class);

        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        select += " " + getSort(pageable) + " LIMIT :limit OFFSET :offset";

        List<Long> ids = jdbcTemplate.queryForList(select, params, Long.class);

        return new PageImpl<>(ids, pageable, totalElements);
    }

    @Override
    public Page<DishIdentity> findIdentities(DishFilter filter, Pageable pageable) {
        Page<Long> dishIdsPage = findIds(filter, pageable);
        List<Long> dishIds = dishIdsPage.getContent();
        Map<Long, DishIdentity> dishMap = findIdentities(dishIds);
        return dishIdsPage.map(dishMap::get);
    }

    @Override
    public Optional<Dish> findById(Long id) {
        var map = findByIds(List.of(id));
        if (map.isEmpty()) {
            return Optional.empty();
        } else if (map.size() == 1) {
            return map.values().stream().findFirst();
        }
        throw new IllegalStateException("query for single entity return several " + map.size());
    }

    private Map<Long, Dish> findByIds(List<Long> ids) {
        List<FlatDishProjection> flatDishProjection;
        if (ids.isEmpty()) {
            flatDishProjection = new ArrayList<>();
        } else {
            flatDishProjection = jdbcTemplate.query(QUERY_DISHES_BY_IDS, Map.of("dishIds", ids), this::map);
        }

        Map<Long, Dish> dishMap = new HashMap<>();
        for (var proj : flatDishProjection) {

            if (!dishMap.containsKey(proj.dishId)) {
                var dish = new Dish(
                        proj.dishId,
                        proj.dishName,
                        new ArrayList<>()
                );
                dishMap.put(dish.getId(), dish);
            }

            if (proj.portionId != null) {
                Portion portion = new Portion(
                        proj.portionId,
                        proj.portionGrams,
                        new BaseProduct(
                                proj.baseProductId,
                                proj.baseProductName,
                                proj.baseProductKcal,
                                proj.baseProductProteins,
                                proj.baseProductFats,
                                proj.baseProductCarbs
                        )
                );
                dishMap.get(proj.dishId).getPortions().add(portion);
            }
        }
        return dishMap;
    }

    private Map<Long, DishIdentity> findIdentities(List<Long> ids) {
        List<DishIdentity> identities;
        if (ids.isEmpty()) {
            identities = new ArrayList<>();
        } else {
            identities = jdbcTemplate.query(QUERY_DISH_IDENTITIES_BY_ID, Map.of("dishIds", ids), this::mapToIdentity);
        }
        return StreamEx.of(identities)
                .mapToEntry(DishIdentity::id, Function.identity())
                .toMap();
    }

    @Override
    protected String table() {
        return "dish";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of("id", "name");
    }

    private FlatDishProjection map(ResultSet rs, int i) throws SQLException {
        return new FlatDishProjection(
                rs.getLong("dish_id"),
                rs.getString("dish_name"),
                rs.getLong("portion_id"),
                rs.getInt("portion_grams"),
                rs.getLong("base_product_id"),
                rs.getString("base_product_name"),
                rs.getDouble("base_product_kcal"),
                rs.getDouble("base_product_proteins"),
                rs.getDouble("base_product_fats"),
                rs.getDouble("base_product_carbs")
        );
    }

    private DishIdentity mapToIdentity(ResultSet rs, int i) throws SQLException {
        return new DishIdentity(
                rs.getLong("id"),
                rs.getString("name")
        );
    }

    private record FlatDishProjection(
            Long dishId,
            String dishName,
            Long portionId,
            Integer portionGrams,
            Long baseProductId,
            String baseProductName,
            double baseProductKcal,
            double baseProductProteins,
            double baseProductFats,
            double baseProductCarbs
    ) {
    }
}
