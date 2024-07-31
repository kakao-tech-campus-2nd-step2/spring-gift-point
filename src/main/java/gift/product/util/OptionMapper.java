package gift.product.util;

import gift.product.dto.OptionRequest;
import gift.product.dto.OptionResponse;
import gift.product.dto.WishOptionResponse;
import gift.product.entity.Option;
import gift.product.entity.Product;

public class OptionMapper {

    public static Option toEntity(OptionRequest request, Product product) {
        return new Option(
                request.name(),
                request.quantity(),
                product
        );
    }

    public static OptionResponse toResponseDto(Option option) {
        return new OptionResponse(
                option.getId(),
                option.getName(),
                option.getQuantity()
        );
    }

    public static WishOptionResponse toWishResponseDto(Option option) {
        return new WishOptionResponse(
                option.getId(),
                option.getName(),
                option.getQuantity(),
                ProductMapper.toGetResponseDto(option.getProduct())
        );
    }

}
