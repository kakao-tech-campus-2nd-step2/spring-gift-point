package gift.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import gift.exception.InputException;
import gift.exception.option.DeleteOptionsException;
import gift.exception.option.DuplicateOptionsException;
import gift.exception.option.NotFoundOptionsException;
import gift.exception.option.OptionsQuantityException;
import gift.exception.product.NotFoundProductException;
import gift.model.Category;
import gift.model.Options;
import gift.model.Product;
import gift.repository.OptionsRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OptionsServiceTest {

    @Autowired
    private OptionsService optionsService;
    @MockBean
    private OptionsRepository optionsRepository;
    @MockBean
    private ProductRepository productRepository;

    @DisplayName("해당 상품의 모든 옵션 조회 테스트")
    @Test
    void findAll() {
        //given
        Product product = demoProduct();
        List<Options> options = IntStream.range(0, 50)
            .mapToObj(i -> demoOptions((long) i, product))
            .collect(Collectors.toList());
        given(optionsRepository.findAllByProductId(any(Long.class)))
            .willReturn(options);

        // when
        List<Options> allOptions = optionsRepository.findAllByProductId(product.getId());

        // then
        then(optionsRepository).should().findAllByProductId(product.getId());
        allOptions
            .forEach(option -> assertThat(option.getProduct()).isEqualTo(product));
    }

    @DisplayName("해당 상품의 특정 옵션 조회 테스트")
    @Test
    void findById() {
        //given
        Product product = demoProduct();
        Long id = 1L;
        Options option = demoOptions(id, product);

        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.of(option));
        //when
        Optional<Options> foundOption = optionsRepository.findById(id);
        //then
        then(optionsRepository).should().findById(id);
        assertThat(foundOption.isPresent()).isTrue();
        assertThat(foundOption.get().getProduct()).isEqualTo(product);
        assertThat(foundOption.get().getId()).isEqualTo(id);
    }

    @DisplayName("옵션 생성 테스트")
    @Test
    void save() {
        //given
        Long id = 1L;
        String name = "옵션";
        Integer quantity = 1;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findByNameAndProductId(any(String.class), any(Long.class)))
            .willReturn(Optional.empty());
        given(optionsRepository.save(any(Options.class)))
            .willReturn(new Options(id, name, quantity, product));

        //when
        optionsService.addOption(name, quantity, product.getId());

        //then
        then(optionsRepository).should().save(any(Options.class));
    }

    @DisplayName("존재하지 않는 상품 기반 옵션 생성 테스트")
    @Test
    void failSave() {
        //given
        Long id = 1L;
        String name = "옵션";
        Integer quantity = 1;
        Product notExistedProduct = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());

        //when // then
        assertThatThrownBy(
            () -> optionsService.addOption(name, quantity, notExistedProduct.getId()))
            .isInstanceOf(NotFoundProductException.class);
    }

    @DisplayName("옵션 중복 생성 테스트")
    @Test
    void failSave2() {
        //given
        Long id = 1L;
        String name = "이미 존재하는 옵션명";
        Integer quantity = 1;
        Product product = demoProduct();
        Options options = new Options(id, name, quantity, demoProduct());

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findByNameAndProductId(any(String.class), any(Long.class)))
            .willReturn(Optional.of(options));
        //when // then
        assertThatThrownBy(() -> optionsService.addOption(name, quantity, product.getId()))
            .isInstanceOf(DuplicateOptionsException.class);
    }


    @DisplayName("옵션 변경 테스트")
    @Test
    void update() {
        //given
        Options savedOption = mock(Options.class);
        Long id = 1L;
        String newName = "새로운 옵션 이름";
        Integer newQuantity = 455;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedOption));
        //when //then
        assertDoesNotThrow(
            () -> optionsService.updateOption(id, newName, newQuantity, product.getId()));
        then(productRepository).should().findById(product.getId());
        then(optionsRepository).should().findById(id);
        then(savedOption).should().updateOption(newName, newQuantity);
    }

    @DisplayName("옵션 변경 실패 테스트 - validation 실패")
    @Test
    void failUpdate() {
        //given
        Options savedOption = mock(Options.class);
        Long id = 1L;
        String errorName = "잘못된 이름####@!!";
        Integer errorQuantity = -1;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedOption));
        doThrow(InputException.class).when(savedOption).updateOption(errorName, errorQuantity);
        //when //then
        assertThatThrownBy(
            () -> optionsService.updateOption(id, errorName, errorQuantity, product.getId()))
            .isInstanceOf(InputException.class);
        then(productRepository).should().findById(product.getId());
        then(optionsRepository).should().findById(id);
        then(savedOption).should().updateOption(errorName, errorQuantity);
    }

    @DisplayName("옵션 변경 실패 테스트 - 존재하지 않는 상품")
    @Test
    void failUpdate2() {
        //given
        Long id = 1L;
        String newName = "옵션";
        Integer newQuantity = 1;
        Product product = demoProduct();

        given(productRepository.findById(product.getId()))
            .willReturn(Optional.empty());
        //when //then
        assertThatThrownBy(
            () -> optionsService.updateOption(id, newName, newQuantity, product.getId()))
            .isInstanceOf(NotFoundProductException.class);
        then(productRepository).should().findById(product.getId());
    }

    @DisplayName("옵션 변경 실패 테스트 - 존재하지 않는 옵션")
    @Test
    void failUpdate3() {
        //given
        Long id = 1L;
        String newName = "옵션";
        Integer newQuantity = 1;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());
        //when //then
        assertThatThrownBy(
            () -> optionsService.updateOption(id, newName, newQuantity, product.getId()))
            .isInstanceOf(NotFoundOptionsException.class);
        then(productRepository).should().findById(product.getId());
        then(optionsRepository).should().findById(id);
    }

    @DisplayName("옵션 수량 변경 테스트")
    @Test
    void quantityUpdate() {
        //given
        Options savedOption = mock(Options.class);
        Long id = 1L;
        Integer subQuantity = 1;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findByIdForUpdate(any(Long.class)))
            .willReturn(Optional.of(savedOption));
        //when //then
        assertDoesNotThrow(() -> optionsService.subtractQuantity(id,
            subQuantity, product.getId()));
        then(productRepository).should().findById(product.getId());
        then(optionsRepository).should().findByIdForUpdate(id);
        then(savedOption).should().subtractQuantity(subQuantity);
    }

    @DisplayName("옵션 수량 변경 실패 테스트")
    @Test
    void quantityFailUpdate() {
        //given
        Options savedOption = mock(Options.class);
        Long id = 1L;
        Integer subQuantity = 455;
        Product product = demoProduct();

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(product));
        given(optionsRepository.findByIdForUpdate(any(Long.class)))
            .willReturn(Optional.of(savedOption));
        doThrow(OptionsQuantityException.class)
            .when(savedOption).subtractQuantity(any(Integer.class));
        //when //then
        assertThatThrownBy(() -> optionsService.subtractQuantity(id,
            subQuantity, product.getId()))
            .isInstanceOf(OptionsQuantityException.class);
        then(productRepository).should().findById(product.getId());
        then(optionsRepository).should().findByIdForUpdate(id);
        then(savedOption).should().subtractQuantity(subQuantity);
    }

    @DisplayName("옵션 삭제 테스트")
    @Test
    void delete() {
        //given
        Long id = 1L;
        Long productId = 1L;
        Options savedOption = mock(Options.class);

        given(optionsRepository.optionsCountByProductId(any(Long.class)))
            .willReturn(2L);
        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedOption));

        // when // then
        assertDoesNotThrow(() -> optionsService.deleteOption(id, productId));
        then(optionsRepository).should().optionsCountByProductId(productId);
        then(optionsRepository).should().findById(id);
    }

    @DisplayName("옵션 삭제 실패 테스트 - 남은 옵션 개수 1개 미만")
    @Test
    void failDelete() {
        //given
        Long id = 1L;
        Long productId = 1L;

        given(optionsRepository.optionsCountByProductId(any(Long.class)))
            .willReturn(1L);

        // when // then
        assertThatThrownBy(() -> optionsService.deleteOption(id, productId))
            .isInstanceOf(DeleteOptionsException.class);
        then(optionsRepository).should().optionsCountByProductId(productId);
    }

    @DisplayName("옵션 삭제 실패 테스트 - 존재하지 않는 옵션")
    @Test
    void failDelete2() {
        //given
        Long id = 1L;
        Long productId = 1L;

        given(optionsRepository.optionsCountByProductId(any(Long.class)))
            .willReturn(2L);
        given(optionsRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());
        // when // then
        assertThatThrownBy(() -> optionsService.deleteOption(id, productId))
            .isInstanceOf(NotFoundOptionsException.class);
        then(optionsRepository).should().optionsCountByProductId(productId);
        then(optionsRepository).should().findById(id);
    }


    private static Options demoOptions(Long id, Product product) {
        return new Options(id, "옵션", 1, product);
    }

    private static Product demoProduct() {
        return new Product(1L, "상품", 1000, "http://a.com", new Category(1L, "카테고리"));
    }

}