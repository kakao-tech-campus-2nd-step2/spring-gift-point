package gift.product.service;

import gift.category.domain.*;
import gift.category.service.CategoryService;
import gift.product.domain.*;
import gift.product.dto.ProductServiceDto;
import gift.product.exception.ProductNotFoundException;
import gift.product.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductServiceDto productServiceDto;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1L, new CategoryName("name"), new CategoryColor("color"), new CategoryImageUrl("imageUrl"), new CategoryDescription("description"));
        product = new Product(1L, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl"), category);
        productServiceDto = new ProductServiceDto(1L, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl"), 1L);
    }

    @Test
    void testGetAllProducts() {
        // given
        given(productRepository.findAll()).willReturn(List.of(product));

        // when
        List<Product> products = productService.getAllProducts();

        // then
        assertThat(products).hasSize(1);
        assertThat(products.get(0)).isEqualTo(product);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        Product foundProduct = productService.getProductById(product.getId());

        // then
        assertThat(foundProduct).isEqualTo(product);
        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetProductById_NotFound() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> productService.getProductById(product.getId()))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateProduct() {
        // given
        given(categoryService.getCategoryById(anyLong())).willReturn(category);
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Product createdProduct = productService.createProduct(productServiceDto);

        // then
        assertThat(createdProduct).isEqualTo(productServiceDto.toProduct(category));
        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));
        given(categoryService.getCategoryById(anyLong())).willReturn(category);
        given(productRepository.save(any(Product.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Product updatedProduct = productService.updateProduct(productServiceDto);

        // then
        assertThat(updatedProduct).isEqualTo(productServiceDto.toProduct(category));
        verify(productRepository, times(1)).findById(anyLong());
        verify(categoryService, times(1)).getCategoryById(anyLong());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_NotFound() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> productService.updateProduct(productServiceDto))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(0)).save(any(Product.class));
        verify(categoryService, times(0)).getCategoryById(anyLong());
    }

    @Test
    void testDeleteProduct() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.of(product));

        // when
        productService.deleteProduct(1L);

        // then
        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteProduct_NotFound() {
        // given
        given(productRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> productService.deleteProduct(1L))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository, times(1)).findById(anyLong());
        verify(productRepository, times(0)).deleteById(anyLong());
    }
}
