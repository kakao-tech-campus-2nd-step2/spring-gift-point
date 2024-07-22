package gift.product.presentation.dto;

import gift.product.business.dto.OptionOut;
import gift.product.business.dto.OptionOut.Info;
import java.util.List;

public class OptionResponse {

    public record Info(
        Long id,
        String name,
        Integer quantity
    ) {

        public static Info from(OptionOut.Info optionOut) {
            return new Info(
                optionOut.id(),
                optionOut.name(),
                optionOut.quantity()
            );
        }

        public static List<Info> of(List<OptionOut.Info> options) {
            return options.stream()
                .map(Info::from)
                .toList();
        }
    }

}
