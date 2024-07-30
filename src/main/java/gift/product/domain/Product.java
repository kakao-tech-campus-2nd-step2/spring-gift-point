package gift.product.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "product")
public class Product {

    @Id
    @Column(name = "product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double price;
    private String imageUrl;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishListProduct> wishListProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany
    @JoinColumn(name = "product_id")
    private List<ProductOption> productOptions = new ArrayList<>();


    public Product() {
    }

    public Product(CreateProductRequestDTO createProductRequestDTO, Category category) {
        this.name = createProductRequestDTO.getName();
        this.price = createProductRequestDTO.getPrice();
        this.imageUrl = createProductRequestDTO.getImageUrl();
        this.category = category;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void addProductOption(CreateProductOptionRequestDTO createProductOptionRequestDTO) {
        ProductOption productOption = new ProductOption(createProductOptionRequestDTO.getName(), createProductOptionRequestDTO.getQuantity(), this);
        productOptions.add(productOption);
    }


    public void addProductOption(ProductOption productOption) {
        productOptions.add(productOption);
    }
}
