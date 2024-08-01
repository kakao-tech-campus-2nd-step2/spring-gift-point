package gift.dto;

import gift.model.Category;
import gift.model.Product;
import java.util.List;
import java.util.stream.Collectors;

public class ProductCreateRequestDTO {
    private String name;
    private int price;
    private String imageUrl;
    private String category;
    private List<OptionSimpleRequestDTO> options;

    public ProductCreateRequestDTO(String name, int price, String imageUrl, String category,
        List<OptionSimpleRequestDTO> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public List<OptionSimpleRequestDTO> getOptions() {
        return options;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setOptions(List<OptionSimpleRequestDTO> options) {
        this.options = options;
    }

}
