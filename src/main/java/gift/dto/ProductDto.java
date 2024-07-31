package gift.dto;

import java.util.List;

public class ProductDto {
    private Long id;
    private String name;
    private int price;
    private String imgUrl;
    private Long categoryId;
    private List<OptionDto> options;

    public ProductDto() {
    }

    public ProductDto(Long id, String name, int price, String imgUrl, Long categoryId, List<OptionDto> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.categoryId = categoryId;
        this.options = options;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public Long getCategoryId() { return categoryId; }

    public List<OptionDto> getOptions() {
        return options;
    }
}