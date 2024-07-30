package gift.dto;

import gift.model.Wish;

public class WishDTO {
    private long id;
    private Long productId;
    private String productName;
    private Integer productPrice;
    private String productImageUrl;

    public WishDTO(long id, ProductDTO productDTO) {
        this.id = id;
        this.productId = productDTO.getId();
        this.productName = productDTO.getName();
        this.productPrice = productDTO.getPrice();
        this.productImageUrl = productDTO.getImageUrl();
    }

    public static WishDTO getWishProductDTO(Wish wish) {
        return new WishDTO(
                wish.getId(),
                ProductDTO.getProductDTO(wish.getProduct())  // ProductDTO로 변환
        );
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public long getId() {
        return id;
    }
}
