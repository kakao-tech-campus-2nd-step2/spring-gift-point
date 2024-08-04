package gift.service;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class ProductServiceTest {
    private final ProductRepository productRepository = Mockito.mock(ProductRepository.class);
    private final CategoryRepository categoryRepository = Mockito.mock(CategoryRepository.class);
    private final OptionRepository optionRepository = Mockito.mock(OptionRepository.class);
    private final ProductService productService = new ProductService(productRepository, categoryRepository, optionRepository);

    @Test
    @DisplayName("모든 상품 페이지 조회 테스트")
    public void testGetProducts() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductDto> result = productService.getProducts(pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("카테고리로 상품 페이지 조회 테스트")
    public void testGetProductsByCategory() {
        Pageable pageable = Mockito.mock(Pageable.class);
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));

        when(productRepository.findByCategoryId(anyLong(), any(Pageable.class))).thenReturn(productPage);

        Page<ProductDto> result = productService.getProductsByCategory("1", pageable);
        assertEquals(1, result.getTotalElements());
    }

    @Test
    @DisplayName("모든 상품 조회 테스트")
    public void testFindAll() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);

        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductDto> result = productService.findAll();
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("ID로 상품 조회 테스트")
    public void testFindById() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Optional<ProductDto> result = productService.findById(1L);
        assertTrue(result.isPresent());
    }

    @Test
    @DisplayName("상품 저장 테스트")
    public void testSave() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        ProductDto productDto = new ProductDto(null, "Product1", 1000, "img1", 1L, Collections.emptyList());

        when(categoryRepository.findById(any(Long.class))).thenReturn(Optional.of(category));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> {
            Product savedProduct = invocation.getArgument(0);
            // Using a proxy to simulate ID assignment
            Product proxyProduct = new Product(savedProduct.getName(), savedProduct.getPrice(), savedProduct.getImgUrl(), savedProduct.getCategory()) {
                @Override
                public Long getId() {
                    return 1L;
                }
            };
            return proxyProduct;
        });

        Long result = productService.save(productDto);
        assertNotNull(result);
    }

    @Test
    @DisplayName("상품 업데이트 테스트")
    public void testUpdate() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        ProductDto productDto = new ProductDto(null, "UpdatedProduct", 2000, "img2", 1L, Collections.emptyList());

        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        productService.update(1L, productDto);
        assertEquals("UpdatedProduct", product.getName());
    }

    @Test
    @DisplayName("상품 삭제 테스트")
    public void testDelete() {
        doNothing().when(productRepository).deleteById(anyLong());

        productService.delete(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("상품 옵션 조회 테스트")
    public void testGetProductOption() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        Option option = Mockito.mock(Option.class);

        when(option.getId()).thenReturn(1L);
        when(option.getName()).thenReturn("Option1");
        when(option.getQuantity()).thenReturn(10);

        product.addOption(option);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        OptionDto result = productService.getProductOption(1L, 1L);
        assertEquals("Option1", result.getName());
    }

    @Test
    @DisplayName("상품에 옵션 추가 테스트")
    public void testAddOptionToProduct() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        OptionDto optionDto = new OptionDto(null, "Option1", 10, 1L);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(new Option("Option1", product, 10));

        productService.addOptionToProduct(1L, optionDto);
        assertEquals(1, product.getOptions().size());
    }

    @Test
    @DisplayName("옵션 수량 감소 테스트")
    public void testSubtractOptionQuantity() {
        Category category = new Category("Category1", "Description1", "CreatedBy1", "UpdatedBy1");
        Product product = new Product("Product1", 1000, "img1", category);
        Option option = new Option("Option1", product, 10);
        product.addOption(option);

        when(optionRepository.findByIdAndProductId(anyLong(), anyLong())).thenReturn(Optional.of(option));

        productService.subtractOptionQuantity(1L, 1L, 5);
        assertEquals(5, option.getQuantity());
    }
}