package com.home.calories.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PageableUtil {

    public static Pageable MAX_SIZE_PAGE = PageRequest.of(0, Integer.MAX_VALUE, Sort.by("id"));

    private PageableUtil() {
        throw new UnsupportedOperationException();
    }

    public static Pageable pageable(Integer page, Integer size, @Nullable String sort) {
        return PageRequest.of(page, size, parseParameterIntoSort(sort));
    }

    public static Sort parseParameterIntoSort(@Nullable String source) {
        if (source == null) {
            return Sort.unsorted();
        }
        String[] tokens = source.split(",");
        if (tokens.length == 0) {
            return Sort.unsorted();
        }
        if (tokens.length == 1) {
            return Sort.by(Sort.Order.by(tokens[0]));
        }

        List<Sort.Order> allOrders = new ArrayList<>();
        for (int i = 0; i < tokens.length; i++) {
            String current = tokens[i];
            if (i + 1 == tokens.length) {
                allOrders.add(Sort.Order.by(current));
                break;
            }
            String next = tokens[i + 1];
            var maybeDirection = Sort.Direction.fromOptionalString(next);
            if (maybeDirection.isPresent()) {
                Sort.Direction direction = maybeDirection.get();
                allOrders.add(new Sort.Order(direction, current));
                i++;
            } else {
                allOrders.add(Sort.Order.by(current));
            }
        }

        return allOrders.isEmpty() ? Sort.unsorted() : Sort.by(allOrders);
    }

}
