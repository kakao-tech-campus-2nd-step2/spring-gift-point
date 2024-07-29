package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PreRemove;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column
    private String color;
    @Column
    private String description;
    @Column
    private String image_url;
    @OneToMany(mappedBy = "category")
    private final List<Product> products = new LinkedList<>();

    public static Category defaultCategory = new Category("default", "#FFFFFF", "default category",
        "");

    public Category() {
    }

    public Category(String name, String color, String description, String image_url) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.image_url = image_url;
    }

    @PreRemove
    private void preRemove() {
        for (Product product : products) {
            product.setCategory(defaultCategory);
        }
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void updateDetails(String name, String color, String description, String image_url) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.image_url = image_url;
    }
}
