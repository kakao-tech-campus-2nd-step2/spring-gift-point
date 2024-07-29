package gift.application.product.dto;

import gift.model.product.Product;
import java.util.List;

public class ProductModel {

    public record Info(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        String category
    ) {

        public static Info from(Product product) {
            return new Info(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), product.getCategory().getName());
        }
    }

    /**
     * N+1 문제를 해결하기 위해 옵션 정보를 추가한 InfoWithOption 클래스
     */
    public record InfoWithOption(
        Long id,
        String name,
        Integer price,
        String imageUrl,
        String category,
        List<OptionModel.Info> optionInfos
    ) {

        public static InfoWithOption from(Product product) {
            var optionInfos = product.getOptions().stream()
                .map(OptionModel.Info::from)
                .toList();
            return new InfoWithOption(product.getId(), product.getName(), product.getPrice(),
                product.getImageUrl(), product.getCategory().getName(), optionInfos);
        }
    }

}
