package gift.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.dto.ProductInfo;
import gift.entity.WishList;

public class WishListResponse {
    
    private Long wishListId;
    private ProductInfo productInfo;

    @JsonCreator
    public WishListResponse(
        @JsonProperty("id")
        Long wishListId,
        @JsonProperty("product")
        ProductInfo productInfo
    ){
        this.wishListId = wishListId;
        this.productInfo = productInfo;
    }

    public Long getWishListId() {
        return wishListId;
    }

    public ProductInfo getProductInfo() {
        return productInfo;
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
