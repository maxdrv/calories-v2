package com.home.calories.repository;

import com.home.calories.model.baseProduct.BaseProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BaseProductRepository {

    Page<BaseProduct> find(BaseProductFilter filter, Pageable pageable);

    Optional<BaseProduct> findById(Long id);

    BaseProduct insert(BaseProduct baseProduct);

    BaseProduct update(BaseProduct baseProduct);

    void deleteById(Long id);

}
