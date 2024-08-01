package gift.web.dto;

import jakarta.validation.constraints.NotNull;

public record MemberDto(
    @NotNull
    String email,
    @NotNull
    String password) { }
