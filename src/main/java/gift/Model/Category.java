package gift.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.beans.ConstructorProperties;

import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 ID(자동으로 설정)", defaultValue = "1")
    private Long id;
    @NotBlank
    @Column(name = "name", nullable = false)
    @Schema(description = "카테고리 이름", defaultValue = "카테고리 이름")
    private String name;
    @Column(name = "color", nullable = false)
    @Schema(description = "카테고리 색상", defaultValue = "카테고리 색상")
    private String color;
    @Column(name = "imageUrl", nullable = false)
    @Schema(description = "카테고리 이미지URL", defaultValue = "카테고리 이미지URL")
    private String imageUrl;
    @Column(name = "description", nullable = false)
    @Schema(description = "카테고리에 대한 설명", defaultValue = "카테고리 설명")
    private String description;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Product> products;

    protected Category(){

    }

    @ConstructorProperties({"id","name","color","imageUrl","description","products"})
    public Category(Long id, String name, String color, String imageUrl, String description, List<Product> products) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
        this.products = products;
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

}
