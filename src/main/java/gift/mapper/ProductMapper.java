package gift.mapper;

import gift.domain.category.Category;
import gift.domain.product.Product;
import gift.web.dto.product.ProductRequestDto;
import gift.web.dto.product.ProductResponseDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final CategoryMapper categoryMapper;
    private final OptionMappper optionMappper;

    public ProductMapper(CategoryMapper categoryMapper, OptionMappper optionMappper) {
        this.categoryMapper = categoryMapper;
        this.optionMappper = optionMappper;
    }

    public ProductResponseDto toDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            product.getOptionList()
                .stream()
                .map(optionMappper::toDto)
                .toList()
        );
    }

    public Product toEntity(ProductRequestDto productRequestDto, Category category) {
        return new Product(productRequestDto.name(),
            productRequestDto.price(),
            productRequestDto.imageUrl(),
            category
        );
    }
}
