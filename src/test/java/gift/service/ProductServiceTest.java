package gift.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.List;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.CreateProductDto;
import gift.dto.UpdateProductDto;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private CreateProductDto createProductDto;
    private Product existingProduct;
    private UpdateProductDto updateProductDto;
    private Category category;

    private static final String CATEGORY_NAME = "Electronics";
    private static final String PRODUCTION_NAME = "Test Product";
    private static final Integer PRODUCT_PRICE = 100;
    private static final String PRODUCT_IMAGE_URL = "imageUrl";

    private static final Long PRODUCT_ID = 1L;
    private static final Long OPTION_ID = 1L;
    private static final Long NEW_OPTION_ID = 2L;

    private static final String EXISTING_PRODUCT_NAME = "Existing Product";
    private static final String EXISTING_OPTION_NAME = "Existing Option";

    private static final String UPDATED_PRODUCT_NAME = "Updated Product";
    private static final String UPDATED_OPTION_NAME = "Updated Option";
    private static final String NEW_OPTION_NAME = "New Option";
    private static final String NEW_IMAGE_URL = "newImageUrl";
    private static final Integer UPDATED_PRODUCT_PRICE = 150;

    @BeforeEach
    public void setUp() {
        // for createProduct test
        category = new Category(CATEGORY_NAME);
        createProductDto = new CreateProductDto(PRODUCTION_NAME, PRODUCT_PRICE, PRODUCT_IMAGE_URL, category);

        // for updateProduct test
        existingProduct = new Product(EXISTING_PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_IMAGE_URL, category);
        existingProduct.setId(PRODUCT_ID);
        Option existingOption = new Option(EXISTING_OPTION_NAME, existingProduct);
        existingOption.setId(OPTION_ID);
        existingProduct.setOptions(Collections.singletonList(existingOption));

        updateProductDto = new UpdateProductDto(UPDATED_PRODUCT_NAME,UPDATED_PRODUCT_PRICE,  NEW_IMAGE_URL, category);

        Option updateOption = new Option(UPDATED_OPTION_NAME, existingProduct);
        updateOption.setId(OPTION_ID);
        Option newOption = new Option(NEW_OPTION_NAME, existingProduct);
        newOption.setId(NEW_OPTION_ID);
        updateProductDto.setOptions(Arrays.asList(updateOption, newOption));
    }

    @Test
    public void testCreateProduct() {
        // Arrange
        Product product = createProductDto.toProduct();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Act
        Product createdProduct = productService.createProduct(createProductDto);

        // Assert
        verify(productRepository).save(any(Product.class));
        assertEquals(createProductDto.getName(), createdProduct.getName());
        assertEquals(createProductDto.getPrice(), createdProduct.getPrice());
        assertEquals(createProductDto.getImageUrl(), createdProduct.getImageUrl());
        assertEquals(createProductDto.getCategory(), createdProduct.getCategory());
    }

    @Test
    public void testUpdateProduct() {
        // Arrange
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(existingProduct);

        // Act
        Product updatedProduct = productService.updateProduct(PRODUCT_ID, updateProductDto);

        // Assert
        verify(productRepository).findById(PRODUCT_ID);
        verify(productRepository).save(existingProduct);

        assertEquals(updateProductDto.getName(), updatedProduct.getName());
        assertEquals(updateProductDto.getPrice(), updatedProduct.getPrice());
        assertEquals(updateProductDto.getImageUrl(), updatedProduct.getImageUrl());
        assertEquals(updateProductDto.getCategory(), updatedProduct.getCategory());

        List<Option> updatedOptions = updatedProduct.getOptions();
        assertEquals(NEW_OPTION_ID, updatedOptions.size());
        assertEquals(UPDATED_OPTION_NAME, updatedOptions.get(0).getName());
        assertEquals(NEW_OPTION_NAME, updatedOptions.get(1).getName());
    }
}
