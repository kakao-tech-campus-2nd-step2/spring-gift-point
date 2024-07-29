package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;

import gift.dto.betweenClient.product.ProductPostRequestDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.entity.Category;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ProductServiceTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @Mock
    Pageable pageable;

    @MockBean
    OptionService optionService;

    Category category1;
    Product product1;
    ProductPostRequestDTO productPostRequestDTO;
    ProductRequestDTO productPutRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productRepository.deleteAll();

        category1 = categoryRepository.save(new Category("테스트1", "#000000",
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                ""));

        product1 = productRepository.save(new Product("커피", 10000,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                categoryRepository.findByName("테스트1").get()));

        productPostRequestDTO = new ProductPostRequestDTO(1L, "커피", 1234,
                "https://st.kakaocdn.net/product/gift/product/20231010111814_9a667f9eccc943648797925498bdd8a3.jpg",
                category1.getName(), "옵션", 1234);

        productPutRequestDTO = new ProductRequestDTO(1L, "제품2", 1000, "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "기타");
    }


    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void addProductAndAddOptionAtomicTest() {
        doThrow(BadRequestException.class).when(optionService).addOption(any(), any());
        assertThrows(BadRequestException.class,
                () -> productService.addProduct(productPostRequestDTO));
        assertThat(productRepository.count()).isEqualTo(1);
    }

    @Test
    void addProduct() {
        productService.addProduct(productPostRequestDTO);

        assertThat(productRepository.count()).isEqualTo(2);
    }

    @Test
    void getProductList() {
        productService.addProduct(productPostRequestDTO);

        assertThat(productService.getProductList(pageable).getContent().getFirst()).isNotNull();
    }

    @Test
    void updateProduct() {
        productService.addProduct(productPostRequestDTO);

        productService.updateProduct(1L, productPutRequestDTO);

        assertThat(productRepository.findById(1L).get().getName()).isEqualTo("제품2");
    }

    @Test
    void deleteProduct() {
        productService.addProduct(productPostRequestDTO);
        assertThat(productRepository.count()).isEqualTo(2);
        productService.deleteProduct(1L);
        assertThat(productRepository.count()).isEqualTo(1);
    }

}