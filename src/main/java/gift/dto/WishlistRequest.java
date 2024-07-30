package gift.dto;

import gift.entity.Product;
import gift.entity.User;
import gift.entity.Wishlist;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class WishlistRequest {

    @NotNull(message = "Product ID는 필수입니다.")
    private Long productId;

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
	@Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
    private int quantity;
    
    public WishlistRequest() {}
    
    public WishlistRequest(Long productId, int quantity) {
    	this.productId = productId;
    	this.quantity = quantity;
    }

	public Long getProductId() {
        return productId;
	}

    public int getQuantity() {
        return quantity;
    }
    
    public Wishlist toEntity(User user, Product product) {
        Wishlist wishlist = new Wishlist(user, product);
        wishlist.setQuantity(this.quantity);
        return wishlist;
    }
}
