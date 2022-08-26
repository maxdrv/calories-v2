package com.home.calories.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.Nullable;

import java.util.Optional;

@RequiredArgsConstructor
public class PageableBuilder {

    public static final String ID_ASC_SORT = "id,asc";

    private final Integer page;
    private final Integer size;
    private String sort;

    public static PageableBuilder of(Integer page, Integer size) {
        return new PageableBuilder(page, size);
    }

    public PageableBuilder sortOrDefault(@Nullable String sort, String defaultSort) {
        this.sort = Optional.ofNullable(sort).orElse(defaultSort);
        return this;
    }

    public PageableBuilder sortOrIdAsc(@Nullable String sort) {
        return sortOrDefault(sort, ID_ASC_SORT);
    }

    public Pageable build() {
        return PageableUtil.pageable(page, size, sort);
    }

}
