package gift.dto;


public class WishListDTO {
    private Long wishId;

    private Long productId;

    public WishListDTO(){}

    /**
     * 해당 생성자를 통해 WishListDTO 객체를 생성
     *
     * @param wishId 멤버의 고유 ID
     * @param productId 상품의 고유 ID
     */
    public WishListDTO(Long wishId, Long productId) {
        this.wishId = wishId;
        this.productId = productId;

    }

    public Long getWishId() {
        return wishId;
    }

    public Long getProductId() {
        return productId;
    }

}

