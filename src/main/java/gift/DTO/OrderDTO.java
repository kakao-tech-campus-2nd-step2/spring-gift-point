package gift.DTO;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrderDTO(
        Long id,

        @NotNull(message = "옵션 ID는 null이 될 수 없습니다.")
        Long optionId,

        @NotNull(message = "수량은 null이 될 수 없습니다.")
        Long quantity,

        LocalDateTime orderDate,

        @NotNull(message = "메시지는 null이 될 수 없습니다.")
        String message,

        Long memberId

) {
    public OrderDTO(Long optionId, Long quantity, String message, Long memberId) {
        this(null, optionId, quantity, null, message, memberId);
    }
}
