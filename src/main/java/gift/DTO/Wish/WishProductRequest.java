package gift.DTO.Wish;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;

public class WishProductRequest {
    UserResponse user;
    ProductResponse product;

    public WishProductRequest(){

    }

    public WishProductRequest(UserResponse user, ProductResponse product) {
        this.user = user;
        this.product = product;
    }

    public UserResponse getUser() {
        return user;
    }

    public ProductResponse getProduct() {
        return product;
    }
}
