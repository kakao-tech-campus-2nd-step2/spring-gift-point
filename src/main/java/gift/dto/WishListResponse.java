package gift.dto;

import gift.model.Wish;
import java.util.Map;

public class WishListResponse {

    private Long memberId;
    private Map<String,Integer> wishList;


    public WishListResponse() {
    }

    public WishListResponse(Wish wish,Map<String,Integer> wishList){
        this.memberId = wish.getMember().getId();
        this.wishList = wishList;
    }

    public WishListResponse(Long memberId, Map<String, Integer> wishList) {
        this.memberId = memberId;
        this.wishList = wishList;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Map<String, Integer> getWishList() {
        return wishList;
    }

}
