package gift.dto.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class GetWishListResponse {
    
    private List<WishListResponse> wishList;

    @JsonCreator
    public GetWishListResponse(
        @JsonProperty("wishlist")
        List<WishListResponse> wishList
    ){
        this.wishList = wishList;
    }

    public List<WishListResponse> getWishList(){
        return wishList;
    }
}
