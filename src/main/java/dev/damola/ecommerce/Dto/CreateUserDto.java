package dev.damola.ecommerce.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record CreateUserDto(
        @NotEmpty
        String email,
        @NotEmpty
        String password,
        @NotEmpty
        @Pattern(regexp = "^(USER|ADMIN|USER,ADMIN)")
        String roles
) {
}
