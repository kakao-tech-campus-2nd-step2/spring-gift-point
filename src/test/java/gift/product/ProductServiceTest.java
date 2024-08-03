package gift.product;

import static gift.exception.ErrorMessage.CATEGORY_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import gift.category.CategoryRepository;
import gift.category.dto.CategoryRequestDTO;
import gift.category.entity.Category;
import gift.product.dto.ProductPaginationResponseDTO;
import gift.product.dto.ProductRequestDTO;
import gift.product.entity.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@SpringBootTest
class ProductServiceTest {

    @MockBean
    ProductRepository productRepository;

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    ProductService productService;

    @Test
    @DisplayName("[Unit] get all products test")
    void getAllProductsTest() {
        //given
        Page<Product> expectFromRepository = new PageImpl<>(List.of(
            new Product(
                1L,
                "product-1",
                1,
                "product-1-image",
                new Category(
                    1L,
                    "category-1",
                    "test-color",
                    "test-image-url",
                    "test-description"
                )
            ), new Product(
                2L,
                "product-2",
                2,
                "product-2-image",
                new Category(
                    1L,
                    "category-1",
                    "test-color",
                    "test-image-url",
                    "test-description"
                )
            )
        ));

        Category findCategory = new Category(
            1L,
            "category-1",
            "test-color",
            "test-image-url",
            "test-description"
        );

        Page<ProductPaginationResponseDTO> expect = new PageImpl<>(List.of(
            new ProductPaginationResponseDTO(1L, "product-1", 1, "product-1-image"),
            new ProductPaginationResponseDTO(2L, "product-2", 2, "product-2-image")
        ));

        PageRequest pageable = PageRequest.of(0, Integer.MAX_VALUE);

        //when
        when(productRepository.findAllByCategory(findCategory, pageable))
            .thenReturn(expectFromRepository);
        when(categoryRepository.findById(1L))
            .thenReturn(
                Optional.of(findCategory)
            );
        Page<ProductPaginationResponseDTO> actual = productService.getAllProducts(pageable, 1L);

        //then
        assertAll(
            () -> assertEquals(expect.getContent(), actual.getContent()),
            () -> assertEquals(expect.getTotalElements(), actual.getTotalElements()),
            () -> assertEquals(expect.getTotalPages(), actual.getTotalPages())
        );
    }

    @Nested
    @DisplayName("[Unit] add product test")
    class addProductTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            ProductRequestDTO productDTO = new ProductRequestDTO(
                "product",
                1,
                "product-image",
                new CategoryRequestDTO("category-1")
            );

            //when
            when(categoryRepository.findByName("category-1"))
                .thenReturn(Optional.of(new Category(
                    1L,
                    "category-1",
                    "test-color",
                    "test-image-url",
                    "test-description"
                )));

            //then
            assertDoesNotThrow(
                () -> productService.addProduct(productDTO)
            );
        }

        @Test
        @DisplayName("category not found error")
        void addCategoryNotFoundError() {
            //given
            ProductRequestDTO productDTO = new ProductRequestDTO(
                "product",
                1,
                "product-image",
                new CategoryRequestDTO("category-1")
            );

            //when
            when(categoryRepository.findByName("category-1"))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> productService.addProduct(productDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CATEGORY_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("[Unit] update product test")
    class updateProductTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            ProductRequestDTO productDTO = new ProductRequestDTO("product", 1, "product-image",
                new CategoryRequestDTO("category-1"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.getCategory().getName()))
                .thenReturn(Optional.of(new Category(
                    1L,
                    "category-1",
                    "test-color",
                    "test-image-url",
                    "test-description"
                )));

            when(productRepository.findById(id))
                .thenReturn(Optional.of(
                    new Product(
                        1L,
                        "prev-product",
                        1,
                        "prev-product-image",
                        new Category(
                            1L,
                            "category-1",
                            "test-color",
                            "test-image-url",
                            "test-description"
                        )
                    )
                ));

            //then
            assertDoesNotThrow(
                () -> productService.updateProduct(id, productDTO)
            );
        }

        @Test
        @DisplayName("category not found error")
        void categoryNotFoundError() {
            //given
            ProductRequestDTO productDTO = new ProductRequestDTO("product", 1, "product-image",
                new CategoryRequestDTO("wrong-category"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.getCategory().getName()))
                .thenReturn(Optional.empty());
            when(productRepository.findById(id))
                .thenReturn(Optional.of(new Product(
                    1L,
                    "prev-product",
                    1,
                    "prev-product-image",
                    new Category(
                        1L,
                        "category-1",
                        "test-color",
                        "test-image-url",
                        "test-description"
                    )
                )));

            //then
            assertThatThrownBy(() -> productService.updateProduct(id, productDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CATEGORY_NOT_FOUND);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //given
            ProductRequestDTO productDTO = new ProductRequestDTO("product", 1, "product-image",
                new CategoryRequestDTO("category-1"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.getCategory().getName()))
                .thenReturn(Optional.of(new Category(
                    1L,
                    "category-1",
                    "test-color",
                    "test-image-url",
                    "test-description"
                )));
            when(productRepository.findById(id))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> productService.updateProduct(id, productDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("[Unit] delete product test")
    class deleteProductTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            long id = 1L;

            //when
            when(productRepository.findById(id))
                .thenReturn(Optional.of(
                        new Product(
                            1L,
                            "product",
                            1,
                            "product-image",
                            new Category(
                                1L,
                                "category-1",
                                "test-color",
                                "test-image-url",
                                "test-description"
                            )
                        )
                    )
                );

            //then
            assertDoesNotThrow(() -> productService.deleteProduct(id));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //given
            long id = 1L;

            //when
            when(productRepository.findById(id))
                .thenReturn(Optional.empty());

            //then
            assertThatThrownBy(() -> productService.deleteProduct(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(PRODUCT_NOT_FOUND);
        }
    }
}