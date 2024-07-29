package testFixtures;

import gift.product.entity.Option;
import gift.product.entity.Product;

public class OptionFixture {

    public static Option createOption(String name, Product product) {
        return new Option(name, 10, product);
    }

}
