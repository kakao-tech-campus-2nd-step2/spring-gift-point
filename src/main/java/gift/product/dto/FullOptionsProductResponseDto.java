package gift.product.dto;

import gift.category.dto.CategoryResponseDto;
import gift.option.dto.OptionResponseDto;
import gift.product.entity.Options;
import gift.product.entity.Product;
import java.util.List;

// 사용자에게 불필요한 모든 정보가 있어서 사용자에게 보여주진 않고 다른 서비스에 제공할 dto
public record FullOptionsProductResponseDto(
    long productId,
    String name,
    int price,
    String imageUrl,
    CategoryResponseDto category,
    List<OptionResponseDto> options
) {

    public static FullOptionsProductResponseDto fromProduct(Product product) {
        var category = CategoryResponseDto.fromCategory(product.getCategory());
        var options = product.getOptions().toList().stream()
            .map(OptionResponseDto::fromOption).toList();

        return new FullOptionsProductResponseDto(product.getProductId(), product.getName(),
            product.getPrice(), product.getImageUrl(), category, options);
    }

    public Product toProduct() {
        var actualOptions = options.stream().map(OptionResponseDto::toOption).toList();
        return new Product(productId, name, price, imageUrl, category.toCategory(),
            new Options(actualOptions));
    }
}
