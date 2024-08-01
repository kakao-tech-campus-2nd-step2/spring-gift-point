package gift.admin.dto;

public class LeafProductDTO {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;
    private String giftOptionName;
    private Integer giftOptionQuantity;

    public LeafProductDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getGiftOptionName() {
        return giftOptionName;
    }

    public void setGiftOptionName(String giftOptionName) {
        this.giftOptionName = giftOptionName;
    }

    public Integer getGiftOptionQuantity() {
        return giftOptionQuantity;
    }

    public void setGiftOptionQuantity(Integer giftOptionQuantity) {
        this.giftOptionQuantity = giftOptionQuantity;
    }
}
