package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.ProductInfo;
import gift.entity.WishList;

public class WishListResponse {
    
    private Long id;
    private ProductInfo product;

    @JsonCreator
    public WishListResponse(
        @JsonProperty("id")
        Long id,
        @JsonProperty("product")
        ProductInfo product
    ){
        this.id = id;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public ProductInfo getProduct() {
        return product;
    }

    public static WishListResponse fromEntity(WishList wishList){
        return new WishListResponse(wishList.getId(), 
            new ProductInfo(
                wishList.getProduct().getId(),
                wishList.getProduct().getName(),
                wishList.getProduct().getPrice(),
                wishList.getProduct().getImageUrl()));
    }
}
