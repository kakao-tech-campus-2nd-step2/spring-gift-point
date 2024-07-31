package gift.model.product;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @ManyToOne
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 15, columnDefinition = "VARCHAR(15) COMMENT '상품명'")
    @Embedded
    private ProductName name;

    @Column(nullable = false, columnDefinition = "integer COMMENT '상품 가격'")
    private int price;

    @Column(nullable = false , columnDefinition = "VARCHAR(255) COMMENT '상품 이미지 주소'")
    private String imageUrl;

    protected Product(){
    }

    public Product(Category category, ProductName name, int price, String imageUrl){
        this.category = category;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public void updateProduct(Product product){
        this.category = product.getCategory();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
    }

    public Category getCategory() {
        return category;
    }

    public long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
