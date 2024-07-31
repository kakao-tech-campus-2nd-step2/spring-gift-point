package gift.wish.service.command;

public record WishCommand(
        Long productId,
        Long memberId,
        Integer amount
) {
}
