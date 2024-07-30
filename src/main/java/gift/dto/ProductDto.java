package gift.dto;

import java.util.ArrayList;
import java.util.List;

public class ProductDto {

    private final Long id;
    private final String name;
    private final double price;
    private final String imageUrl;
    private final CategoryDto categoryDto;
    private List<OptionDto> optionDtos = new ArrayList<>();

    public ProductDto(Long id, String name, double price, String imageUrl, CategoryDto categoryDto,
        List<OptionDto> optionDtos) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryDto = categoryDto;
        this.optionDtos = optionDtos;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
