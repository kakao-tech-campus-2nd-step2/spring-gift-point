package gift.dto.pointDTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointRequestDTO(
    @NotNull(message = "이메일을 입력해야 합니다.")
    String email,
    @Min(value = 1, message = "포인트는 1 이상이어야 합니다.")
    @NotNull(message = "포인트 입력은 필수입니다.")
    Long points
) {

}
