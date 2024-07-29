package gift.option.service;

import static org.assertj.core.api.Assertions.*;

import gift.category.domain.Category;
import gift.category.repository.CategoryRepository;
import gift.option.domain.Option;
import gift.option.domain.OptionDTO;
import gift.product.domain.Product;
import gift.product.repository.ProductRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class OptionServiceTest {

    @Autowired
    OptionService optionService;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    @Transactional
    public void setup(){
        Category category1 = new Category("category1");
        categoryRepository.save(category1);

        Product product1 = new Product(1L, "product1", 5000L, "www.naver.com");
        product1.setCategory(categoryRepository.findById(1L).get());
        productRepository.save(product1);
    }
    @Test
    @DisplayName("중복이름")
    public void 중복이름테스트(){
        //given
        optionService.save(new OptionDTO("option1", 5L, 1L));
        //when
        OptionDTO actual = new OptionDTO("option1", 5L, 1L);

        //then
        assertThatThrownBy(() -> optionService.save(actual))
            .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("이름 50자")
    public void 이름_50자_최대(){
        //given
        String longName = "option".repeat(50);
        //when
        OptionDTO actual = new OptionDTO(longName, 3L, 1L);
        //then
        assertThatThrownBy(() -> optionService.save(actual))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("이름은 최대 50글자입니다.");
    }

    @Test
    @DisplayName("특수문자")
    public void 허용되지않은_특수문자(){
        //given
        String unacceptableName = "<>?/";
        //when
        OptionDTO actual = new OptionDTO(unacceptableName, 3L, 1L);
        //then
        assertThatThrownBy(() -> optionService.save(actual))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("특수문자는 (),[],+,-,&,/,_만 허용됩니다.");
    }

    @Test
    @DisplayName("갯수 0개미만")
    public void 갯수가_0개_미만일때() {
        //given
        Long illegalQuantity = -1L;
        //when
        OptionDTO actual = new OptionDTO("option1", illegalQuantity, 1L);
        //then
        assertThatThrownBy(() -> optionService.save(actual))
            .isInstanceOf(ConstraintViolationException.class)
            .hasMessageContaining("quantity는 0이상이어야 합니다.");
    }
}
