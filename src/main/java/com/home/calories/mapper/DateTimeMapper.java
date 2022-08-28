package com.home.calories.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

import static com.home.calories.util.DateTimeUtil.MOSCOW_ZONE_OFFSET;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface DateTimeMapper {

    default OffsetDateTime mapToOffsetAtMoscow(Instant instant) {
        return mapToOffset(instant, MOSCOW_ZONE_OFFSET);
    }

    default OffsetDateTime mapToOffset(Instant instant, ZoneOffset offset) {
        return Optional.ofNullable(instant).map(i -> i.atOffset(offset)).orElse(null);
    }

    default Instant map(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime).map(OffsetDateTime::toInstant).orElse(null);
    }

}
