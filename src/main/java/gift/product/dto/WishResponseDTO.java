package gift.product.dto;

import gift.product.model.Product;

public class WishResponseDTO {

    private Long id;
    private Product product;

    public WishResponseDTO(Long id, Product product) {
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

}
