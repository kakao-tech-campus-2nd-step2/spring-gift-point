package gift.event;

public record OrderEventDto(
        Long productId,
        Long memberId,
        Long orderId
) {
}
