package gift.dto;

public class WishResponse {
    private final Long id;
    private final ProductDto product;
    private final Long memberId;
    private final String email;

    public WishResponse(Long id, ProductDto product, Long memberId, String email) {
        this.id = id;
        this.product = product;
        this.memberId = memberId;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public ProductDto getProduct() {
        return product;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}