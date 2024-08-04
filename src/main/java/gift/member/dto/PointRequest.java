package gift.member.dto;

import jakarta.validation.constraints.NotNull;

public record PointRequest(
        @NotNull
        Integer point
) { }
