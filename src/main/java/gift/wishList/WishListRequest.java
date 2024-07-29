package gift.wishList;

public class WishListRequest {
    long optionID;

    long count;

    public WishListRequest() {
    }

    public WishListRequest(long optionID, long count) {
        this.optionID = optionID;
        this.count = count;
    }

    public WishListRequest(WishList wishList) {
        this.optionID = wishList.getOption().getId();
        this.count = wishList.getCount();
    }

    public long getOptionID() {
        return optionID;
    }

    public long getCount() {
        return count;
    }

    public void setOptionID(long optionID) {
        this.optionID = optionID;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
