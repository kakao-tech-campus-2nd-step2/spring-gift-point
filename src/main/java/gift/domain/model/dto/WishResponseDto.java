package gift.domain.model.dto;

public class WishResponseDto {

    private Long id;
    private Long productId;
    private Integer count;

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Integer getCount() {
        return count;
    }

    public WishResponseDto() {
    }

    public WishResponseDto(Long id, Long productId, Integer count) {
        this.id = id;
        this.productId = productId;
        this.count = count;
    }
}
