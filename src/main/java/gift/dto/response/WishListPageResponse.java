package gift.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;

import gift.dto.WishListDto;
import gift.entity.WishList;

public class WishListPageResponse {
    
    private List<WishListDto> wishLists;

    public WishListPageResponse(){

    }

    public WishListPageResponse(List<WishListDto> wishLists) {
        this.wishLists = wishLists;
    }

    public List<WishListDto> getWishLists() {
        return wishLists;
    }

    public void setWishLists(List<WishListDto> wishLists) {
        this.wishLists = wishLists;
    }

    public WishListPageResponse fromPage(Page<WishList> wishListPage){
        List<WishListDto> wishList = wishListPage.getContent()
                                          .stream()
                                          .map(WishListDto::fromEntity)
                                          .collect(Collectors.toList());
    
        WishListPageResponse wishListPageResponse = new WishListPageResponse(wishList);
        return wishListPageResponse;
    }
}
