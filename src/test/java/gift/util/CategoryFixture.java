package gift.util;

import gift.category.entity.Category;

public class CategoryFixture {

    public static Category createCategory() {
        return createCategory(null, "test");
    }

    public static Category createCategory(Long id, String name) {
        return new Category(id, name, "testImageUrl", "test");
    }
}