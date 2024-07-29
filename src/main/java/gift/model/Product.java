package gift.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "name", columnDefinition = "varchar(15) not null")
    private String name;

    @Column(name = "price", columnDefinition = "integer not null")
    private Integer price;

    @Column(name = "image_url", columnDefinition = "varchar(255) not null")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "category_id"), nullable = false)
    private Category category;

    public Product(long id, String name, int price, String imageUrl, Category category) {
        this.id = id;
        this.updateName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.updateName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    protected Product() {
    }

    public Product update(String name, Integer price, String imageUrl, Category category){
        if(imageUrl != null && !name.isEmpty()){
            this.updateName(name);
        }
        if(price != null){
            this.price = price;
        }
        if(imageUrl != null && !imageUrl.isEmpty()){
            this.imageUrl = imageUrl;
        }
        if(!category.getName().equals("NONE")){
            this.category = category;
        }
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getPrice() {
        return price;
    }

    public String getName(){
        return this.name;
    }

    public Category getCategory() {
        return category;
    }

    public long getId() {
        return id;
    }

    public void updateName(String name) {
        ProductName productName = new ProductName(name);
        this.name = productName.getName();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, imageUrl);
    }
}
