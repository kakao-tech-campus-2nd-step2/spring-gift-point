package gift.service;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.AddOptionRequest;
import gift.dto.request.AddProductRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.MessageResponse;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.persistence.PersistenceException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static gift.constant.SuccessMessage.ADD_SUCCESS_MSG;
import static gift.constant.SuccessMessage.UPDATE_SUCCESS_MSG;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private OptionRepository optionRepository;
    private ProductService productService;
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        optionRepository = mock(OptionRepository.class);
        optionService = new OptionService(optionRepository);
        productService = new ProductService(productRepository, categoryRepository, optionService);
    }

    @Test
    void addProduct() {
        //given
        AddProductRequest addProductRequest = new AddProductRequest("newProduct", 500, "image.image", "뷰티", "newOption", 5);
        Category category = new Category(1L, "뷰티");
        Option option = new Option(addProductRequest);

        given(categoryRepository.findByName(any())).willReturn(Optional.of(category));
        given(productRepository.save(any())).willReturn(new Product(addProductRequest, category, option));
        given(optionRepository.save(any())).willReturn(new Option(addProductRequest));

        // when
        MessageResponse successMsg = productService.addProduct(addProductRequest);

        // then
        Assertions.assertThat(successMsg).isEqualTo(new MessageResponse(ADD_SUCCESS_MSG));
    }

    @Test
    void updateProductCategory() {
        // given
        Product product = new Product(1L, "name", 500, "image.image");
        Category category1 = new Category(1L, "상품권");
        product.setCategory(category1);

        Category category2 = new Category(2L, "뷰티");
        Long requestId = 1L;
        UpdateProductRequest updateProductRequest = new UpdateProductRequest("name", 500, "image.image", "뷰티");

        given(productRepository.findProductById(any())).willReturn(Optional.of(product));
        given(categoryRepository.findByName(any())).willReturn(Optional.of(category2));

        // when
        MessageResponse successMsg = productService.updateProduct(requestId, updateProductRequest);

        // then
        Assertions.assertThat(successMsg).isEqualTo(new MessageResponse(UPDATE_SUCCESS_MSG));
    }

    @Test
    void addProductOption_SameOptionName() {
        // given
        Product product = new Product(1L, "name", 500, "image.image");
        Category category1 = new Category(1L, "상품권");
        Option option = new Option("optionName", 100, product);
        product.setCategory(category1);
        product.setOption(option);

        Long requestId = 1L;
        AddOptionRequest addOptionRequest = new AddOptionRequest("optionName", 5);

        given(productRepository.findProductById(any())).willReturn(Optional.of(product));
        given(optionRepository.findAllByProduct(any())).willReturn(List.of(option));
        given(optionRepository.save(any())).willThrow(new PersistenceException());

        // when
        // then
        assertThrows(PersistenceException.class, () -> {
            productService.addOption(requestId, addOptionRequest);
        });
    }
}