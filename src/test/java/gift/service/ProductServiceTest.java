package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.exception.GiftException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @DisplayName("모든 상품 정보를 조회해 반환한다.")
    @Test
    void getProducts() throws Exception {
        //given
        PageRequest pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        given(productRepository.findAll(pageable)).willReturn(new PageImpl<>(List.of()));

        //when
        productService.getProducts(pageable);

        //then
        then(productRepository).should().findAll(pageable);
    }

    @DisplayName("상품 ID를 받아 해당하는 상품 정보를 조회한다.")
    @Test
    void getProduct() throws Exception {
        //given
        Long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.of(new Product()));

        //when
        productService.getProduct(productId);

        //then
        then(productRepository).should().findById(productId);
    }

    @DisplayName("상품 ID를 받아 해당하는 상품 정보를 조회하는데, 존재하지 않는 상품이면 예외를 던진다.")
    @Test
    void getProductWithNonExistingProduct() throws Exception {
        //given
        Long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        //when & then
        assertThatExceptionOfType(GiftException.class)
                .isThrownBy(() -> productService.getProduct(productId));

        then(productRepository).should().findById(productId);
    }

    @DisplayName("상품 하나를 추가한다.")
    @Test
    void addProduct() throws Exception {
        //given
        Long categoryId = 1L;
        ProductDto dto = new ProductDto("아이스티", 2500, "https://example.com", categoryId, List.of(new OptionDto("옵션", 123L)));

        given(productRepository.save(any(Product.class))).willReturn(new Product());
        given(categoryRepository.findById(anyLong())).willReturn(Optional.of(new Category()));

        //when
        productService.addProduct(dto);

        //then
        then(productRepository).should().save(any(Product.class));
        then(categoryRepository).should().findById(anyLong());
    }

    @DisplayName("상품 정보를 수정한다.")
    @Test
    void editProduct() throws Exception {
        //given
        Long productId = 1L;
        Long categoryId = 1L;
        ProductDto dto = new ProductDto("아이스티", 2500, "https://example.com", categoryId);

        given(productRepository.findById(productId)).willReturn(Optional.of(new Product()));
        given(categoryRepository.findById(categoryId)).willReturn(Optional.of(new Category()));

        //when
        productService.editProduct(productId, dto);

        //then
        then(productRepository).should().findById(productId);
        then(categoryRepository).should().findById(categoryId);
    }

    @DisplayName("상품 ID를 받아 해당하는 상품을 삭제한다.")
    @Test
    void removeProduct() throws Exception {
        //given
        Long productId = 1L;
        Product product = new Product();
        product.setId(productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        willDoNothing().given(productRepository).deleteById(productId);

        //when
        productService.removeProduct(productId);

        //then
        then(productRepository).should().deleteById(productId);
    }

    @DisplayName("상품 ID를 받아 해당하는 상품을 삭제하는데, 존재하지 않는 상품이면 예외를 던진다.")
    @Test
    void removeProductWithNonExistingProduct() throws Exception {
        //given
        Long productId = 1L;

        given(productRepository.findById(productId)).willReturn(Optional.empty());

        //when & then
        assertThatExceptionOfType(GiftException.class)
                .isThrownBy(() -> productService.removeProduct(productId));

        then(productRepository).should().findById(productId);
    }

    @DisplayName("상품에 존재하는 모든 옵션을 조회해 반환한다.")
    @Test
    void getOptions() throws Exception {
        //given
        Long productId = 1L;

        given(productRepository.findById(anyLong())).willReturn(Optional.of(new Product()));

        //when
        productService.getOptions(productId);

        //then
        then(productRepository).should().findById(anyLong());
    }

    @DisplayName("상품에 옵션 하나를 추가한다.")
    @Test
    void addOption() throws Exception {
        //given
        Long productId = 1L;
        gift.dto.OptionDto dto = new gift.dto.OptionDto("옵션", 2500L);

        given(productRepository.findById(anyLong())).willReturn(Optional.of(new Product()));

        //when
        productService.addOption(productId, dto);

        //then
        then(productRepository).should().findById(anyLong());
    }

    @DisplayName("상품 ID와 옵션 ID를 받아 상품(id)에 존재하는 옵션(id)을 삭제한다.")
    @Test
    void removeOption() throws Exception {
        //given
        Long productId = 1L;
        Long optionId = 1L;

        Product product = new Product();
        product.setId(productId);

        Option option1 = new Option("옵션", 123L);
        Option option2 = new Option("옵션", 123L);
        option1.setId(1L);
        option2.setId(2L);

        product.addOption(option1);
        product.addOption(option2);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        //when
        productService.removeOption(productId, optionId);

        //then
        then(productRepository).should().findById(productId);
    }

}
