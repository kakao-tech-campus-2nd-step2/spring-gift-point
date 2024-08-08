package gift.domain.member;

import jakarta.validation.constraints.NotNull;

public record PointRequest(
    @NotNull
    Long point
) {

}
