package gift.auth.jwt;

import jakarta.validation.constraints.NotBlank;

public record JwtToken(@NotBlank String token) {}
