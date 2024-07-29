package gift.product;

import gift.category.CategoryFixture;
import gift.category.entity.Category;
import gift.option.OptionFixture;
import gift.option.dto.OptionReqDto;
import gift.product.dto.ProductReqDto;
import gift.product.entity.Product;
import java.util.List;

public class ProductFixture {

    public static Product createProduct() {
        return new Product("name", 1000, "imageUrl", null);
    }

    public static Product createProduct(ProductReqDto productReqDto) {
        Product product = productReqDto.toEntity();
        productReqDto.options().forEach(optionReqDto -> product.addOption(
                OptionFixture.createOption(optionReqDto.name(), optionReqDto.quantity())
        ));
        product.changeCategory(CategoryFixture.createCategory(productReqDto.category()));
        return product;
    }

    public static Product createProduct(String name, Integer price, String imageUrl) {
        return new Product(name, price, imageUrl, null);
    }

    public static Product createProduct(String name, Integer price, String imageUrl, Category category) {
        return new Product(name, price, imageUrl, category);
    }

    public static ProductReqDto createProductReqDto() {
        return new ProductReqDto("name", 1000, "imageUrl", "category", List.of());
    }

    public static ProductReqDto createProductReqDto(List<OptionReqDto> options) {
        return new ProductReqDto("name", 1000, "imageUrl", "category", options);
    }

    public static ProductReqDto createProductReqDto(String name, List<OptionReqDto> options) {
        return new ProductReqDto(name, 1000, "imageUrl", "category", options);
    }

    public static ProductReqDto createProductReqDto(String name, Integer price, String imageUrl, String category, List<OptionReqDto> options) {
        return new ProductReqDto(name, price, imageUrl, category, options);
    }
}
