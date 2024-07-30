package gift.util;

import gift.domain.Category;
import gift.domain.Product;

public class ProductFixture {

    public static Product createProduct(Category category) {
        return createProduct(null, "아이스 아메리카노", category);
    }

    public static Product createProduct(Long id, String name, Category category) {
        return new Product(id, name, 4500, "image", category);
    }
}
