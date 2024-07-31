package gift.service;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("상품 목록 페이징 조회 테스트")
    public void testGetProducts() {
        // Given
        Product product = mock(Product.class);
        Page<Product> page = new PageImpl<>(Collections.singletonList(product));
        Pageable pageable = PageRequest.of(0, 10);

        when(productRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<Product> result = productService.getProducts(pageable);

        // Then
        assertEquals(1, result.getTotalElements());
        verify(productRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    public void testFindAll() {
        // Given
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        // When
        List<ProductDto> result = productService.findAll();

        // Then
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("ID로 상품 조회 테스트")
    public void testFindById() {
        // Given
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // When
        Optional<ProductDto> result = productService.findById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals("Test Product", result.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void testSave() {
        // Given
        List<OptionDto> options = Collections.emptyList(); // 빈 옵션 리스트를 추가
        ProductDto productDto = new ProductDto(null, "Test Product", 100, "http://example.com/image.jpg", 1L, options);
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);
        ReflectionTestUtils.setField(product, "id", 1L); // Set the ID for the product

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // When
        Long result = productService.save(productDto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result);
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 수정 테스트")
    public void testUpdate() {
        // Given
        List<OptionDto> options = Collections.emptyList(); // 빈 옵션 리스트를 추가
        ProductDto productDto = new ProductDto(null, "Updated Product", 150, "http://example.com/image_updated.jpg", 1L, options);
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // When
        productService.update(1L, productDto);

        // Then
        verify(categoryRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void testDelete() {
        // Given
        doNothing().when(productRepository).deleteById(anyLong());

        // When
        productService.delete(1L);

        // Then
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("상품 옵션 조회 테스트")
    public void testGetProductOptions() {
        // Given
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);
        Option option = new Option("Option 1", product, 10);
        product.addOption(option);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // When
        List<OptionDto> result = productService.getProductOptions(1L);

        // Then
        assertEquals(1, result.size());
        assertEquals("Option 1", result.get(0).getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("상품 옵션 수량 차감 테스트")
    public void testSubtractOptionQuantity() {
        // Given
        Category category = new Category("Electronics", "Blue", "http://example.com/category.jpg", "Category Description");
        Product product = new Product("Test Product", 100, "http://example.com/image.jpg", category);
        Option option = new Option("Option 1", product, 10);

        when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(option));

        // When
        productService.subtractOptionQuantity(1L, 1L, 5);

        // Then
        assertEquals(5, option.getQuantity());
        verify(optionRepository, times(1)).findByIdAndProductId(1L, 1L);
        verify(optionRepository, times(1)).save(option);
    }
}