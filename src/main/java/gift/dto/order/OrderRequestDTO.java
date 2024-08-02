package gift.dto.order;

public record OrderRequestDTO(int optionId, String message,
                              int quantity, int productId, int point,
                              String phone, boolean receipt) {
}
