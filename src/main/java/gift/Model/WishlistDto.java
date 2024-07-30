package gift.Model;

public class WishlistDto {
    private long userId;
    private long productId;
    private int count; // 담은 개수
    private int quantity;//뺄 개수
    private String productName;
    private int price;
    private long optionId;

    public WishlistDto() {
    }

    public WishlistDto(long userId, long productId, int count, int quantity, String productName, int price, long optionId) {
        this.userId = userId;
        this.productId = productId;
        this.count = count;
        this.quantity = quantity;
        this.productName = productName;
        this.price = price;
        this.optionId = optionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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
