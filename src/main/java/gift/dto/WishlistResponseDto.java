package gift.dto;

import gift.entity.Product;

public class WishlistResponseDto {

    private Long id;
    private ProductInfoDto product;

    public WishlistResponseDto() {
    }

    public WishlistResponseDto(Long id, ProductInfoDto product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductInfoDto getProduct() {
        return product;
    }

    public void setProduct(ProductInfoDto product) {
        this.product = product;
    }

}
