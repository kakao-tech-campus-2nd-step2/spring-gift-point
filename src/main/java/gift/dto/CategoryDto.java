package gift.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private Long id;
    private String name;
    private List<String> productName = new ArrayList<>();

    public CategoryDto() {
    }

    public CategoryDto(Long id, String name, List<String> productName) {
        this.id = id;
        this.name = name;
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public CategoryDto setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public CategoryDto setName(String name) {
        this.name = name;
        return this;
    }

    public List<String> getProductName() {
        return productName;
    }

    public CategoryDto setProductName(List<String> productName) {
        this.productName = productName;
        return this;
    }
}
