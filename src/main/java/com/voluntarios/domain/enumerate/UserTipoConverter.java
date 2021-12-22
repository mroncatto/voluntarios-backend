package com.voluntarios.domain.enumerate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UserTipoConverter implements AttributeConverter<UserTipo, String> {

    @Override
    public String convertToDatabaseColumn(UserTipo attribute) {
        switch (attribute) {
            case ORGANIZACION -> {
                return "O";
            }
            case VOLUNTARIO -> {
                return "V";
            }
            default -> throw new IllegalArgumentException("Valor inesperado en Situacion: " + attribute);
        }
    }

    @Override
    public UserTipo convertToEntityAttribute(String dbData) {
        switch (dbData) {
            case "O" -> {
                return UserTipo.ORGANIZACION;
            }
            case "V" -> {
                return UserTipo.VOLUNTARIO;
            }
            default -> throw new IllegalArgumentException("Valor inesperado en Situacion: " + dbData);
        }
    }
}
