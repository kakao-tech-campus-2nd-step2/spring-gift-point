package gift.dto.point;

import jakarta.validation.constraints.NotNull;

public class MemberPointRequest {
    public record Add(
            @NotNull
            int depositPoint
    ){

    }
}
