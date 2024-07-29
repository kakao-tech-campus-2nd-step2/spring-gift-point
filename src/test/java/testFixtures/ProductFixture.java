package testFixtures;

import gift.product.entity.Category;
import gift.product.entity.Product;

public class ProductFixture {

    public static Product createProduct(String name, Category category) {
        return new Product.ProductBuilder()
                .setName(name)
                .setPrice(1000)
                .setImageUrl("https://shop.io/image.jpg")
                .setCategory(category)
                .build();
    }

}
