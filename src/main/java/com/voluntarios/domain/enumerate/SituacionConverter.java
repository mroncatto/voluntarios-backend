package com.voluntarios.domain.enumerate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class SituacionConverter implements AttributeConverter<Situacion, String> {

    @Override
    public String convertToDatabaseColumn(Situacion attribute) {
        switch (attribute) {
            case PENDIENTE -> {
                return "P";
            }
            case FINALIZADO -> {
                return "F";
            }
            default -> throw new IllegalArgumentException("Valor inesperado en Situacion: " + attribute);
        }
    }

    @Override
    public Situacion convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "P" -> {
                return Situacion.PENDIENTE;
            }
            case "F" -> {
                return Situacion.FINALIZADO;
            }
            default -> throw new IllegalArgumentException("Valor inesperado en Situacion: " + dbData);
        }
    }
}
