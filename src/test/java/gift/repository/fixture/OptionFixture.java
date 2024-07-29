package gift.repository.fixture;

import gift.domain.Option;
import gift.domain.Product;

public class OptionFixture {
    public static Option createOption(String name,int quantity){
        return new Option(name,quantity);
    }
}
