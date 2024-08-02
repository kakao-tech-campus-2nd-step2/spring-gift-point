package gift.entity;

import gift.exception.ProductNoConferredException;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;


import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private int price;

    @NotNull
    @Pattern(
            regexp = "^(http|https)://.*$",
            message = "Invalid image URL format"
    )
    private String imageUrl;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @NotEmpty(message = "Product must have at least one option")
    private Set<Option> options = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;


    public Product(){
    }


    public Product(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Set<Option> getOptions() { return options; }

    public void setOptions(Set<Option> options) {

        this.options = options;
    }

    public void addOption(Option option) {

        options.add(option);
        option.setProduct(this);
    }

    public void removeOption(Option option) {
        options.remove(option);
        option.setProduct(null);
    }

    public void edit(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    private void checkName(String name) {
        if (name.contains("카카오")) {
            throw new ProductNoConferredException(List.of("카카오"));
        }
    }

}
