package com.miaostar.assess.jpa.converter;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StringToSetConverter implements AttributeConverter<Set<String>, String> {
    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return (Objects.isNull(attribute) || attribute.isEmpty())
                ? null : String.join(",", attribute);
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        return (Objects.isNull(dbData) || dbData.isEmpty())
                ? Collections.emptySet() : Arrays.stream(dbData.split(",")).collect(Collectors.toSet());
    }
}
