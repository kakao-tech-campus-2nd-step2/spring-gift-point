package gift.ServiceTest;

import gift.Entity.Product;
import gift.Entity.Wishlist;
import gift.Mapper.Mapper;
import gift.Model.ProductDto;
import gift.Repository.CategoryJpaRepository;
import gift.Repository.ProductJpaRepository;
import gift.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//JpaRepository의 메서드를 실행을 했느냐를 테스트
public class ProductServiceTest {

    private ProductJpaRepository productJpaRepository;
    private CategoryJpaRepository categoryJpaRepository;
    private Mapper mapper;
    private ProductService productService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        productJpaRepository = mock(ProductJpaRepository.class);
        categoryJpaRepository = mock(CategoryJpaRepository.class);
        mapper = mock(Mapper.class);

        productService = new ProductService(productJpaRepository, mapper);
    }

    @Test
    public void testGetAllProducts() {
        //given
        productService.getAllProducts();

        //then
        verify(productJpaRepository, times(1)).findByisDeletedFalse();

    }

    @Test
    public void testGetAllProductsByPage() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(Collections.emptyList());
        given(productJpaRepository.findByisDeletedFalse(pageable)).willReturn(page);

        // When
        productService.getAllProductsByPage(pageable);

        // Then
        verify(productJpaRepository, times(1)).findByisDeletedFalse(pageable);
    }

    @Test
    public void testGetProductById() {
        productService.getProductById(1L);

        verify(productJpaRepository, times(1)).findById(1L);

    }

    @Test
    public void testSaveProduct() {
        // Given
        ProductDto productDto = new ProductDto(1L, "product1", 1L, 1000, "http://localhost:8080/image1.jpg", false);
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg", false);
        given(mapper.productDtoToEntity(productDto)).willReturn(product);

        // When
        productService.saveProduct(productDto);

        // Then
        verify(productJpaRepository, times(1)).save(product);
    }

    @Test
    public void testUpdateProduct() {
        // Given
        ProductDto productDto = new ProductDto(1L, "product1", 1L, 1000, "http://localhost:8080/image1.jpg", false);
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg", false);
        given(mapper.productDtoToEntity(productDto)).willReturn(product);

        // When
        productService.updateProduct(productDto);

        // Then
        verify(productJpaRepository, times(1)).save(product);

    }

    @Test
    public void testDeleteProduct() {
        // Given
        Product product = new Product(1L, "product1", null, 1000, "http://localhost:8080/image1.jpg", false);
        given(productJpaRepository.findById(1L)).willReturn(Optional.of(product));

        // When
        productService.deleteProduct(1L);

        // Then
        verify(productJpaRepository, times(1)).findById(1L);
        verify(productJpaRepository, times(1)).save(product);
    }

}
