package gift.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

import gift.exception.CustomException;
import gift.product.category.entity.Category;
import gift.product.entity.Product;
import gift.product.option.dto.request.CreateOptionRequest;
import gift.product.option.dto.request.UpdateOptionRequest;
import gift.product.option.entity.Option;
import gift.product.option.repository.OptionJpaRepository;
import gift.product.option.service.OptionService;
import gift.product.repository.ProductJpaRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @InjectMocks
    private OptionService optionService;

    @Mock
    private ProductJpaRepository productRepository;

    @Mock
    private OptionJpaRepository optionRepository;

    @Test
    @DisplayName("옵션 이름 공백 포함 최대 50자 예외")
    void createNameLengthOver50() {
        // given
        Product product = createProduct();
        CreateOptionRequest newOption = new CreateOptionRequest("a".repeat(51), 1);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, newOption)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("옵션 수량 1개 이상 1억 개 미만 예외 - 1개 미만")
    void createOptionQuantityUnder1() {
        // given
        Product product = createProduct();
        CreateOptionRequest newOption = new CreateOptionRequest("a", 0);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, newOption)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("옵션 수량 1개 이상 1억 개 미만 예외 - 1억 개 이상")
    void createOptionQuantityOver1Million() {
        // given
        Product product = createProduct();
        CreateOptionRequest newOption = new CreateOptionRequest("a", 100_000_000);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, newOption)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("동일한 상품 내 옵션 이름 중복 허용 X")
    void createOptionDuplicateOptionNameInProduct() {
        // given
        Option option = new Option("a", 2, null);
        Product product = createProductWithOptions(option);
        CreateOptionRequest newOption = new CreateOptionRequest("a", 1);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, newOption)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("특수 문자 예외 케이스")
    void createOptionSpecialCharacterExceptionTest() {
        // given
        Product product = createProduct();
        CreateOptionRequest newOption = new CreateOptionRequest("!@#$", 1);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when & then
        assertThatThrownBy(() -> optionService.createOption(1L, newOption)).isInstanceOf(
            CustomException.class);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("createOption test")
    void createOptionTest() {
        // given
        Product product = createProduct();
        CreateOptionRequest newOption = new CreateOptionRequest("a", 1);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(optionRepository.save(any())).willReturn(new Option("a", 1, product));

        // when
        optionService.createOption(1L, newOption);

        // then
        then(productRepository).should().findById(any());
        then(optionRepository).should().save(any());
    }

    @Test
    @DisplayName("getProductOptionTest")
    void getProductOptionTest() {
        // given
        Option newOption = new Option("a", 1, null);
        Product product = createProductWithOptions(newOption);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        // when
        var actual = optionService.getProductOptions(1L);

        // then
        assertThat(actual).hasSize(1);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("옵션 수량 수정 1개 이상 1억 개 미만 - 1개 미만")
    void updateOptionQuantityUnder1() {
        // given
        Option option = new Option("a", 100, null);
        setIdUsingReflection(option, 1L);
        Product product = createProductWithOptions(option);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateOptionRequest request = new UpdateOptionRequest("a", 0);

        // when & then
        assertThatThrownBy(() -> optionService.updateOption(1L, 1L, request))
            .isInstanceOf(CustomException.class);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("옵션 수량 수정 1개 이상 1억 개 미만 - 1억 개 이상")
    void updateOptionQuantityOver1Million() {
        // given
        Option option = new Option("a", 1000, null);
        setIdUsingReflection(option, 1L);
        Product product = createProductWithOptions(option);
        setIdUsingReflection(product, 1L);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateOptionRequest request = new UpdateOptionRequest("a", 100_000_000);

        // when & then
        assertThatThrownBy(() -> optionService.updateOption(1L, 1L, request))
            .isInstanceOf(CustomException.class);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("수정된 옵션 이름 중복")
    void updateOptionIfNameDuplicate() {
        // given
        Option option1 = new Option("a", 100, null);
        setIdUsingReflection(option1, 1L);
        Option option2 = new Option("b", 100, null);
        setIdUsingReflection(option2, 2L);
        Product product = createProductWithOptions(option1, option2);
        setIdUsingReflection(product, 1L);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateOptionRequest newOption = new UpdateOptionRequest("b", 1000);

        // when & then
        assertThatThrownBy(
            () -> optionService.updateOption(1L, 1L, newOption))
            .isInstanceOf(CustomException.class);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("옵션 수정 특수문자 예외")
    void updateOptionSpecialCharacterException() {
        // given
        Option option = new Option("a", 100, null);
        setIdUsingReflection(option, 1L);
        Product product = createProductWithOptions(option);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateOptionRequest request = new UpdateOptionRequest("!@#$", 100);

        // when & then
        assertThatThrownBy(() -> optionService.updateOption(1L, 1L, request))
            .isInstanceOf(CustomException.class);
        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("update Option test")
    void updateOptionTest() {
        // given
        Option option = new Option("a", 100, null);
        setIdUsingReflection(option, 1L);
        Product product = createProductWithOptions(option);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        UpdateOptionRequest request = new UpdateOptionRequest("updated", 1000);

        // when
        optionService.updateOption(1L, 1L, request);

        // then
        then(productRepository).should().findById(any());
        assertThat(option.getName()).isEqualTo("updated");
        assertThat(option.getQuantity()).isEqualTo(1000);
    }

    @Test
    @DisplayName("유일한 옵션 삭제 불가")
    void deleteLastOption() {
        // given
        Option option = new Option("a", 100, null);
        Product product = createProductWithOptions(option);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(optionRepository.findById(any())).willReturn(Optional.of(option));

        // when & then
        assertThatThrownBy(() -> optionService.deleteOption(1L, 1L)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("해당 상품의 옵션이 아닌 경우")
    void deleteOtherProductOption() {
        // given
        Option option = new Option("a", 100, null);
        Option productOption = new Option("b", 100, null);
        Product product = createProductWithOptions(productOption);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(optionRepository.findById(any())).willReturn(Optional.of(option));

        // when & then
        assertThatThrownBy(() -> optionService.deleteOption(1L, 1L)).isInstanceOf(
            CustomException.class);
    }

    @Test
    @DisplayName("deleteOption test")
    void deleteOptionTest() {
        // given
        Option option1 = new Option("a", 100, null);
        Option option2 = new Option("b", 100, null);
        Product product = createProductWithOptions(option1, option2);
        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(optionRepository.findById(any())).willReturn(Optional.of(option1));
        willDoNothing().given(optionRepository).delete(any());

        // when
        optionService.deleteOption(1L, 1L);

        // then
        then(optionRepository).should().delete(any());
    }

    @Test
    @DisplayName("Option subtract - 남은 재고보다 큰 수 차감")
    void optionsSubtractOverQuantity() {
        // given
        Option option1 = new Option("a", 100, null);
        given(optionRepository.findByIdWithPessimisticLocking(any())).willReturn(
            Optional.of(option1));

        // when & then
        assertThatThrownBy(() -> optionService.subtractOptionQuantity(1L, 1000))
            .isInstanceOf(CustomException.class);
    }

    @Test
    @DisplayName("Option subtract test - happy case")
    void optionSubtractTest() {
        // given
        Option option1 = new Option("a", 100, null);
        given(optionRepository.findByIdWithPessimisticLocking(any())).willReturn(
            Optional.of(option1));

        // when
        optionService.subtractOptionQuantity(1L, 30);

        // then
        then(optionRepository).should().findByIdWithPessimisticLocking(any());
        assertThat(option1.getQuantity()).isEqualTo(70);
    }

    private Product createProduct() {
        return createProduct("Product 1");
    }

    private Product createProduct(String name) {
        return new Product(
            name,
            1000,
            "image",
            new Category("category")
        );
    }

    private Product createProductWithOptions(Option... option) {
        return new Product(
            "Product 1",
            1000,
            "image",
            new Category("category"),
            Set.of(option)
        );
    }

    private void setIdUsingReflection(Object entity, Long id) {
        try {
            Field idField = entity.getClass().getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(entity, id);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

}
