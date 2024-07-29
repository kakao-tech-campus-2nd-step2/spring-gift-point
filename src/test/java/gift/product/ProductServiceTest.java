package gift.product;

import static gift.exception.ErrorMessage.CATEGORY_NOT_FOUND;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import gift.category.Category;
import gift.category.CategoryDTO;
import gift.category.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

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
        List<Product> expect = List.of(
            new Product(1L, "product-1", 1, "product-1-image", new Category(1, "category-1")),
            new Product(2L, "product-2", 2, "product-2-image", new Category(1, "category-1")),
            new Product(3L, "product-3", 3, "product-3-image", new Category(2, "category-2"))
        );
        when(productRepository.findAll()).thenReturn(expect);
        //when
        List<Product> actual = productService.getAllProducts();

        //then
        assertEquals(expect, actual);
    }

    @Nested
    @DisplayName("[Unit] add product test")
    class addProductTest {

        @Test
        @DisplayName("success")
        void success() {
            //given
            ProductDTO productDTO = new ProductDTO(
                "product",
                1,
                "product-image",
                new CategoryDTO("category-1")
            );

            //when
            when(categoryRepository.findByName("category-1"))
                .thenReturn(Optional.of(new Category(1, "category-1")));

            //then
            assertDoesNotThrow(
                () -> productService.addProduct(productDTO)
            );
        }

        @Test
        @DisplayName("category not found error")
        void addCategoryNotFoundError() {
            //given
            ProductDTO productDTO = new ProductDTO(
                "product",
                1,
                "product-image",
                new CategoryDTO("category-1")
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
            ProductDTO productDTO = new ProductDTO("product", 1, "product-image",
                new CategoryDTO("category-1"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.category().getName()))
                .thenReturn(Optional.of(new Category(1L, "category-1")));

            when(productRepository.findById(id))
                .thenReturn(Optional.of(new Product(1L, "prev-product", 1, "prev-product-image",
                    new Category(1, "category-1"))));

            //then
            assertDoesNotThrow(
                () -> productService.updateProduct(id, productDTO)
            );
        }

        @Test
        @DisplayName("category not found error")
        void categoryNotFoundError() {
            //given
            ProductDTO productDTO = new ProductDTO("product", 1, "product-image",
                new CategoryDTO("wrong-category"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.category().getName()))
                .thenReturn(Optional.empty());
            when(productRepository.findById(id))
                .thenReturn(Optional.of(new Product(1L, "prev-product", 1, "prev-product-image",
                    new Category(1L, "category-1"))));

            //then
            assertThatThrownBy(() -> productService.updateProduct(id, productDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(CATEGORY_NOT_FOUND);
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() {
            //given
            ProductDTO productDTO = new ProductDTO("product", 1, "product-image",
                new CategoryDTO("category-1"));
            long id = 1L;

            //when
            when(categoryRepository.findByName(productDTO.category().getName()))
                .thenReturn(Optional.of(new Category(1L, "category-1")));
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
                            new Category(1L, "category-1")
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