package gift.controller.product.dto;

import gift.application.product.dto.OptionModel;
import gift.application.product.dto.ProductModel;
import java.util.List;

public class ProductResponse {

    public record Info(
        Long productId,
        String name,
        Integer price,
        String imageUrl,
        String category,
        OptionResponse.InfoList optionInfos
    ) {

        public static Info from(
            ProductModel.Info productModel,
            List<OptionModel.Info> optionModels
        ) {
            var optionInfos = OptionResponse.InfoList.from(optionModels);
            return new Info(
                productModel.id(),
                productModel.name(),
                productModel.price(),
                productModel.imageUrl(),
                productModel.category(),
                optionInfos
            );
        }
    }

    public record Summary(
        Long productId,
        String name,
        Integer price,
        String category,
        Integer optionCount,
        List<String> optionNames
    ) {

        public static Summary from(ProductModel.InfoWithOption productModel) {
            var optionNames = productModel.optionInfos().stream()
                .map(OptionModel.Info::name)
                .toList();
            return new Summary(
                productModel.id(),
                productModel.name(),
                productModel.price(),
                productModel.category(),
                productModel.optionInfos().size(),
                optionNames
            );
        }
    }
}
