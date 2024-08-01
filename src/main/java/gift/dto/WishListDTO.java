package gift.dto;


public class WishListDTO {
    private Long userId;

    private Long productId;

    public WishListDTO(){}

    /**
     * 해당 생성자를 통해 WishListDTO 객체를 생성
     *
     * @param userId 멤버의 고유 ID
     * @param productId 상품의 고유 ID
     */
    public WishListDTO(Long userId, Long productId) {
        this.userId = userId;
        this.productId = productId;

    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

}

