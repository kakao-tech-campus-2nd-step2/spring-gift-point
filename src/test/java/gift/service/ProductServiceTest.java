package gift.service;

import static gift.util.constants.ProductConstants.INVALID_PRICE;
import static gift.util.constants.ProductConstants.PRODUCT_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.exception.product.InvalidProductPriceException;
import gift.exception.product.ProductNotFoundException;
import gift.model.Category;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private OptionRepository optionRepository;
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        categoryRepository = Mockito.mock(CategoryRepository.class);
        optionRepository = Mockito.mock(OptionRepository.class);
        productService = new ProductService(
            productRepository,
            categoryRepository,
            optionRepository
        );
    }

    @Test
    @DisplayName("모든 상품 조회 (페이지네이션 적용)")
    public void testGetAllProducts() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Test Product", 100, "test.jpg", category);
        Pageable pageable = PageRequest.of(0, 10);
        when(productRepository.findAll(pageable))
            .thenReturn(new PageImpl<>(List.of(product), pageable, 1));

        Page<ProductResponse> products = productService.getAllProducts(pageable);
        assertEquals(1, products.getTotalElements());
        assertEquals("Test Product", products.getContent().get(0).name());
    }

    @Test
    @DisplayName("상품 ID로 조회")
    public void testGetProductById() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Test Product", 100, "test.jpg", category);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse productResponse = productService.getProductById(1L);
        assertEquals("Test Product", productResponse.name());
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 조회")
    public void testGetProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.getProductById(1L);
        });

        assertEquals(PRODUCT_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("상품 추가")
    public void testAddProduct() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product product = new Product(1L, "Test Product", 100, "test.jpg", category);
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Test Product",
            100,
            "test.jpg",
            1L
        );
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse createdProduct = productService.addProduct(productCreateRequest);
        assertEquals("Test Product", createdProduct.name());
    }

    @Test
    @DisplayName("유효하지 않은 가격으로 상품 추가")
    public void testAddProductInvalidPrice() {
        ProductCreateRequest productCreateRequest = new ProductCreateRequest(
            "Test Product",
            -100,
            "test.jpg",
            1L
        );

        InvalidProductPriceException exception = assertThrows(
            InvalidProductPriceException.class,
            () -> {
                productService.addProduct(productCreateRequest);
            }
        );

        assertEquals(INVALID_PRICE, exception.getMessage());
    }

    @Test
    @DisplayName("상품 업데이트")
    public void testUpdateProduct() {
        Category category = new Category("Category", "#000000", "imageUrl", "description");
        Product existingProduct = new Product(1L, "Old Product", 100, "old.jpg", category);
        Product updatedProduct = new Product(1L, "Updated Product", 200, "updated.jpg", category);
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(
            "Updated Product",
            200,
            "updated.jpg",
            1L
        );

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductResponse result = productService.updateProduct(1L, productUpdateRequest);
        assertEquals("Updated Product", result.name());
        assertEquals(200, result.price());
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 업데이트")
    public void testUpdateProductNotFound() {
        ProductUpdateRequest productUpdateRequest = new ProductUpdateRequest(
            "Updated Product",
            200,
            "updated.jpg",
            1L
        );

        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(1L, productUpdateRequest);
        });

        assertEquals(PRODUCT_NOT_FOUND + 1, exception.getMessage());
    }

    @Test
    @DisplayName("상품 삭제")
    public void testDeleteProduct() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 상품 ID로 삭제")
    public void testDeleteProductNotFound() {
        when(productRepository.existsById(1L)).thenReturn(false);

        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class, () -> {
            productService.deleteProduct(1L);
        });

        assertEquals(PRODUCT_NOT_FOUND + 1, exception.getMessage());
    }
}
