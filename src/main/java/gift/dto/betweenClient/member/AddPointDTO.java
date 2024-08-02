package gift.dto.betweenClient.member;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record AddPointDTO(@NotNull @Min(value = 1, message = "충전할 포인트는 최소 1 이여야 합니다.") Long addPoint) { }
