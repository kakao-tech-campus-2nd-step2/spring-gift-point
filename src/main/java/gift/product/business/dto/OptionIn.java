package gift.product.business.dto;

import gift.product.persistence.entity.Option;

public class OptionIn {

    public record Create(
        String name,
        Integer quantity
    ) {

        public Option toOption() {
            return new Option(name(), quantity());
        }
    }

    public record Update(
        Long id,
        String name,
        Integer quantity
    ) {

        public Option toOption() {
            return new Option(name, quantity);
        }
    }

    public record Subtract(
        Long id,
        Integer quantity
    ) {
    }

}
