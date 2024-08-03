package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import gift.entity.Category;

@JsonPropertyOrder({"name", "color", "image_url", "description"})
public class CategoryRequest {

    private String name;
    private String color;

    @JsonProperty("image_url")
    private String imgUrl;
    private String description;

    public CategoryRequest() {
    }

    public CategoryRequest(String name, String color, String imgUrl, String description) {
        this.name = name;
        this.color = color;
        this.imgUrl = imgUrl;
        this.description = description;
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

    public static Category toEntity(CategoryRequest categoryRequest) {
        return new Category(categoryRequest.getName(), categoryRequest.getColor(),
            categoryRequest.getImgUrl(), categoryRequest.getDescription());
    }
}
