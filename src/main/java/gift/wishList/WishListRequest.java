package gift.wishList;

import jakarta.validation.constraints.Min;

public class WishListRequest {
    @Min(value = 0)
    long optionId;

    public WishListRequest() {
    }

    public WishListRequest(long optionId) {
        this.optionId = optionId;
    }

    public WishListRequest(WishList wishList) {
        this.optionId = wishList.getOption().getId();
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

}
