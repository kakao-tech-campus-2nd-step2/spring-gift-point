package gift.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import gift.controller.dto.OptionRequest;
import gift.controller.dto.OptionResponse;
import gift.controller.dto.ProductRequest;
import gift.controller.dto.ProductResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import gift.utils.error.CategoryNotFoundException;
import gift.utils.error.NotpermitNameException;
import gift.utils.error.ProductNotFoundException;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class GiftServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionService optionService;

    @InjectMocks
    private GiftService giftService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("getProduct 메서드 확인")
    void getProduct() {
        // Given
        Long productId = 1L;
        Category category = new Category(1L, "Test Category", "Red", "category.jpg", "Description");
        Product product = new Product("Test Product", 100.0, "image.jpg", category);
        product.setId(productId);

        when(productRepository.findByIdWithCategoryAndOption(productId)).thenReturn(Optional.of(product));

        // When
        ProductResponse response = giftService.getProduct(productId);

        // Then
        assertNotNull(response);
        assertEquals("Test Product", response.getName());
        assertEquals(100.0, response.getPrice());
        assertEquals("Test Category", response.getCategory().getName());
    }

    @Test
    @DisplayName("getAllProduct 메서드 확인")
    void getAllProduct() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Category category1 = new Category(1L, "Category 1", "Red", "cat1.jpg", "Description 1");
        Category category2 = new Category(2L, "Category 2", "Blue", "cat2.jpg", "Description 2");
        Product product1 = new Product("Product 1", 100.0, "image1.jpg", category1);
        Product product2 = new Product("Product 2", 200.0, "image2.jpg", category2);
        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAllWithCategoryAndOption(pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> responsePage = giftService.getAllProduct(pageable);

        // Then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());
        assertEquals("Product 1", responsePage.getContent().get(0).getName());
        assertEquals("Product 2", responsePage.getContent().get(1).getName());
    }

    @Test
    @DisplayName("postProducts 메서드 확인")
    void postProducts() {
        // Given
        ProductRequest productRequest = new ProductRequest("New Product", 150.0, "new-image.jpg", 1L);
        OptionRequest optionRequest = new OptionRequest("Option 1", 5);
        Category category = new Category(1L, "Test Category", "Blue", "category.jpg", "Description");
        Product savedProduct = new Product("New Product", 150.0, "new-image.jpg", category);
        savedProduct.setId(1L);

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductResponse response = giftService.postProducts(productRequest, optionRequest);

        // Then
        assertNotNull(response);
        assertEquals("New Product", response.getName());
        assertEquals(150.0, response.getPrice());
        assertEquals("Test Category", response.getCategory().getName());
        verify(optionService).addOption(eq(optionRequest), eq(1L));
    }

    @Test
    @DisplayName("deleteProducts 메서드 확인")
    void deleteProducts() {
        // Given
        Long productId = 1L;
        Category category = new Category(1L, "Test Category", "Red", "category-image.jpg", "Test Description");
        Product product = new Product("Test Product", 100.0, "test-image.jpg", category);
        product.setId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        ProductResponse response = giftService.deleteProducts(productId);

        // Then
        assertNotNull(response);
        assertEquals(productId, response.getId());
        assertEquals("Test Product", response.getName());
        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("getOption 메서드 확인")
    void getOption() {
        // Given
        Long productId = 1L;
        Product product = new Product("Test Product", 100.0, "image.jpg");
        product.setId(productId);
        product.addOption(new Option("Option 1", 10));
        product.addOption(new Option("Option 2", 20));

        when(productRepository.findByIdWithOption(productId)).thenReturn(Optional.of(product));

        // When
        List<OptionResponse> responses = giftService.getOption(productId);

        // Then
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Option 1", responses.get(0).getName());
        assertEquals("Option 2", responses.get(1).getName());
    }

    @Test
    @DisplayName("카카오가 포함된 상품명 예외 처리 확인")
    void postProductWithInvalidName() {
        // Given
        ProductRequest request = new ProductRequest("카카오 상품", 100.0, "image.jpg", 1L);
        OptionRequest optionRequest = new OptionRequest("Option", 5);

        // When & Then
        assertThrows(NotpermitNameException.class, () -> giftService.postProducts(request, optionRequest));
    }

    @Test
    @DisplayName("존재하지 않는 카테고리로 상품 등록 시 예외 처리 확인")
    void postProductWithNonExistentCategory() {
        // Given
        ProductRequest request = new ProductRequest("Valid Product", 100.0, "image.jpg", 999L);
        OptionRequest optionRequest = new OptionRequest("Option", 5);
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CategoryNotFoundException.class, () -> giftService.postProducts(request, optionRequest));
    }
}
