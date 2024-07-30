package gift.wish.domain;

import gift.product.dto.ProductDTO;

public class WishDTO {
    private Long id;
    private ProductDTO product;

    public WishDTO(Long id, ProductDTO product) {
        this.id = id;
        this.product = product;
    }

    // getter & setter

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }
}
