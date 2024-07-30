package gift.service.option;

import gift.dto.option.OptionRequest;
import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.product.Product;
import gift.repository.product.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;


    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category(10L, "test", "test", "test", "test");
        Option option1 = new Option("testOption", 10);
        List<Option> options = List.of(option1);

        product = new Product("Test Gift", 100, "test.jpg", category, options);
    }

    @Test
    @DisplayName("중복값을 넣었을때 에러가 잘 뜨는지 테스트")
    void testAddOptionWithDuplicateName() {
        // given
        OptionRequest.Create duplicateOptionRequest = new OptionRequest.Create("testOption", 10);
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // when & then
        assertThrows(IllegalArgumentException.class, () -> {
            optionService.addOptionToGift(product.getId(), duplicateOptionRequest);
        });
    }


}