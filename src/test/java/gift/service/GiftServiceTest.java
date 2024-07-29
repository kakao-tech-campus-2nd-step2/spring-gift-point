package gift.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import gift.controller.dto.OptionResponse;
import gift.controller.dto.ProductOptionRequest;
import gift.controller.dto.ProductRequest;
import gift.controller.dto.ProductResponse;
import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.CategoryRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class GiftServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;


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
        Product product = new Product("Test Product", 100.0, "image.jpg");
        Category category = new Category(1L, "Test Category", "Red", "category.jpg", "Description");
        Option option = new Option("Option 1", 10);
        product.setCategory(category);
        product.addOption(option);

        when(productRepository.findByIdWithCategoryAndOption(productId)).thenReturn(Optional.of(product));

        // When
        ProductResponse response = giftService.getProduct(productId);

        // Then
        assertNotNull(response);
        assertEquals("Test Product", response.getName());
        assertEquals(100.0, response.getPrice());
        assertEquals("Test Category", response.getCategory().getName());
        assertEquals(1, response.getOptionList().size());
    }

    @Test
    @DisplayName("getAllProduct 메서드 확인")
    void getAllProduct() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        Category category1 = new Category(1L, "Category 1", "Red", "cat1.jpg", "Description 1");
        Category category2 = new Category(2L, "Category 2", "Blue", "cat2.jpg", "Description 2");

        Product product1 = new Product("Product 1", 100.0, "image1.jpg");
        product1.setCategory(category1);
        product1.addOption(new Option("Option 1 for Product 1", 10));
        product1.addOption(new Option("Option 2 for Product 1", 20));

        Product product2 = new Product("Product 2", 200.0, "image2.jpg");
        product2.setCategory(category2);
        product2.addOption(new Option("Option 1 for Product 2", 15));

        List<Product> products = Arrays.asList(product1, product2);
        Page<Product> productPage = new PageImpl<>(products, pageable, products.size());

        when(productRepository.findAllWithCategoryAndOption(pageable)).thenReturn(productPage);

        // When
        Page<ProductResponse> responsePage = giftService.getAllProduct(pageable);

        // Then
        assertNotNull(responsePage);
        assertEquals(2, responsePage.getContent().size());

        ProductResponse response1 = responsePage.getContent().get(0);
        assertEquals("Product 1", response1.getName());
        assertEquals("Category 1", response1.getCategory().getName());
        assertEquals(2, response1.getOptionList().size());
        assertTrue(response1.getOptionList().stream().anyMatch(opt -> opt.getName().equals("Option 1 for Product 1")));
        assertTrue(response1.getOptionList().stream().anyMatch(opt -> opt.getName().equals("Option 2 for Product 1")));

        ProductResponse response2 = responsePage.getContent().get(1);
        assertEquals("Product 2", response2.getName());
        assertEquals("Category 2", response2.getCategory().getName());
        assertEquals(1, response2.getOptionList().size());
        assertTrue(response2.getOptionList().stream().anyMatch(opt -> opt.getName().equals("Option 1 for Product 2")));
    }

    @Test
    @DisplayName("PostProduct 메서드 확인")
    void postProducts() {
        // Given
        ProductRequest request = new ProductRequest("New Product", 150.0, "new-image.jpg", 1L);
        request.setOptions(Arrays.asList(new ProductOptionRequest("Option 1", 5)));

        Category category = new Category(1L, "Test Category", "Blue", "category.jpg", "Description");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product savedProduct = new Product("New Product", 150.0, "new-image.jpg");
        savedProduct.setCategory(category);
        savedProduct.addOption(new Option("Option 1", 5));
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductResponse response = giftService.postProducts(request);

        // Then
        assertNotNull(response);
        assertEquals("New Product", response.getName());
        assertEquals(150.0, response.getPrice());
        assertEquals("Test Category", response.getCategory().getName());
        assertEquals(1, response.getOptionList().size());
    }

    @Test
    @DisplayName("deleteProduct 메서드 확인")
    void deleteProducts() {
        // Given
        Long productId = 1L;
        Product product = new Product("Test Product", 100.0, "test-image.jpg");
        product.setId(productId);

        Category category = new Category(1L, "Test Category", "Red", "category-image.jpg", "Test Description");
        product.setCategory(category);

        Option option = new Option("Test Option", 10);
        product.addOption(option);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        // When
        Long deletedId = giftService.deleteProducts(productId);

        // Then
        assertEquals(productId, deletedId);
        verify(productRepository).deleteById(productId);
    }

    @Test
    @DisplayName("getOption 메서드 확인")
    void getOption() {
        // Given
        Long productId = 1L;
        Product product = new Product("Test Product", 100.0, "image.jpg");
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
}
