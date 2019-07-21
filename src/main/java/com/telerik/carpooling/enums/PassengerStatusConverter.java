package com.telerik.carpooling.enums;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;

@Converter(autoApply = true)
public class PassengerStatusConverter implements AttributeConverter<PassengerStatus, String> {

    @Override
    public String convertToDatabaseColumn(PassengerStatus passengerStatus) {
        if (passengerStatus == null) {
            return null;
        }
        return passengerStatus.getCode();
    }

    @Override
    public PassengerStatus convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(PassengerStatus.values())
                .filter(c -> c.getCode().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
