package gift.dto;

import java.time.LocalDateTime;

public class WishDTO {

    private Long id;
    private Long productId;
    private String productName;
    private LocalDateTime createdDate;

    public WishDTO() {
    }

    public WishDTO(Long id, Long productId, String productName, LocalDateTime createdDate) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.createdDate = createdDate;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
