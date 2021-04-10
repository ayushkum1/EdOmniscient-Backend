package com.brainsinjars.projectbackend.pojo.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Year;

/**
 * This is an Attribute Converter that helps Converting Java types to DB type.
 *
 * This class converts java.time.Year to short to store into DB
 * And it also converts back the short to java.time.Year when retrieving from DB
 */

@Converter(autoApply = true)
public class YearAttributeConverter implements AttributeConverter<Year, Short> {
    @Override
    public Short convertToDatabaseColumn(Year year) {
        if (year != null) return (short) year.getValue();
        return null;
    }

    @Override
    public Year convertToEntityAttribute(Short dbData) {
        if (dbData != null) return Year.of(dbData);
        return null;
    }
}
