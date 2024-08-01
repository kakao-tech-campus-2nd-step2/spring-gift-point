package gift.mapper;

import gift.domain.category.Category;
import gift.domain.product.Product;
import gift.web.dto.OptionDto;
import gift.web.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final CategoryMapper categoryMapper;
    private final OptionMappper optionMappper;

    public ProductMapper(CategoryMapper categoryMapper, OptionMappper optionMappper) {
        this.categoryMapper = categoryMapper;
        this.optionMappper = optionMappper;
    }

    public ProductDto toDto(Product product) {
        return new ProductDto(product.getId(),
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

    public Product toEntity(ProductDto productDto, Category category) {
        return new Product(productDto.name(),
            productDto.price(),
            productDto.imageUrl(),
            category
        );
    }
}
