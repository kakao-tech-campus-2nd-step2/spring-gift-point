package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import gift.administrator.category.Category;
import gift.administrator.category.CategoryService;
import gift.administrator.option.Option;
import gift.administrator.product.Product;
import gift.administrator.product.ProductDTO;
import gift.administrator.product.ProductRepository;
import gift.administrator.product.ProductService;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository = mock(ProductRepository.class);
    private CategoryService categoryService = mock(CategoryService.class);

    @BeforeEach
    void beforeEach(){
        productService = new ProductService(productRepository, categoryService);
    }

    @Test
    @DisplayName("상품 전체 가져오기")
    void getAllProducts(){
        //given
        int page = 0;
        int size = 10;
        String sortBy = "name";
        Sort.Direction direction = Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Category category = new Category(1L, "도서", null, null, null);
        Option option = new Option("어린왕자", 3, null);

        Product product1 = new Product(1L, "Product1", 1000, "imageUrl1", category, null);
        option.setProduct(product1);
        product1.setOption(List.of(option));

        Product product2 = new Product(2L, "Product2", 2000, "imageUrl2", category, null);
        option.setProduct(product2);
        product2.setOption(List.of(option));

        ProductDTO productDTO1 = new ProductDTO(1L, "Product1", 1000, "imageUrl1", 1L, null);
        ProductDTO productDTO2 = new ProductDTO(2L, "Product2", 2000, "imageUrl2", 1L, null);

        List<Product> products = List.of(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());
        List<ProductDTO> expected = List.of(productDTO1, productDTO2);

        given(productRepository.findAll(pageable)).willReturn(productPage);

        //when
        Page<ProductDTO> result = productService.getAllProducts(page, size, sortBy, direction);

        //then
        assertThat(result.getTotalElements()).isEqualTo(expected.size());
        assertThat(result.getContent().get(0))
            .extracting(ProductDTO::getName, ProductDTO::getPrice, ProductDTO::getImageUrl, ProductDTO::getCategoryId)
            .containsExactly(expected.getFirst().getName(), expected.getFirst().getPrice(),
                expected.getFirst().getImageUrl(), expected.getFirst().getCategoryId());
        assertThat(result.getContent().get(1))
            .extracting(ProductDTO::getName, ProductDTO::getPrice, ProductDTO::getImageUrl, ProductDTO::getCategoryId)
            .containsExactly(expected.get(1).getName(), expected.get(1).getPrice(),
                expected.get(1).getImageUrl(), expected.get(1).getCategoryId());
    }
}
