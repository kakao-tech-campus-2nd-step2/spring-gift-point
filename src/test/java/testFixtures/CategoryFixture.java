package testFixtures;

import gift.product.entity.Category;

public class CategoryFixture {

    public static Category createCategory(String name) {
        return new Category.CategoryBuilder()
                .setName(name)
                .setColor("#ffffff")
                .setImageUrl("https://product-category/image.jpg")
                .setDescription("")
                .build();
    }

}
