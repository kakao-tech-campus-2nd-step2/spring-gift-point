package gift.dto.product;

import gift.dto.category.CategoryDTO;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;

import java.util.List;

public class ProductResponseWithDetailsDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private CategoryDTO category;
    private List<Option> options;

    public ProductResponseWithDetailsDto(Product product, Category category, List<Option> options) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category = new CategoryDTO(category);
        this.options = options;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
