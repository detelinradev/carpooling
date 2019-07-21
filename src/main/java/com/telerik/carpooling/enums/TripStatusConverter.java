package com.telerik.carpooling.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class TripStatusConverter implements AttributeConverter<TripStatus, String> {

    @Override
    public String convertToDatabaseColumn(TripStatus tripStatus) {
        if (tripStatus == null) {
            return null;
        }
        return tripStatus.getCode();
    }

    @Override
    public TripStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(TripStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
