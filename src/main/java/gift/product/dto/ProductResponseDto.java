package gift.product.dto;

import gift.category.dto.CategoryResponseDto;
import gift.option.entity.Option;
import gift.product.entity.Options;
import gift.product.entity.Product;
import java.util.List;

public record ProductResponseDto(
    long productId,
    String name,
    int price,
    String imageUrl,
    CategoryResponseDto category,
    List<OptionExceptProductResponseDto> options
) {

    public static ProductResponseDto fromProduct(Product product) {
        var categoryResponseDto = CategoryResponseDto.fromCategory(product.getCategory());
        var optionResponseDtoList = product.getOptions().toList().stream()
            .map(OptionExceptProductResponseDto::fromOption).toList();

        return new ProductResponseDto(product.getProductId(), product.getName(), product.getPrice(),
            product.getImageUrl(), categoryResponseDto, optionResponseDtoList);
    }

    // 이렇게 억지로 만들다보니 차라리 Service에서 Entity를 반환하는 것이 맞다는 생각이 들었습니다.
    // 어디서 Service와 Controller가 통신할 때 DTO를 사용해야 한다는 글을 읽었었는데, service가 service와 통신할 때도 그렇게 하면 유지보수 비용이 너무 많이 들게 되는 것 같습니다.
    public Product toProduct() {
        return new Product(productId, name, price, imageUrl, category.toCategory(), new Options(
            options.stream().map(
                option -> new Option(option.optionId(), option.name(), option.quantity(), productId)
            ).toList())
        );
    }
}
