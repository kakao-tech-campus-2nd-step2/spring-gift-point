package gift.DTO;

import jakarta.validation.constraints.Min;


public record RequestAddPointDTO(
        @Min(value = 0, message = "충전할 포인트는 적어도 0이상입니다")
        int chargePoint
) { }


