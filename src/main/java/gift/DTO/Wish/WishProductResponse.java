package gift.DTO.Wish;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.domain.WishProduct;

public class WishProductResponse {
    Long id;
    UserResponse user;
    ProductResponse product;

    public WishProductResponse(){

    }

    public WishProductResponse(WishProduct wishProduct) {
        this.id = wishProduct.getId();
        this.user = new UserResponse(wishProduct.getUser());
        this.product = new ProductResponse(wishProduct.getProduct());
    }

    public Long getId(){
        return id;
    }
    public UserResponse getUser() {
        return user;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
