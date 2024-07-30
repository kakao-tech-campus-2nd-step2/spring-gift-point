package gift.Product;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.product.Product;
import gift.global.exception.BusinessException;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ActiveProfiles("test")
public class ProductTest {

    @Test
    void 카카오_문구_포함하는_경우() {
        // given
        Category category = new Category(1L, "카테고리1", "카테고리1 입니다.");

        // when, then
        assertThatExceptionOfType(BusinessException.class)
            .isThrownBy(() -> new Product("카카오이름", category, 1000, "description","https://example.com/image.jpg"))
            .withMessageContaining("'카카오' 문구를 포함할 수 없습니다. 담당 MD와 협의하세요.");
    }

    @Test
    void 카카오_문구_포함하지_않는_경우() {
        // given
        Category category = new Category(1L, "카테고리1", "카테고리1 입니다.");

        // when, then
        assertThatCode(() -> {
            new Product("일반이름", category, 1000, "description","https://example.com/image.jpg");
        }).doesNotThrowAnyException();
    }

}
