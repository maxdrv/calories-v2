package com.home.calories.repository;

import com.home.calories.model.BaseProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class JdbcBaseProductRepository implements BaseProductRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Page<BaseProduct> find(BaseProductFilter filter, Pageable pageable) {
        String select = "SELECT * FROM base_product";
        String count = "SELECT count(*) FROM base_product";

        Map<String, Object> params = new HashMap<>();

        if (filter.getName() != null) {
            String namePattern = "%" + filter.getName() + "%";
            params.put("namePattern", namePattern);

            String where = " WHERE name like :namePattern";

            select += where;
            count += where;
        }

        Integer totalElements = jdbcTemplate.queryForObject(count, params, Integer.class);

        params.put("limit", pageable.getPageSize());
        params.put("offset", pageable.getOffset());

        select += " " + getSort(pageable) + " LIMIT :limit OFFSET :offset";

        var baseProducts = jdbcTemplate.query(select, params, this::map);
        return new PageImpl<>(baseProducts, pageable, totalElements);
    }

    @Override
    public Optional<BaseProduct> findById(Long id) {
        if (id == null) {
            throw new RuntimeException("id should be not null");
        }

        BaseProduct result = jdbcTemplate.queryForObject("""
                        select * from base_product where id = :id;
                        """,
                Map.of("id", id),
                this::map
        );

        return result == null ? Optional.empty() : Optional.of(result);
    }

    @Override
    public BaseProduct insert(BaseProduct baseProduct) {
        return jdbcTemplate.queryForObject("""
                        insert into base_product (id, name, kcal, proteins, fats, carbs)
                            values (nextval('base_product_seq'), :name, :kcal, :proteins, :fats, :carbs)
                        returning *;
                        """,
                Map.of(
                        "name", baseProduct.getName(),
                        "kcal", baseProduct.getKcal(),
                        "proteins", baseProduct.getProteins(),
                        "fats", baseProduct.getFats(),
                        "carbs", baseProduct.getCarbs()
                ),
                this::map
        );
    }

    @Override
    public BaseProduct update(BaseProduct baseProduct) {
        if (baseProduct.getId() == null) {
            throw new RuntimeException("id should be not null");
        }

        return jdbcTemplate.queryForObject("""
                        update base_product set name = :name, kcal = :kcal, proteins = :proteins, fats = :fats, carbs = :carbs where id = :id
                        returning *;
                        """,
                Map.of(
                        "id", baseProduct.getId(),
                        "name", baseProduct.getName(),
                        "kcal", baseProduct.getKcal(),
                        "proteins", baseProduct.getProteins(),
                        "fats", baseProduct.getFats(),
                        "carbs", baseProduct.getCarbs()
                ),
                this::map
        );
    }

    @Override
    public void deleteById(Long id) {
        if (id == null) {
            throw new RuntimeException("id should be not null");
        }

        jdbcTemplate.update("delete from base_product where id = :id", Map.of("id", id));
    }

    private String getSort(Pageable pageable) {
        var sortable = Set.of("id", "name");

        var sqlSort = pageable.getSort().stream()
                .filter(order -> sortable.contains(order.getProperty()))
                .map(order -> "base_product." + order.getProperty() + " " + order.getDirection().name())
                .collect(Collectors.joining(", "));

        if (sqlSort.isBlank()) {
            return "ORDER BY base_product.id ASC";
        } else {
            return "ORDER BY " + sqlSort;
        }
    }

    private BaseProduct map(ResultSet rs, int i) throws SQLException {
        return new BaseProduct(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getDouble("kcal"),
                rs.getDouble("proteins"),
                rs.getDouble("fats"),
                rs.getDouble("carbs")
        );
    }
}
