package gift.model.product;

import gift.dto.categoryDto.CategoryDto;
import jakarta.persistence.*;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(255) COMMENT '카테고리명'")
    private String categoryName;

    @Column(nullable = false, columnDefinition = "VARCHAR(7) COMMENT '제품 색상'")
    private String color;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) COMMENT '카테고리명'")
    private String imageUrl;

    @Column(columnDefinition = "VARCHAR(255) COMMENT '카테고리명'")
    private String description;

    @Version
    private Long version;

    protected Category(){
    }

    public Category(String categoryName, String color, String imageUrl, String description){
        this.categoryName = categoryName;
        this.color  = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public void updateCategory(CategoryDto categoryDto){
        this.categoryName = categoryDto.getName();
        this.color = categoryDto.getColor();
        this.imageUrl = categoryDto.getImageUrl();
        this.description = categoryDto.getDescription();
    }

    public long getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
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
