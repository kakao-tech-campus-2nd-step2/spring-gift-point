package gift.dto;

import gift.model.Product;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDTO {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private CategoryDTO category;
    private Set<OptionDTO> options;

    public ProductDTO() {}

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        if (product.getCategory() != null) {
            this.category = new CategoryDTO(product.getCategory());
        }
        if (product.getOptions() != null) {
            this.options = product.getOptions().stream().map(OptionDTO::new).collect(Collectors.toSet());
        }
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

    public Set<OptionDTO> getOptions() {
        return options;
    }

    public void setOptions(Set<OptionDTO> options) {
        this.options = options;
    }
}