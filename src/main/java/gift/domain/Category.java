package gift.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="name", nullable = false, unique = true)
    private String name;
    @Column(name="color", nullable = false)
    private String color;
    @Column(name="image_url", nullable = false)
    private String imageUrl;
    @Column(name="description", nullable = false)
    private String description;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();
    protected Category (){
    }

    public void addProduct(Product product){
        this.products.add(product);
        product.setCategory(this);
    }

    public void removeProduct(Product product){
        product.setCategory(null);
        this.products.remove(product);
    }

    public void removeProducts(){
        Iterator<Product> iterator = products.iterator();

        while(iterator.hasNext()){
            Product product = iterator.next();
            product.setCategory(null);
            iterator.remove();
        }
    }

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(Builder builder){
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
    }

    public static class Builder{
        private Long id;
        private String name;
        private String color;
        private String imageUrl;
        private String description;

        public Builder id(Long id){
            this.id = id;
            return this;
        }

        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder color(String color){
            this.color = color;
            return this;
        }

        public Builder imageUrl(String imageUrl){
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder description(String description){
            this.description = description;
            return this;
        }
        public Category build(){
            return new Category(this);
        }
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public  void updateCategory(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
