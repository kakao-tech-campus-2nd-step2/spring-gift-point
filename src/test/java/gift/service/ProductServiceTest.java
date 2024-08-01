package gift.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.product.dto.option.OptionDto;
import gift.product.dto.product.ClientProductRequest;
import gift.product.dto.product.ClientProductUpdateRequest;
import gift.product.model.Category;
import gift.product.model.Product;
import gift.product.repository.CategoryRepository;
import gift.product.repository.OptionRepository;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ProductServiceTest {

    @InjectMocks
    ProductService productService;

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    OptionRepository optionRepository;

    @Test
    void 상품_추가() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));
        List<OptionDto> optionDtos = new ArrayList<>();
        optionDtos.add(new OptionDto("테스트옵션", 1));

        //when
        productService.insertProduct(
            new ClientProductRequest(product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory().getId(),
                optionDtos));

        //then
        then(productRepository).should().save(any());
        then(optionRepository).should().save(any());
    }

    @Test
    void 상품_조회() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        productService.getProduct(product.getId());

        //then
        then(productRepository).should().findById(product.getId());
    }

    @Test
    void 상품_전체_조회() {
        //given
        given(productRepository.findAll()).willReturn(Collections.emptyList());

        //when
        productService.getProductAll();

        //then
        then(productRepository).should().findAll();
    }

    @Test
    void 상품_전체_조회_페이지() {
        //given
        int PAGE = 1;
        int SIZE = 4;
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.Direction.fromString("desc"), "name");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "테스트상품", 1000, "테스트주소", category));
        given(productRepository.findAll(pageable)).willReturn(new PageImpl<>(products));

        //when
        productService.getProductAll(pageable);

        //then
        then(productRepository).should().findAll(pageable);
    }

    @Test
    void 상품_전체_조회_페이지_특정_카테고리만() {
        //given
        int PAGE = 1;
        int SIZE = 4;
        Pageable pageable = PageRequest.of(PAGE, SIZE, Sort.Direction.fromString("desc"), "name");
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1L, "테스트상품", 1000, "테스트주소", category));
        given(productRepository.findAllByCategoryId(pageable,
            category.getId())).willReturn(new PageImpl<>(products));

        //when
        productService.getProductAll(pageable, category.getId());

        //then
        then(productRepository).should().findAllByCategoryId(pageable, category.getId());
    }

    @Test
    void 상품_수정() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        given(categoryRepository.findById(category.getId())).willReturn(Optional.of(category));

        //when
        ClientProductUpdateRequest clientProductUpdateRequest = new ClientProductUpdateRequest(
            "테스트상품수정",
            2000,
            "테스트주소수정",
            category.getId());
        productService.updateProduct(product.getId(), clientProductUpdateRequest);

        //then
        then(productRepository).should().save(any());
    }

    @Test
    void 상품_삭제() {
        //given
        Category category = new Category(1L, "테스트카테고리", "테스트컬러", "테스트주소", "테스트설명");
        Product product = new Product(1L, "테스트상품", 1500, "테스트주소", category);
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        //when
        productService.deleteProduct(product.getId());

        //then
        then(productRepository).should().deleteById(product.getId());
    }

    @Test
    void 실패_존재하지_않는_상품_조회() {
        //given
        given(productRepository.findById(any())).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> productService.getProduct(-1L)).isInstanceOf(
            NoSuchElementException.class);
    }
}
