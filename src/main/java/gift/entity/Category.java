package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Schema(description = "Category entity representing a product category")
@Entity
@Table(name = "categories")
public class Category {

    @OneToMany(mappedBy = "category")
    @Schema(description = "List of products in this category")
    private final List<Product> products = new ArrayList<>();
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "Unique identifier of the category", example = "1")
    private Long id;
    @NotNull
    @Schema(description = "Name of the category", example = "교환권")
    private String name;
    @NotNull
    @Schema(description = "Color associated with the category", example = "#6c95d1")
    private String color;
    @NotNull
    @Schema(description = "Image URL representing the category", example = "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png")
    private String imageUrl;
    @NotNull
    @Schema(description = "Description of the category", example = "교환권 카테고리입니다.")
    private String description;

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    protected Category() {

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public boolean emptyCategoryCheck() {
        return products.isEmpty();
    }

    public void updateCategory(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
