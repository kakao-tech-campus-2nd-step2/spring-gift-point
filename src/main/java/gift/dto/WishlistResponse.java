package gift.dto;

import gift.entity.Product;

public class WishlistResponse {

    private Long id;
    private Product product;
    
    public WishlistResponse(Long id, Product product) {
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
