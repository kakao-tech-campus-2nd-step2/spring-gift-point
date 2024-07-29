package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

import gift.exception.InputException;
import gift.exception.category.NotFoundCategoryException;
import gift.exception.product.NotFoundProductException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
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
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private CategoryRepository categoryRepository;


    @DisplayName("상품+카테고리 저장 테스트")
    @Test
    void save() {
        //given
        String name = "카테고리";
        Category category = new Category(1L, name);
        given(categoryRepository.findByName(name))
            .willReturn(Optional.of(category));
        given(productRepository.save(any(Product.class))).
            willReturn(new Product(1L, "name", 1000, "http://a.com", category));
        //when
        productService.addProduct("name", 1000, "http://a.com", category.getName());
        //then
        then(categoryRepository).should().findByName(any(String.class));
        then(productRepository).should().save(any(Product.class));
    }

    @DisplayName("상품+존재하지 않는 카테고리 저장 테스트")
    @Test
    void failSave() {
        //given
        String name = "카테고리";
        Category category = new Category(name);
        given(categoryRepository.findByName(name))
            .willThrow(NotFoundCategoryException.class);
        //when //then
        assertThatThrownBy(
            () -> productService.addProduct("name", 1000, "http://", category.getName()))
            .isInstanceOf(NotFoundCategoryException.class);
    }

    @DisplayName("상품+카테고리 변경 테스트")
    @Test
    void update() {
        //given
        Long id = 1L; String newName = "name"; Integer newPrice = 1000;
        String newUrl = "http://a.com"; String newCategoryName = "name";

        Product savedProduct = mock(Product.class);
        Category savedCategory = new Category("새로운 카테고리");

        given(savedProduct.getCategory())
            .willReturn(savedCategory);
        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.of(savedProduct));

        //when
        productService.updateProduct(id, newName, newPrice, newUrl, newCategoryName);
        //then
        then(productRepository).should().findById(any(Long.class));
        then(savedProduct).should().updateProduct(newName, newPrice, newUrl, newCategoryName);
    }

    @DisplayName("상품+카테고리 변경 실패 테스트")
    @Test
    void failUpdate() {
        //given
        Category category = new Category(1L, "변경된 카테고리");
        Product product = new Product(1L, "name", 1000, "http://a.com", category);

        given(productRepository.findById(any(Long.class)))
            .willReturn(Optional.empty());

        //when //then
        assertThatThrownBy(() -> productService.updateProduct(product.getId(),
            product.getName(), product.getPrice(), product.getImageUrl(), product.getCategoryName()))
            .isInstanceOf(NotFoundProductException.class);
    }


}