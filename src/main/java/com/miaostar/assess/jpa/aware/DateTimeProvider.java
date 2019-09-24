package com.miaostar.assess.jpa.aware;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Optional;
@Component
public class DateTimeProvider  implements org.springframework.data.auditing.DateTimeProvider{
    @Override
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(LocalDateTime.now());
    }
}
