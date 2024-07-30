package gift.dto;

import gift.annotation.ProductName;
import gift.entity.Category;
import gift.entity.Product;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class ProductDTO {

    @ProductName
    private String name;
    private int price;
    private String imageUrl;

    private Long categoryId;

    @Valid
    @NotEmpty(message = "최소 1개의 Option을 지정해야 합니다.")
    private List<OptionDTO> option;


    public ProductDTO() {
    }


    public ProductDTO(Long id, String name, int price, String imageUrl, Long categoryId,
        List<OptionDTO> option) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.option = option;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public List<OptionDTO> getOption() {
        return option;
    }

    public Product toEntity(Category category) {
        return new Product(name, price, imageUrl, category);
    }


}
