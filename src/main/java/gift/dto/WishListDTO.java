package gift.dto;


public class WishListDTO {

    private Long id;
    private UserDTO user;
    private ProductDTO product;

    public WishListDTO() {}

    public WishListDTO(Long id, UserDTO user, ProductDTO product) {
        this.id = id;
        this.user = user;
        this.product = product;
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
}