package gift.dto;

import gift.model.WishList;

public class WishListResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private String productImageUrl;

    public WishListResponseDTO(Long id, Long productId, String productName, String productImageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public static WishListResponseDTO fromEntity(WishList wishList) {
        return new WishListResponseDTO(
            wishList.getId(),
            wishList.getProduct().getId(),
            wishList.getProduct().getName(),
            wishList.getProduct().getImageUrl()
        );
    }
}
