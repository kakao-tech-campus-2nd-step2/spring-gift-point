package gift.category.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.ToString;

@ToString
public class CategoryRequest {

    private Long id;
    private String name;
    private List<String> productName = new ArrayList<>();
    private String color;
    private String imageUrl;
    private String description;

    public CategoryRequest() {
    }

    public CategoryRequest(String name, List<String> productName) {
        this(null, name, productName, null, null, null);
    }

    public CategoryRequest(Long id, String name, List<String> productName) {
        this(id, name, productName, null, null, null);
    }

    public CategoryRequest(String name, List<String> productName, String color, String imageUrl,
        String description) {
        this(null, name, productName, color, imageUrl, description);
    }

    public CategoryRequest(Long id, String name, List<String> productName, String color,
        String imageUrl,
        String description) {
        this.id = id;
        this.name = name;
        this.productName = productName;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public CategoryRequest setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getProductName() {
        return productName;
    }

    public CategoryRequest setProductName(List<String> productName) {
        this.productName = productName;
        return this;
    }

    public Long getId() {
        return id;
    }

    public CategoryRequest setId(Long id) {
        this.id = id;
        return this;
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
