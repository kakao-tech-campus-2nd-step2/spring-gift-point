package gift.application.product.dto;

import gift.model.product.Option;
import gift.model.product.Product;

public class OptionModel {

    public record Info(
        Long id,
        String name,
        Integer quantity
    ) {

        public static Info from(Option option) {
            return new Info(option.getId(), option.getName(), option.getQuantity());
        }
    }

    public record PurchaseInfo(
        Long productId,
        Long optionId,
        Integer quantity,
        Integer totalPrice
    ) {

        public static PurchaseInfo from(Option option, Product product, Integer quantity) {
            return new PurchaseInfo(product.getId(), option.getId(), quantity,
                quantity * product.getPrice());
        }
    }


}
