package gift.util;

import gift.domain.Option;
import gift.domain.Product;

public class OptionFixture {

    public static Option createOption(Product product) {
        return createOption(null, "test", 1, product);
    }

    public static Option createOption(Long id, String name, int quantity, Product product) {
        return new Option(id, name, quantity, product);
    }
}
