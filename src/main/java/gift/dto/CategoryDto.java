package gift.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import gift.entity.Category;

public class CategoryDto {
    

    private Long id;
    private String name;
    private String color;
    private String imageUrl;
    private String description;

    public CategoryDto(){}

    @JsonCreator
    public CategoryDto(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("color") String color,
        @JsonProperty("image_url") String imageUrl,
        @JsonProperty("description") String description
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
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
    
    public static CategoryDto fromEntity(Category category){
        return new CategoryDto(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }
    
}
