package gift.application.product.dto;

import gift.model.product.Option;
import gift.model.product.Options;
import gift.model.product.Product;
import java.util.List;

public class OptionCommand {

    public record Info(
        String name,
        Integer quantity
    ) {

        public Option toEntity(Product product) {
            return new Option(name, quantity, product);
        }
    }

    public record Update(
        String name,
        Integer quantity
    ) {

    }

    public record RegisterMany(
        List<Info> request
    ) {

        public Options toOptions(Product product) {
            Options options = new Options(request.stream()
                .map(info -> new Option(info.name(), info.quantity(), product))
                .toList());

            return options;
        }

    }

    public record Purchase(
        Long optionId,
        Integer quantity
    ) {

    }
}
