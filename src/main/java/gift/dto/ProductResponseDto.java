package gift.dto;

import gift.domain.product.Product;

import java.util.List;

public class ProductResponseDto {
    private final long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final String category;
    private final List<OptionResponseDto> options;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.category = product.getCategory().getName();
        this.options = product.getOptions().stream().map(OptionResponseDto::new).toList();
    }

    public long getId() {
        return id;
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

    public List<OptionResponseDto> getOptions() {
        return options;
    }
}
