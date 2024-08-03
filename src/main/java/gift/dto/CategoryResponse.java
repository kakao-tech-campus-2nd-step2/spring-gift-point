package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Category;

@JsonPropertyOrder({"id", "name", "color", "image_url", "description"})
public class CategoryResponse {

    private Long id;
    private String name;
    private String color;

    @JsonProperty("image_url")
    private String imgUrl;
    private String description;

    public CategoryResponse() {
    }

    public CategoryResponse(Long id, String name, String color, String imgUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imgUrl = imgUrl;
        this.description = description;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDescription() {
        return description;
    }

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getImgUrl(), category.getDescription());
    }
}
