package gift.DTO;

import gift.Model.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import java.beans.ConstructorProperties;
import java.util.List;

public class CategoryDTO {
    private Long id;
    @NotBlank
    @Schema(description = "카테고리 이름", defaultValue = "카테고리 이름")
    private String name;
    @NotBlank
    @Schema(description = "카테고리 색상", defaultValue = "카테고리 색상")
    private String color;
    @NotBlank
    @Schema(description = "카테고리 이미지URL", defaultValue = "카테고리 이미지URL")
    private String imageUrl;
    @NotBlank
    @Schema(description = "카테고리에 대한 설명", defaultValue = "카테고리 설명")
    private String description;

    private List<Product> products;



    @ConstructorProperties({"id","name","color","imageUrl","description","products"})
    public CategoryDTO(Long id, String name, String color, String imageUrl, String description, List<Product> products) {
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

    public List<Product> getProducts() {
        return products;
    }
}
