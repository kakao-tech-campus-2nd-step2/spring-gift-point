package gift.domain;

public record OrderDTO(int optionId,
                       int productId,
                       int quantity,
                       String message) {}
