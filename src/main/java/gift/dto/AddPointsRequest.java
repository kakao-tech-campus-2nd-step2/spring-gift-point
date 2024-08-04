package gift.dto;

import jakarta.validation.constraints.NotNull;

public record AddPointsRequest(
        @NotNull Long memberId,
        @NotNull int points
) {}
