package gift.product.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.product.domain.Category;
import gift.product.domain.Product;
import gift.product.domain.ProductOption;
import gift.product.exception.option.ProductOptionDuplicatedException;
import gift.product.exception.option.ProductOptionNotDeletedException;
import gift.product.persistence.ProductOptionRepository;
import gift.product.persistence.ProductRepository;
import gift.product.service.command.ProductOptionCommand;
import gift.product.service.dto.ProductOptionInfo;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductOptionServiceTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private ProductOptionRepository productOptionRepository;

    @InjectMocks
    private ProductOptionService productOptionService;

    @Test
    @DisplayName("ProductOptionService Option생성 테스트")
    void createProductOptionTest() {
        //given
        final Long productId = 1L;
        ProductOptionCommand productOptionCommand = new ProductOptionCommand("optionName", 10);

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        ProductOption productOption = new ProductOption(1L, "optionName", 10, product);

        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(productOptionRepository.save(any())).willReturn(productOption);

        //when
        Long optionId = productOptionService.createProductOption(productId, productOptionCommand);

        //then
        assertThat(optionId).isEqualTo(productOption.getId());
    }

    @Test
    @DisplayName("ProductOptionService Option수정 테스트")
    void modifyProductOptionTest() {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;
        ProductOptionCommand productOptionCommand = new ProductOptionCommand("newName", 10);

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        ProductOption productOption = new ProductOption(1L, "optionName", 10, product);

        given(productOptionRepository.findByProductIdAndId(any(), any())).willReturn(Optional.of(productOption));

        //when
        productOptionService.modifyProductOption(productId, optionId, productOptionCommand);

        //then
        assertThat(productOption.getName()).isEqualTo(productOptionCommand.name());
        assertThat(productOption.getQuantity()).isEqualTo(productOptionCommand.quantity());
    }

    @Test
    @DisplayName("ProductOptionService Option정보 조회 테스트")
    void getProductOptionInfoTest() {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        ProductOption productOption = new ProductOption(1L, "optionName", 10, product);

        given(productOptionRepository.findByProductIdAndId(any(), any())).willReturn(Optional.of(productOption));

        //when
        ProductOptionInfo productOptionInfo = productOptionService.getProductOptionInfo(productId, optionId);

        //then
        assertThat(productOptionInfo.id()).isEqualTo(productOption.getId());
        assertThat(productOptionInfo.name()).isEqualTo(productOption.getName());
        assertThat(productOptionInfo.quantity()).isEqualTo(productOption.getQuantity());
    }

    @Test
    @DisplayName("ProductOptionService Option 삭제 테스트")
    void deleteProductOptionTest() {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        List<ProductOption> productOptions = List.of(
                new ProductOption(1L, "optionName", 10, product),
                new ProductOption(2L, "optionName2", 10, product)
        );
        given(productOptionRepository.findByProductId(any())).willReturn(productOptions);
        given(productOptionRepository.findByProductIdAndId(any(), any())).willReturn(
                Optional.of(productOptions.get(0)));

        //when
        productOptionService.deleteProductOption(productId, optionId);

        //then
        then(productOptionRepository).should().delete(any());
    }

    @Test
    @DisplayName("ProductOptionService Option 삭제 실패 테스트[최소 1개 이상]")
    void deleteProductOptionWithMinimumOptionTest() {
        //given
        final Long productId = 1L;
        final Long optionId = 1L;

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        List<ProductOption> productOptions = List.of(
                new ProductOption(1L, "optionName", 10, product)
        );
        given(productOptionRepository.findByProductId(any())).willReturn(productOptions);

        //when//then
        assertThatThrownBy(() -> productOptionService.deleteProductOption(productId, optionId))
                .isInstanceOf(ProductOptionNotDeletedException.class);
    }

    @Test
    @DisplayName("ProductOptionService Option 생성 실패 테스트[중복된 이름]")
    void createProductOptionWithDuplicatedNameTest() {
        //given
        final Long productId = 1L;
        ProductOptionCommand productOptionCommand = new ProductOptionCommand("optionName", 10);

        Category category = new Category("카테고리", "색상", "이미지", "설명");
        Product product = new Product("productName", 1000, "이미지", category);
        ProductOption productOption = new ProductOption(1L, "optionName", 10, product);

        given(productRepository.findById(any())).willReturn(Optional.of(product));
        given(productOptionRepository.findByProductId(any())).willReturn(List.of(productOption));

        //when//then
        assertThatThrownBy(() -> productOptionService.createProductOption(productId, productOptionCommand))
                .isInstanceOf(ProductOptionDuplicatedException.class);
    }
}