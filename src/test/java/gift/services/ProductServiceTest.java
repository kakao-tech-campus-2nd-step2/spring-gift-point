//package gift.services;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import gift.classes.Exceptions.ProductException;
//import gift.domain.Category;
//import gift.domain.Product;
//import gift.dto.CategoryDto;
//import gift.dto.ProductDto;
//import gift.dto.RequestOptionDto;
//import gift.dto.RequestProductDto;
//import gift.repositories.CategoryRepository;
//import gift.repositories.OptionRepository;
//import gift.repositories.ProductRepository;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.NoSuchElementException;
//import java.util.Optional;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//class ProductServiceTest {
//
//    @Mock
//    private OptionService optionService;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private OptionRepository optionRepository;
//
//    @Mock
//    private CategoryRepository categoryRepository;
//
//    @InjectMocks
//    private ProductService productService;
//
//    private Product product;
//    private Category category;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        category = new Category(1L, "Test Category", "Blue", "CategoryImageUrl", "Test");
//        product = new Product("Test Product", 300, "ImageUrl", category);
//        product.setId(1L);
//    }
//
//    @Test
//    @DisplayName("제품 저장 테스트")
//    void testAddProduct() {
//        RequestProductDto requestProductDto = new RequestProductDto(1L,
//            "Test Product", 300, "imageUrl",
//            new CategoryDto(1L, "Test Category", "Blue", "CategoryImageUrl", "Test")
//        );
//
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//        when(categoryRepository.findByName(any())).thenReturn(category);
//
//        ProductDto result = productService.addProduct(requestProductDto);
//        assertEquals("Test Product", result.getName());
//        assertEquals(300, result.getPrice());
//        assertEquals("imageUrl", result.getImageUrl());
//        verify(productRepository).save(any(Product.class));
//    }
//
//
//    @Test
//    @DisplayName("제품 수정 테스트")
//    void testUpdateProduct() {
//        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
//
//        RequestProductDto requestProductDto = new RequestProductDto(1L, "Updated Test Product", 700,
//            "Updated ImageUrl",
//            new CategoryDto(1L, "Test Category", "Blue", "CategoryImageUrl", "Test"));
//
//        when(categoryRepository.findById(anyLong())).thenReturn(
//            Optional.of(new Category("Electronics", "Blue", "url", "description")));
//        when(productRepository.save(any(Product.class))).thenReturn(product);
//
//        ProductDto result = productService.updateProduct(requestProductDto);
//
//        assertEquals("Updated Test Product", result.getName());
//        assertEquals(700, result.getPrice());
//        assertEquals("Updated ImageUrl", result.getImageUrl());
//        verify(productRepository).findById(1L);
//    }
//
//    @Test
//    @DisplayName("제품 수정시, 제품ID 없을 경우 예외 테스트")
//    void testUpdateProduct_ProductNotFound() {
//        RequestProductDto requestProductDto = new RequestProductDto(1L,
//            "Test Product", 300, "imageUrl",
//            new CategoryDto(1L, "Test Category", "Blue", "CategoryImageUrl", "Test")
//        );
//
//        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
//
//        Exception exception = assertThrows(NoSuchElementException.class, () -> {
//            productService.updateProduct(requestProductDto);
//        });
//
//        assertEquals("Product not found with id 1", exception.getMessage());
//    }
//
//    @Test
//    @DisplayName("제품 삭제 테스트")
//    void testDeleteProduct() {
//        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
//        productService.deleteProduct(1L);
//        verify(productRepository).deleteById(1L);
//    }
//}