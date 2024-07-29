package gift.dto;

public class WishRequestDto {

    private final Long id;
    private final Long productId;
    private final String tokenValue;

    public WishRequestDto(Long id, Long productId, String tokenValue) {
        this.id = id;
        this.productId = productId;
        this.tokenValue = tokenValue;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getTokenValue() {
        return tokenValue;
    }
}

