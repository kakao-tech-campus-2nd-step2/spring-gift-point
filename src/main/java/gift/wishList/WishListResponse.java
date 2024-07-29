package gift.wishList;
public class WishListResponse {
    long productID;
    long optionID;

    long count;

    public WishListResponse() {
    }

    public WishListResponse(long productID, long optionID, long count) {
        this.productID = productID;
        this.optionID = optionID;
        this.count = count;
    }

    public WishListResponse(WishList wishList) {
        this.productID = wishList.getProduct().getId();
        this.optionID = wishList.getOption().getId();
        this.count = wishList.getCount();
    }

    public long getProductID() {
        return productID;
    }

    public long getOptionID() {
        return optionID;
    }

    public long getCount() {
        return count;
    }

    public void setProductID(long productID) {
        this.productID = productID;
    }

    public void setOptionID(long optionID) {
        this.optionID = optionID;
    }

    public void setCount(long count) {
        this.count = count;
    }
}
