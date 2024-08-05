package gift.repository.fixture;

import gift.domain.Category;

public class CategoryFixture {

    public static Category createCategory(String name,String color, String description,String imageUrl) {
        return new Category(name,color,description,imageUrl);
    }
}
