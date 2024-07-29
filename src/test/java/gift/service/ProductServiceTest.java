package gift.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.controller.product.dto.ProductRequest.Create;
import gift.controller.product.dto.ProductRequest.Update;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

@ExtendWith(MockitoExtension.class)
@Sql("/truncate.sql")
public class ProductServiceTest {


    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private WishRepository wishRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("상품 등록")
    void save() {
        Category category = new Category(null, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(null, "product1", 1000, "product.jpg", category);
        Create request = new Create("product1", 1000, "image1.jpg", 1L, "option1",
            3);
        given(categoryRepository.findById(any())).willReturn(Optional.of(category));
        given(productRepository.save(any())).willReturn(product);

        productService.addProduct(request);

        then(categoryRepository).should().findById(any());
        then(productRepository).should().save(any());
    }

    @Test
    @DisplayName("상품 조회")
    void findById() {
        Category category = new Category(null, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(null, "product1", 1000, "product.jpg", category);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        productService.findProduct(any());

        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("전체 상품 조회")
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        given(productRepository.findAll((Pageable) any())).willReturn(Page.empty());

        productService.findAllProduct(pageable);

        then(productRepository).should().findAll(pageable);
    }

    @Test
    @DisplayName("상품 수정")
    void update() {
        Category category = new Category(null, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(null, "product1", 1000, "product.jpg", category);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        productService.updateProduct(1L, new Update("product", 1000, "update.jpg", 1L));

        then(productRepository).should().findById(any());
    }

    @Test
    @DisplayName("상품 삭제")
    void delete() {
        Category category = new Category(null, "상품", "red", "https://st.kakaocdn.net/category1.jpg",
            "상품 카테고리입니다.");
        Product product = new Product(1L, "product1", 1000, "product.jpg", category);
        Long productId = product.getId();
        given(productRepository.existsById(productId)).willReturn(true);
        given(productRepository.findById(productId)).willReturn(Optional.of(product));

        productService.deleteProduct(productId);

        then(productRepository).should().existsById(productId);
        then(productRepository).should().findById(productId);
        then(wishRepository).should().deleteByProductId(productId);
        then(productRepository).should().deleteById(productId);
    }
}
