package com.odakota.tms.mapper.convert;

import com.odakota.tms.enums.process.ProcessStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * @author haidv
 * @version 1.0
 */
@Converter
public class ProcessStatusConverter implements AttributeConverter<ProcessStatus, Integer> {

    /**
     * Converts the value stored in the entity attribute into the data representation to be stored in the database.
     *
     * @param attribute the entity attribute value to be converted
     * @return the converted data to be stored in the database column
     */
    @Override
    public Integer convertToDatabaseColumn(ProcessStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    /**
     * Converts the data stored in the database column into the value to be stored in the entity attribute. Note that it
     * is the responsibility of the converter writer to specify the correct <code>dbData</code> type for the
     * corresponding column for use by the JDBC driver: i.e., persistence providers are not expected to do such type
     * conversion.
     *
     * @param dbData the data from the database column to be converted
     * @return the converted value to be stored in the entity attribute
     */
    @Override
    public ProcessStatus convertToEntityAttribute(Integer dbData) {
        return ProcessStatus.of(dbData);
    }
}
