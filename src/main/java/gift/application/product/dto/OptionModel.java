package gift.application.product.dto;

import gift.model.product.Option;

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


}
