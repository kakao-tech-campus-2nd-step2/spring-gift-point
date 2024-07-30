package gift.Category;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.category.Category;
import gift.domain.category.CategoryDTO;
import gift.domain.category.CategoryService;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    private JpaCategoryRepository categoryRepository;
    @Autowired
    private JpaProductRepository productRepository;
    private Category category1;
    private Category category2;
    private CategoryDTO categoryDTO;

    @BeforeEach
    void setUp() {
        category1 = new Category("에티오피아산", "에티오피아산 원두를 사용했습니다.");
        category2 = new Category("자메이카산", "자메이카산 원두를 사용했습니다.");
        categoryDTO = new CategoryDTO("수정", "수정");
    }

    @Test
    @Description("카테고리 추가")
    void testCreateCategory() {
        Category savedCategory = categoryRepository.saveAndFlush(category1);
        assertThat(savedCategory).isEqualTo(category1);
    }

    @Test
    @Description("카테고리 수정")
    void testUpdateCategory() {
        // given
        Category savedCategory = categoryRepository.saveAndFlush(category1);
        clear();

        // when
        Category findCategory = categoryRepository.findById(savedCategory.getId()).get();
        findCategory.update(categoryDTO.name(), categoryDTO.description());
        categoryRepository.saveAndFlush(findCategory);
        clear();

        // then
        Category modifiedCategory = categoryRepository.findById(findCategory.getId()).get();

        assertThat(modifiedCategory.getName()).isEqualTo(categoryDTO.name());
        assertThat(modifiedCategory.getDescription()).isEqualTo(categoryDTO.description());
    }

    @Test
    @Description("카테고리 삭제")
    void testCategoryDelete() {
        // given
        Category savedCategory = categoryRepository.saveAndFlush(category1);
        clear();

        // when
        categoryRepository.deleteById(savedCategory.getId());

        // then
        assertThat(categoryRepository.existsById(savedCategory.getId())).isEqualTo(false);
    }

    @Test
    @Description("카테고리를 참조하는 상품 존재하면 삭제 실패") // 상품은 그냥 삭제해도 상관없음
    void testCategoryDeleteFailed() {
        // given
        Category savedCategory = categoryRepository.saveAndFlush(category1);
        Product product = new Product("아이스 아메리카노 T", savedCategory, 4500,
            "https://example.com/image.jpg");

        // when
        productRepository.saveAndFlush(product);
        clear();

        // then
        categoryRepository.deleteById(savedCategory.getId());
        assertThrows(ConstraintViolationException.class, // 특정 데이터베이스 제약 조건 위반
            () -> flush());
    }

    @Test
    @Description("카테고리 이름 중복시 에러 발생")
    void testCategoryNameDuplicate() {
        // given
        Category savedCategory = categoryRepository.saveAndFlush(category1);

        // when
        Category same_name_category = new Category("에티오피아산", "에티오피아산 원두를 사용했습니다222.");

        // then
        assertThrows(DataIntegrityViolationException.class, // 일반적인 데이터 무결성 위반(더 넓은 범위)
            () -> categoryRepository.saveAndFlush(same_name_category));
    }

    void clear() {
        entityManager.clear();
    }

    void flush() {
        entityManager.flush();
    }
}
