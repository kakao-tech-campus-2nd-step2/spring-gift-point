package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import gift.category.dto.CategoryResponseDto;
import gift.category.entity.Category;
import gift.category.service.CategoryService;
import gift.global.dto.PageInfoDto;
import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.option.service.OptionService;
import gift.product.dto.FullOptionsProductResponseDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Options;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OptionService optionService;
    @Mock
    private CategoryService categoryService;

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void insertProductTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";

        var optionResponseDto1 = new OptionResponseDto(1L, "화이트초콜릿", 10000,
            productId);
        var categoryResponseDto1 = new CategoryResponseDto(1L, "간식", "gansik.png");
        var options = new Options(List.of(optionResponseDto1.toOption()));
        var product = new Product(productId, name, price, imageUrl,
            categoryResponseDto1.toCategory(), options);
        var productRequestDto1 = new ProductRequestDto(name, price, imageUrl);

        given(optionService.selectOption(1L)).willReturn(optionResponseDto1);
        given(categoryService.selectCategory(1L)).willReturn(categoryResponseDto1);
        given(productRepository.save(any())).willReturn(product);

        // when, then
        Assertions.assertThatCode(() -> productService.insertProduct(productRequestDto1, 1L, 1L))
            .doesNotThrowAnyException();

        then(optionService).should().selectOption(1L);
        then(categoryService).should().selectCategory(1L);
        then(productRepository).should().save(any());
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 없는 것을 넣은 경우
     */
    @Test
    public void selectProductTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var invalidProductId = 2L;

        var category = new Category(1L, "간식", "gansik.png");
        var options = new Options(List.of(new Option(1L, "화이트초콜릿", 10000, productId),
            new Option(2L, "다크초콜릿", 1000, productId)));
        var product = new Product(productId, name, price, imageUrl, category, options);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.findById(invalidProductId)).willReturn(Optional.empty());

        // when, then
        // 정상
        Assertions.assertThat(productService.selectProduct(productId))
            .isEqualTo(ProductResponseDto.fromProduct(product));

        // 없는 것을 넣음.
        Assertions.assertThatThrownBy(() -> productService.selectProduct(invalidProductId))
            .isInstanceOf(
                NoSuchElementException.class);

        // 정상
        then(productRepository).should().findById(productId);
        // 없는 값
        then(productRepository).should().findById(invalidProductId);
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 없는 것을 넣은 경우
     */
    @Test
    public void selectFullOptionProductTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var invalidProductId = 2L;

        var category = new Category(1L, "간식", "gansik.png");
        var options = new Options(List.of(new Option(1L, "화이트초콜릿", 10000, productId),
            new Option(2L, "다크초콜릿", 1000, productId)));
        var product = new Product(productId, name, price, imageUrl, category, options);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.findById(invalidProductId)).willReturn(Optional.empty());

        // when, then
        // 정상
        Assertions.assertThat(productService.selectFullOptionProduct(productId))
            .isEqualTo(FullOptionsProductResponseDto.fromProduct(product));

        // 없는 것을 넣음
        Assertions.assertThatThrownBy(() -> productService.selectProduct(invalidProductId))
            .isInstanceOf(
                NoSuchElementException.class);

        // 정상
        then(productRepository).should().findById(productId);
        // 없는 값
        then(productRepository).should().findById(invalidProductId);
    }

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void selectProductsTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var productId2 = 2L;
        var name2 = "사탕";
        var price2 = 1000;
        var imageUrl2 = "satang.png";

        var pageInfoDto = new PageInfoDto(0, 10, "price", "DESC");
        var category = new Category(1L, "간식", "gansik.png");
        var options = new Options(List.of(new Option(1L, "화이트초콜릿", 10000, productId),
            new Option(2L, "다크초콜릿", 1000, productId)));
        var options2 = new Options(List.of(new Option(1L, "츄파츕스", 100000, productId2),
            new Option(2L, "츕파츄스", 10000, productId2)));
        var product = new Product(productId, name, price, imageUrl, category, options);
        var product2 = new Product(productId2, name2, price2, imageUrl2, category, options2);
        var products = List.of(product, product2);
        var productPage = new PageImpl<>(products, pageInfoDto.toPageInfo().toPageRequest(), 2L);

        given(productRepository.findAll(any(PageRequest.class))).willReturn(productPage);

        // when, then
        Assertions.assertThat(productService.selectProducts(pageInfoDto))
            .isEqualTo(productPage.stream().map(ProductResponseDto::fromProduct).toList());

        then(productRepository).should().findAll(any(PageRequest.class));
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 제품을 선택한 경우
     */
    @Test
    public void updateProductTest() {
        // given
        var productId = 1L;
        var invalidProductId = 2L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";

        var productRequestDto1 = new ProductRequestDto("사탕", 1000, "satang.png");
        var categoryResponseDto1 = new CategoryResponseDto(2L, "디저트", "sweet.png");
        var category = new Category(1L, "간식", "gansik.png");
        var options = new Options(List.of(new Option(1L, "화이트초콜릿", 10000, productId)));
        var product = new Product(productId, name, price, imageUrl, category, options);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.findById(invalidProductId)).willReturn(Optional.empty());
        given(categoryService.selectCategory(2L)).willReturn(categoryResponseDto1);

        // when, then
        // 정상
        productService.updateProduct(1L, productRequestDto1, 2L);
        Assertions.assertThat(product.getName()).isEqualTo("사탕");

        // 존재하지 않는 제품
        Assertions.assertThatThrownBy(
                () -> productService.updateProduct(invalidProductId, productRequestDto1, 2L))
            .isInstanceOf(NoSuchElementException.class);

        // 정상
        then(productRepository).should().findById(productId);
        then(categoryService).should().selectCategory(2L);
        // 존재하지 않는 제품
        then(productRepository).should().findById(invalidProductId);
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 중복된 옵션명을 넣는 경우
     */
    @Test
    public void insertOptionTest() {
        // given
        var productId = 1L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";

        var dupOptionId = 2L;
        var optionId = 3L;

        var category = new Category(1L, "간식", "gansik.png");
        var options = new Options(new Option(1L, "화이트초콜릿", 10000, productId));
        var product = new Product(productId, name, price, imageUrl, category, options);
        var dupOptionResponseDto = new OptionResponseDto(dupOptionId, "화이트초콜릿", 1000,
            productId);
        var optionResponseDto = new OptionResponseDto(optionId, "다크초콜릿", 1000, productId);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(optionService.selectOption(dupOptionId)).willReturn(dupOptionResponseDto);
        given(optionService.selectOption(optionId)).willReturn(optionResponseDto);

        // when, then
        // 정상
        productService.insertOption(productId, optionId);
        Assertions.assertThat(product.getOptions().getOption(optionId).getName())
            .isEqualTo("다크초콜릿");

        // 중복된 옵션명
        Assertions.assertThatThrownBy(() -> productService.insertOption(productId, dupOptionId))
            .isInstanceOf(IllegalArgumentException.class);

        // 정상, 중복 옵션
        then(productRepository).should(times(2)).findById(1L);
        // 정상
        then(optionService).should().selectOption(optionId);
        // 중복
        then(optionService).should().selectOption(dupOptionId);
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 제품의 옵션을 차감하는 경우
     * - [ ] 수량이 0개인 옵션을 차감하는 경우
     */
    @Test
    public void subtractOptionTest() {
        // given
        var productId = 1L;
        var invalidProductId = 2L;
        var name = "초콜릿";
        var price = 3000;
        var imageUrl = "choco.png";
        var optionId = 1L;
        var optionQuantity = 10000;
        var optionName = "화이트초콜릿";
        var zeroOptionId = 2L;
        var zeroOptionQuantity = 0;
        var zeroOptionName = "다크초콜릿(품절)";

        var category = new Category(1L, "간식", "gansik.png");
        var option = new Option(optionId, optionName, optionQuantity, productId);
        var zeroOption = new Option(zeroOptionId, zeroOptionName, zeroOptionQuantity, productId);
        var options = new Options(new ArrayList<>(List.of(option, zeroOption)));
        var product = new Product(productId, name, price, imageUrl, category, options);

        given(productRepository.findById(productId)).willReturn(Optional.of(product));
        given(productRepository.findById(invalidProductId)).willReturn(Optional.empty());

        // when, then
        // 정상
        productService.subtractOption(productId, optionId, 1);
        Assertions.assertThat(product.getOptions().getOption(optionId).getQuantity())
            .isEqualTo(optionQuantity - 1);

        // 존재하지 않는 제품
        Assertions.assertThatThrownBy(
                () -> productService.subtractOption(invalidProductId, optionId, 1))
            .isInstanceOf(NoSuchElementException.class);

        // 수량이 0개
        Assertions.assertThatThrownBy(
                () -> productService.subtractOption(productId, zeroOptionId, 1))
            .isInstanceOf(NoSuchElementException.class);

        // 차감하려는 수량이 더 많음
        Assertions.assertThatThrownBy(
                () -> productService.subtractOption(productId, zeroOptionId, 1000000))
            .isInstanceOf(IllegalArgumentException.class);

        // 정상, 수량이 0개, 차감하려는 수량이 더 많음
        then(productRepository).should(times(3)).findById(1L);
        // 존재하지 않는 제품
        then(productRepository).should().findById(2L);
    }
}
