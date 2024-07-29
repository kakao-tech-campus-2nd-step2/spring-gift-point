package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.Model.Category;
import gift.Repository.CategoryRepository;
import gift.Service.CategoryService;

import gift.Service.ProductService;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @DirtiesContext
    @Test
    void getAllCategories() {
        // 기존 데이터 14개 존재
        List<Category> actual = categoryService.getAllCategory();

        assertAll(
            () -> assertThat(actual).isNotNull(),
            () -> assertThat(actual.size()).isEqualTo(14)// 기존 데이터 14개 존재
        );

    }
    @DirtiesContext
    @Test
    void addCategory(){
        Category expect = new Category(null, "testCategory1", "testColor1", "testUrl1",
            "testDescription1", new ArrayList<>());// 1~14번까지 기존 데이터가 존재하므로 20으로 test객체 생성
        categoryService.addCategory(expect);
        //1~14번 까지 있으므로 15번에 생성됨 => (long)categoryService.getAllCategory().size()으로 15번째 아이디 갖고옴
        Category actual = categoryService.getCategoryById((long)categoryService.getAllCategory().size());
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expect.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expect.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(expect.getColor()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expect.getImageUrl()),
            () -> assertThat(actual.getDescription()).isEqualTo(expect.getDescription())
        );
    }
    @DirtiesContext
    @Test
    void updateCategory(){

        Category expect = new Category(1L, "testCategory2", "testColor2", "testUrl2",
            "testDescription2", new ArrayList<>());
        categoryService.updateCategory(expect);
        Category actual = categoryService.getCategoryById(expect.getId());
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expect.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expect.getName()),
            () -> assertThat(actual.getColor()).isEqualTo(expect.getColor()),
            () -> assertThat(actual.getImageUrl()).isEqualTo(expect.getImageUrl()),
            () -> assertThat(actual.getDescription()).isEqualTo(expect.getDescription())
        );
    }

    @DirtiesContext
    @Test
    void deleteCategory(){
        categoryService.deleteCategory(1L);
        assertAll(
            () -> assertThat(categoryService.getCategoryById(1L)).isNull()
        );
    }
}
