package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "카테고리 이름을 입력해주세요")
    private String name;

    @NotBlank(message = "카테고리 설명을 입력해주세요")
    private String description;

    @NotBlank(message = "카테고리 색상을 입력해주세요")
    private String color;

    @NotBlank(message = "카테고리 이미지 URL을 입력해주세요")
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    protected Category() {
    }

    public Category(String name, String description, String color, String imageUrl) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
