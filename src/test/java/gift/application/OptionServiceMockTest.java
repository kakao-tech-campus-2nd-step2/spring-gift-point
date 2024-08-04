package gift.application;

import gift.application.product.dto.OptionCommand;
import gift.application.product.dto.OptionModel;
import gift.application.product.service.OptionService;
import gift.model.product.Category;
import gift.model.product.Option;
import gift.model.product.Options;
import gift.model.product.Product;
import gift.repository.product.OptionRepository;
import gift.repository.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
public class OptionServiceMockTest {

    @InjectMocks
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    private Product product;
    private Option option;
    private Options options;
    private OptionCommand.RegisterMany registerMany;
    private OptionCommand.RegisterMany duplicateRegisterMany;

    @BeforeEach
    void setUp() {
        product = createProduct();
        option = createOption();
        options = new Options(List.of(option));
        registerMany = createRegisterMany(List.of("testOption1", "testOption"));
        duplicateRegisterMany = createRegisterMany(List.of("testOption", "testOption"));
    }

    @Test
    @DisplayName("옵션_생성_성공")
    void 옵션_생성_성공() {
        options = new Options(List.of(createOption("testOption2")));
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(optionRepository.findAllByProductId(anyLong())).willReturn(options);

        //when
        List<OptionModel.Info> result = optionService.createOption(1L, registerMany);

        //then
        assertAll(
            () -> assertEquals(3, result.size()),
            () -> assertEquals("testOption1", result.get(0).name()),
            () -> assertEquals(10, result.get(0).quantity()),
            () -> assertEquals("testOption", result.get(1).name()),
            () -> assertEquals(10, result.get(1).quantity())
        );
    }

    @Test
    @DisplayName("옵션_생성_실패_중복된_이름")
    void 옵션_생성_실패() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> optionService.createOption(1L, duplicateRegisterMany));
    }

    @Test
    @DisplayName("옵션_조회")
    void 옵션_조회() {
        //given
        given(optionRepository.findAllByProductId(anyLong())).willReturn(options);

        //when
        List<OptionModel.Info> result = optionService.getOptions(1L);

        //then
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("상품_수정_성공")
    void 상품_수정_성공() {
        //given
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(optionRepository.findAllByProductId(anyLong())).willReturn(options);
        OptionCommand.Update command = new OptionCommand.Update("newName", 10);

        OptionModel.Info result = optionService.updateOption(1L, 1L, command);

        assertAll(
            () -> assertEquals("newName", result.name()),
            () -> assertEquals(10, result.quantity())
        );
    }

    @Test
    @DisplayName("상품_수정_실패")
    void 상품_수정_실패() {
        //given
        options = new Options(List.of(createOption("testOption1"), createOption("testOption2")));
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(optionRepository.findAllByProductId(anyLong())).willReturn(options);
        OptionCommand.Update command = new OptionCommand.Update("testOption2", 10);

        //when & then
        assertThrows(IllegalArgumentException.class,
            () -> optionService.updateOption(1L, 1L, command));

    }

    private Product createProduct() {
        return createProduct("testProduct");
    }

    private Product createProduct(String name) {
        Category category = new Category("category", "ABCD", "test", "test");
        Product product = new Product(1L, name, 1000, "product1.jpg", category);

        return product;
    }

    private Option createOption() {
        Product product = createProduct("testOption");
        return new Option("testOption", 10, product);
    }

    private Option createOption(String name) {
        Product product = createProduct();
        return new Option(name, 10, product);
    }

    private Option createOption(String name, int quantity) {
        return new Option(name, quantity, product);
    }

    private OptionCommand.Info createInfo(String name) {
        return new OptionCommand.Info(name, 10);
    }

    private OptionCommand.RegisterMany createRegisterMany(List<String> names) {
        return new OptionCommand.RegisterMany(names.stream().map(this::createInfo).toList());
    }
}
