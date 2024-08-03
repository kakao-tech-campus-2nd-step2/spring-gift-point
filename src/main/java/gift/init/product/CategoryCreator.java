package gift.init.product;

import gift.domain.product.Category.CreateCategory;
import gift.service.product.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryCreator {

    private final CategoryService categoryService;

    @Autowired
    public CategoryCreator(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public void creator() {
        categoryService.createCategory(new CreateCategory("Test1",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F20240214_EWQEQ.png",
            "졸업을 축하하는 축하 리스트",
            "Test1",
            "#B67352"));
        categoryService.createCategory(new CreateCategory("Test2",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Fst.kakaocdn.net%2Fproduct%2Fgift%2Fproduct%2F20240213090444_1b3dc970aec54eabbf3fbb7d35c0b7af.jpg",
            "졸업을 축하하는 축하 리스트",
            "Test2",
            "#B99352"));
        categoryService.createCategory(new CreateCategory("Test3",
            "https://img1.daumcdn.net/thumb/S104x104/?fname=https%3A%2F%2Ft1.daumcdn.net%2Fgift%2Fhome%2Ftheme%2F292620240221_MLFJR.jpeg",
            "졸업을 축하하는 축하 리스트",
            "Test3",
            "#B67344"));
    }
}
