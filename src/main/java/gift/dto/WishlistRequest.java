package gift.dto;

import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import jakarta.validation.constraints.NotNull;

public class WishlistRequest {

    @NotNull(message = "Product ID는 필수입니다.")
    private Long productId;
    
    public WishlistRequest() {}
    
    public WishlistRequest(Long productId) {
    	this.productId = productId;
    }

	public Long getProductId() {
        return productId;
	}

    
    public Wishlist toEntity(User user, Product product) {
        Wishlist wishlist = new Wishlist(user, product);
        return wishlist;
    }
}
