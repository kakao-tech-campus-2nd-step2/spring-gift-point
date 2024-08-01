package gift.Model;

public class WishlistDto {
    private long wishlistId;
    private long memberId;
    private long productId;
    private int quantity;
    private String productName;
    private int price;
    private long optionId;

    public WishlistDto() {
    }

    public WishlistDto(long wishlistId, long memberId, long productId, int quantity, String productName, int price, long optionId) {
        this.wishlistId = wishlistId;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.optionId = optionId;
    }

    public long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(long wishlistId) {
        this.wishlistId = wishlistId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }
}
