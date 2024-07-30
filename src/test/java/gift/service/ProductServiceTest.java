package gift.service;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.request.OptionRequestDto;
import gift.dto.request.ProductRequestDto;
import gift.dto.response.ProductResponseDto;
import gift.exception.customException.EntityNotFoundException;
import gift.exception.customException.KakaoInNameException;
import gift.repository.category.CategoryRepository;
import gift.repository.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static gift.exception.exceptionMessage.ExceptionMessage.CATEGORY_NOT_FOUND;
import static gift.exception.exceptionMessage.ExceptionMessage.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionService optionService;

    @Test
    @DisplayName("상품 저장, 수정 시 이름에 카카오 포함 Exception 테스트")
    void 상품_카카오_포함_테스트(){
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트 카카오", 1000, "abc.png", 1L);
        OptionRequestDto optionRequestDto = new OptionRequestDto("TEST", 100);

        assertThatThrownBy(() -> productService.addProduct(productRequestDto, optionRequestDto))
                .isInstanceOf(KakaoInNameException.class);

        assertThatThrownBy(() -> productService.updateProduct(1L, productRequestDto))
                .isInstanceOf(KakaoInNameException.class);
    }

    @Test
    @DisplayName("상품 저장 시 카테고리 NOT FOUND EXCEPTION 테스트")
    void 상품_저장_카테고리_NOT_FOUND_EXCEPTION_테스트(){
        //given
        ProductRequestDto inValidproductRequestDto = new ProductRequestDto("테스트 상품", 1000, "abc.png", 2L);
        OptionRequestDto optionRequestDto = new OptionRequestDto("TEST", 100);

        given(categoryRepository.findById(inValidproductRequestDto.categoryId())).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> productService.addProduct(inValidproductRequestDto, optionRequestDto))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage("해당 카테고리는 존재하지 않습니다.")
        );
    }

    @Test
    @DisplayName("상품 정상 저장 테스트")
    void 상품_정상_저장_테스트() throws Exception{
        //given
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트 상품", 1000, "abc.png", 1L);
        OptionRequestDto optionRequestDto = new OptionRequestDto("TEST", 100);

        Category category = new Category("상품권", "#0000", "abc.png");

        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Field idField = Product.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(product, 1L);

        given(productRepository.save(any(Product.class))).willReturn(product);
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        //when
        ProductResponseDto productResponseDto = productService.addProduct(productRequestDto, optionRequestDto);

        //then
        assertAll(
                () -> assertThat(productResponseDto.name()).isEqualTo(product.getName()),
                () -> assertThat(productResponseDto.price()).isEqualTo(product.getPrice()),
                () -> assertThat(productResponseDto.imageUrl()).isEqualTo(product.getImageUrl()),
                () -> verify(optionService, times(1)).saveOption(any(OptionRequestDto.class), any(Long.class))
        );
    }

    @Test
    @DisplayName("상품 전체 조회 테스트")
    void 상품_전체_조회_테스트(){
        //given
        Category category = new Category("상품권", "#0000", "abc.png");

        Product product1 = new Product.Builder()
                .name("테스트 상품1")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Product product2 = new Product.Builder()
                .name("테스트 상품2")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        Product product3 = new Product.Builder()
                .name("테스트 상품3")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        List<Product> products = Arrays.asList(product1, product2, product3);

        given(productRepository.findAll()).willReturn(products);

        //when
        List<ProductResponseDto> findProductsDto = productService.findAllProducts();

        //then
        assertAll(
                () -> assertThat(findProductsDto.size()).isEqualTo(3),
                () -> assertThat(findProductsDto.get(0).name()).isEqualTo("테스트 상품1")
        );
    }

    @Test
    @DisplayName("상품 수정 시 상품 NOT FOUND EXCEPTION 테스트")
    void 상품_수정_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long nullId = 1L;
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트 상품", 1000, "abc.png", 1L);

        given(productRepository.findById(nullId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> productService.updateProduct(nullId, productRequestDto))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(PRODUCT_NOT_FOUND)
        );
    }

    @Test
    @DisplayName("상품 수정 시 카테고리 NOT FOUND EXCEPTION 테스트")
    void 상품_수정_카테고리_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long testId = 1L;
        Long nullId = 2L;
        Category category = new Category("상품권", "#0000", "abc.png");
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트 상품", 1000, "abc.png", nullId);

        Product product = new Product.Builder()
                .name("수정 전")
                .price(1)
                .imageUrl("abcd.png")
                .category(category)
                .build();

        given(productRepository.findById(testId)).willReturn(Optional.of(product));
        given(categoryRepository.findById(nullId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> productService.updateProduct(testId, productRequestDto))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(CATEGORY_NOT_FOUND)
        );
    }
    @Test
    @DisplayName("상품 수정 테스트")
    void 상품_수정_테스트(){
        //given
        Long testId = 1L;
        Category category = new Category("상품권", "#0000", "abc.png");
        ProductRequestDto productRequestDto = new ProductRequestDto("테스트 상품", 1000, "abc.png", 1L);

        Product product = new Product.Builder()
                .name("수정 전")
                .price(1)
                .imageUrl("abcd.png")
                .category(category)
                .build();

        given(productRepository.findById(testId)).willReturn(Optional.of(product));
        given(categoryRepository.findById(1L)).willReturn(Optional.of(category));

        //when
        ProductResponseDto updatedProductDto = productService.updateProduct(testId, productRequestDto);

        //then
        assertAll(
                () -> assertThat(updatedProductDto.name()).isEqualTo(productRequestDto.name()),
                () -> assertThat(updatedProductDto.price()).isEqualTo(productRequestDto.price()),
                () -> assertThat(updatedProductDto.imageUrl()).isEqualTo(productRequestDto.imageUrl())
        );
    }

    @Test
    @DisplayName("상품 삭제 시 상품 NOT FOUND EXCEPTION 테스트")
    void 상품_삭제_NOT_FOUND_EXCEPTION_테스트(){
        //given
        Long nullId = 1L;

        given(productRepository.findById(nullId)).willReturn(Optional.empty());

        //expected
        assertAll(
                () -> assertThatThrownBy(() -> productService.deleteProduct(nullId))
                        .isInstanceOf(EntityNotFoundException.class)
                        .hasMessage(PRODUCT_NOT_FOUND)
        );

    }

    @Test
    @DisplayName("상품 삭제 테스트")
    void 상품_삭제_테스트(){
        //given
        Long testId = 1L;
        Category category = new Category("상품권", "#0000", "abc.png");
        Product product = new Product.Builder()
                .name("테스트 상품")
                .price(1000)
                .imageUrl("abc.png")
                .category(category)
                .build();

        given(productRepository.findById(testId)).willReturn(Optional.of(product));

        //when
        ProductResponseDto deletedProductDto = productService.deleteProduct(testId);

        //then
        assertAll(
                () -> assertThat(deletedProductDto.name()).isEqualTo(product.getName()),
                () -> assertThat(deletedProductDto.price()).isEqualTo(product.getPrice()),
                () -> assertThat(deletedProductDto.imageUrl()).isEqualTo(product.getImageUrl()),
                () -> verify(productRepository, times(1)).delete(product)
        );

    }

    @Test
    @DisplayName("상품 페이지 기능 테스트 - 가격으로 정렬")
    void 상품_페이지_기능_테스트(){
        //given
        List<Product> products = new ArrayList<>();
        Category category = new Category("상품권", "#0000", "abc.png");
        for(int i=0; i<20; i++){
            Product product = new Product.Builder()
                    .name("테스트" + i)
                    .price(i)
                    .imageUrl("abc.png")
                    .category(category)
                    .build();

            products.add(product);
        }

        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "price"));

        given(productRepository.findAll(pageRequest)).willReturn(new PageImpl<>(products.subList(15, 20).reversed(), pageRequest, products.size()));

        //when
        List<ProductResponseDto> productsDto = productService.findProducts(pageRequest);

        //then
        assertAll(
                () -> assertThat(productsDto.size()).isEqualTo(5),
                () -> assertThat(productsDto.get(0).name()).isEqualTo("테스트19"),
                () -> assertThat(productsDto.get(0).price()).isEqualTo(19)
        );
    }
}