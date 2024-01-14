package dev.damola.ecommerce.Dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginDto(
        @NotEmpty
        String email,
        @NotEmpty
        String password

) {
}
