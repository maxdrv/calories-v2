package com.home.calories.repository.implementation;

import com.home.calories.model.dish.Dish;
import com.home.calories.model.mealHistory.*;
import com.home.calories.repository.DishRepository;
import com.home.calories.repository.MealHistoryRepository;
import com.home.calories.util.DbUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.home.calories.util.DateTimeUtil.MOSCOW_ZONE_ID;

@Repository
public class JdbcMealHistoryRepository extends JdbcRepository implements MealHistoryRepository {

    private final DishRepository dishRepository;

    public JdbcMealHistoryRepository(NamedParameterJdbcTemplate jdbcTemplate, DishRepository dishRepository) {
        super(jdbcTemplate);
        this.dishRepository = dishRepository;
    }

    @Override
    public List<MealHistory> findAllByDate(MealHistoryFilter filter) {
        var records = jdbcTemplate.query(
                "select * from meal_history where consumed_at::date = :date ORDER BY consumed_at asc",
                Map.of("date", filter.date()),
                this::map
        );

        var dishIds = records.stream().map(MealHistoryRecord::dishId).toList();
        var dishById = dishRepository.findByIds(dishIds);
        return records.stream()
                .map(record -> new MealHistory(
                                record.id(),
                                record.createdAt(),
                                record.updatedAt(),
                                dishById.get(record.dishId()),
                                record.consumedAt()
                        )
                )
                .toList();
    }

    public static void main(String[] args) {
        var l = LocalDate.of(1990, 2, 1);

        var a = l.atStartOfDay(MOSCOW_ZONE_ID);
        System.out.println(a);
    }

    @Override
    public MealHistory insert(MealHistoryInsert insert) {
        var record = jdbcTemplate.queryForObject("""
                        insert into meal_history (id, created_at, updated_at, dish_id, consumed_at)
                            values (nextval('meal_history_seq'), now(), now(), :dishId, now())
                        returning *;
                        """,
                Map.of("dishId", insert.dishId()),
                this::map
        );

        Dish dish = dishRepository.findById(record.dishId()).orElseThrow();
        return new MealHistory(
                record.id(),
                record.createdAt(),
                record.updatedAt(),
                dish,
                record.consumedAt()
        );
    }

    @Override
    public MealHistory update(MealHistoryUpdate update) {
        var record = jdbcTemplate.queryForObject("""
                        update meal_history set update_at=now(), dish_id=:dishId, consumedAt=:consumedAt
                            where id=:mealHistoryId
                        returning *;
                        """,
                Map.of(
                        "dishId", update.dishId(),
                        "consumedAt", update.consumedAt(),
                        "mealHistoryId", update.mealHistoryId()
                ),
                this::map
        );

        Dish dish = dishRepository.findById(record.dishId()).orElseThrow();
        return new MealHistory(
                record.id(),
                record.createdAt(),
                record.updatedAt(),
                dish,
                record.consumedAt()
        );
    }

    @Override
    public void delete(Long mealHistoryId) {
        if (mealHistoryId == null) {
            throw new RuntimeException("id should be not null");
        }

        jdbcTemplate.update("delete from meal_history where id=:id", Map.of("id", mealHistoryId));
    }

    @Override
    protected String table() {
        return "meal_history";
    }

    @Override
    protected Set<String> sortableColumns() {
        return Set.of();
    }

    private MealHistoryRecord map(ResultSet rs, int i) throws SQLException {
        return new MealHistoryRecord(
                DbUtil.getLong(rs, "id"),
                DbUtil.getInstant(rs, "created_at"),
                DbUtil.getInstant(rs, "updated_at"),
                DbUtil.getLong(rs, "dish_id"),
                DbUtil.getInstant(rs, "consumed_at")
        );
    }

}
