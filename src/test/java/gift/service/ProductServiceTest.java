package gift.service;

import gift.category.entity.Category;
import gift.category.repository.CategoryRepository;
import gift.exception.DuplicateResourceException;
import gift.exception.ResourceNotFoundException;
import gift.product.dto.OptionRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.entity.Product;
import gift.product.repository.ProductRepository;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private ProductService productService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllProducts_ShouldReturnPageOfProductResponseDto() {
    // Given
    Long categoryId = 1L;
    Pageable pageable = PageRequest.of(0, 10);
    Category category = new Category();
    category.setId(categoryId);

    Product product1 = new Product();
    product1.setId(1L);
    product1.setName("Product 1");
    product1.setCategory(category);

    Product product2 = new Product();
    product2.setId(2L);
    product2.setName("Product 2");
    product2.setCategory(category);

    Page<Product> productPage = new PageImpl<>(Arrays.asList(product1, product2));

    when(categoryRepository.existsById(categoryId)).thenReturn(true);
    when(productRepository.findByCategoryId(eq(categoryId), any(Pageable.class))).thenReturn(productPage);

    // When
    Page<ProductResponseDto> result = productService.getAllProducts(categoryId, pageable);

    // Then
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals("Product 1", result.getContent().get(0).name());
    assertEquals("Product 2", result.getContent().get(1).name());
  }

  @Test
  void getAllProducts_ShouldThrowResourceNotFoundException_WhenCategoryNotFound() {
    // Given
    Long categoryId = 1L;
    Pageable pageable = PageRequest.of(0, 10);

    when(categoryRepository.existsById(categoryId)).thenReturn(false);

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> productService.getAllProducts(categoryId, pageable));
  }

  @Test
  void getProductById_ShouldReturnProductResponseDto() {
    // Given
    Long productId = 1L;
    Product product = new Product();
    product.setId(productId);
    product.setName("Test Product");

    Category category = new Category();
    category.setId(1L);
    product.setCategory(category);

    when(productRepository.findById(productId)).thenReturn(Optional.of(product));

    // When
    ProductResponseDto result = productService.getProductById(productId);

    // Then
    assertNotNull(result);
    assertEquals(productId, result.id());
    assertEquals("Test Product", result.name());
  }

  @Test
  void getProductById_ShouldThrowResourceNotFoundException_WhenProductNotFound() {
    // Given
    Long productId = 1L;

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(productId));
  }

  @Test
  void createProduct_ShouldReturnProductResponseDto() {
    // Given
    ProductRequestDto productRequestDto = new ProductRequestDto("New Product", 1000, "image.jpg", 1L);
    OptionRequestDto optionRequestDto = new OptionRequestDto("Option", 10);

    Category category = new Category();
    category.setId(1L);

    Product savedProduct = new Product();
    savedProduct.setId(1L);
    savedProduct.setName("New Product");
    savedProduct.setCategory(category);

    when(productRepository.existsByName(productRequestDto.name())).thenReturn(false);
    when(categoryRepository.findById(productRequestDto.categoryId())).thenReturn(Optional.of(category));
    when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

    // When
    ProductResponseDto result = productService.createProduct(productRequestDto, optionRequestDto);

    // Then
    assertNotNull(result);
    assertEquals("New Product", result.name());
    assertEquals(1L, result.id());
  }

  @Test
  void createProduct_ShouldThrowDuplicateResourceException_WhenProductNameExists() {
    // Given
    ProductRequestDto productRequestDto = new ProductRequestDto("Existing Product", 1000, "image.jpg", 1L);
    OptionRequestDto optionRequestDto = new OptionRequestDto("Option", 10);

    when(productRepository.existsByName(productRequestDto.name())).thenReturn(true);

    // When & Then
    assertThrows(DuplicateResourceException.class, () -> productService.createProduct(productRequestDto, optionRequestDto));
  }

  @Test
  void updateProduct_ShouldReturnUpdatedProductResponseDto() {
    // Given
    Long productId = 1L;
    ProductRequestDto productRequestDto = new ProductRequestDto("Updated Product", 2000, "new-image.jpg", 2L);

    Product existingProduct = new Product();
    existingProduct.setId(productId);
    existingProduct.setName("Old Product");

    Category newCategory = new Category();
    newCategory.setId(2L);

    when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
    when(productRepository.existsByName(productRequestDto.name())).thenReturn(false);
    when(categoryRepository.findById(productRequestDto.categoryId())).thenReturn(Optional.of(newCategory));
    when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

    // When
    ProductResponseDto result = productService.updateProduct(productId, productRequestDto);

    // Then
    assertNotNull(result);
    assertEquals("Updated Product", result.name());
    assertEquals(productId, result.id());
  }

  @Test
  void updateProduct_ShouldThrowResourceNotFoundException_WhenProductNotFound() {
    // Given
    Long productId = 1L;
    ProductRequestDto productRequestDto = new ProductRequestDto("Updated Product", 2000, "new-image.jpg", 2L);

    when(productRepository.findById(productId)).thenReturn(Optional.empty());

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> productService.updateProduct(productId, productRequestDto));
  }

  @Test
  void deleteProduct_ShouldDeleteProduct() {
    // Given
    Long productId = 1L;

    when(productRepository.existsById(productId)).thenReturn(true);

    // When
    productService.deleteProduct(productId);

    // Then
    verify(productRepository, times(1)).deleteById(productId);
  }

  @Test
  void deleteProduct_ShouldThrowResourceNotFoundException_WhenProductNotFound() {
    // Given
    Long productId = 1L;

    when(productRepository.existsById(productId)).thenReturn(false);

    // When & Then
    assertThrows(ResourceNotFoundException.class, () -> productService.deleteProduct(productId));
  }
}