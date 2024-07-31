package gift.wishList;

import gift.option.OptionDTO;
import gift.option.OptionReadResponse;

public class WishListResponse {
    long id;
    Object option;

    public WishListResponse() {
    }

    public WishListResponse(long id, Object option) {
        this.id = id;
        this.option = option;
    }

    public WishListResponse(WishList wishList){
        this.id = wishList.getId();
        this.option = new OptionDTO(wishList.getOption().getId());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }
}
