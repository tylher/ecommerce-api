package dev.damola.ecommerce.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER( "USER"),
    ADMIN("ADMIN");

    private final String typeValue;

    Role(String typeValue) {
        this.typeValue = typeValue;
    }

}
