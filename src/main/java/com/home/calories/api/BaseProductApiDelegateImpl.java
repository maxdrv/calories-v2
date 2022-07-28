package com.home.calories.api;

import com.home.calories.openapi.api.BaseProductsApiDelegate;
import com.home.calories.openapi.model.*;
import com.home.calories.util.Comparators;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class BaseProductApiDelegateImpl implements BaseProductsApiDelegate {

    private final Map<Long, BaseProductDto> productMap;
    private final AtomicLong seq = new AtomicLong(1L);

    public BaseProductApiDelegateImpl() {
        this.productMap = new ConcurrentHashMap<>();

        Long newId = seq.getAndIncrement();

        this.productMap.put(newId, new BaseProductDto()
                .id(newId)
                .name("test 1")
                .nutrients(
                        new NutrientsDto()
                                .kcal(100.0)
                                .proteins(10.0)
                                .fats(20.0)
                                .carbs(30.0)
                ));
    }

    @Override
    public ResponseEntity<ListOfBaseProductDto> listProducts() {
        return ResponseEntity.ok(new ListOfBaseProductDto().content(listProductsOrdered()));
    }
    @Override
    public ResponseEntity<BaseProductDto> baseProductById(Long baseProductId) {
        return ResponseEntity.ok(getByIdOrThrow(baseProductId));
    }

    @Override
    public ResponseEntity<BaseProductDto> createBaseProduct(CreateBaseProductRequest request) {
        Long nextId = seq.getAndIncrement();

        BaseProductDto dto = new BaseProductDto()
                .id(nextId)
                .name(request.getName())
                .nutrients(map(request.getNutrients()));

        productMap.put(dto.getId(), dto);
        return ResponseEntity.ok(getByIdOrThrow(nextId));
    }

    @Override
    public ResponseEntity<BaseProductDto> updateBaseProduct(Long baseProductId, UpdateBaseProductRequest request) {
        BaseProductDto stored = getByIdOrThrow(baseProductId);

        BaseProductDto updated = stored
                .id(stored.getId())
                .name(request.getName())
                .nutrients(map(request.getNutrients()));

        productMap.put(updated.getId(), updated);
        return ResponseEntity.ok(getByIdOrThrow(updated.getId()));
    }

    private BaseProductDto getByIdOrThrow(Long id) {
        if (productMap.containsKey(id)) {
            return productMap.get(id);
        }
        throw new IllegalArgumentException("Product does not exists " + id);
    }

    private List<BaseProductDto> listProductsOrdered() {
        return productMap.values().stream().sorted(baseProductComparator).toList();
    }

    private final Comparator<BaseProductDto> baseProductComparator = (o1, o2) -> Comparators.LONG_ASC.compare(o1.getId(), o2.getId());

    private static NutrientsDto map(NutrientsDto dto) {
        return new NutrientsDto()
                .kcal(dto.getKcal())
                .proteins(dto.getProteins())
                .fats(dto.getFats())
                .carbs(dto.getCarbs());
    }

}
