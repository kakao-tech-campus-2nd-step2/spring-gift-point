package gift.dto;


import java.time.LocalDateTime;

public class WishListDTO {

    private Long id;
    private UserDTO user;
    private ProductDTO product;
    private LocalDateTime createdDate;

    public WishListDTO() {}

    public WishListDTO(Long id, UserDTO user, ProductDTO product, LocalDateTime createdDate) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}