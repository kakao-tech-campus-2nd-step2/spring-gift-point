package gift.DTO;

public class WishDTO {

    private Long id;
    private Long userId;
    private Long productId;
    private String productName;
    private String name;
    private int price;
    private String imageUrl;

    public WishDTO(Long id, Long userId, Long productId, String name, int price, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public WishDTO(Long id, Long userId, Long productId, String productName) {
        this.id = id;
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

}
