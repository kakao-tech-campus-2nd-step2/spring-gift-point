package gift.product.business.dto;

import gift.product.persistence.entity.Option;
import java.util.List;

public class OptionOut {

    public record Info(
        Long id,
        String name,
        Integer quantity
    ) {

        public static Info from(Option option) {
            return new Info(
                option.getId(),
                option.getName(),
                option.getQuantity()
            );
        }

        public static List<Info> of(List<Option> options) {
            return options.stream()
                .map(Info::from)
                .toList();
        }
    }

}
