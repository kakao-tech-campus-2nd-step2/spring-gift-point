package gift.user.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PointRequestDto(

    @NotNull(message = "포인트 값을 입력해야 합니다.")
    @Min(value = 0, message = "포인트는 0 이상이어야 합니다.")
    Integer point,

    @NotNull(message = "적립 또는 사용을 지정해야 합니다.")
    Boolean isCredit // true: 적립, false: 사용

) {

}
