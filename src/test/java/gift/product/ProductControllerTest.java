package gift.product;

import static gift.exception.ErrorMessage.PRODUCT_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.PRODUCT_NAME_KAKAO_STRING;
import static gift.exception.ErrorMessage.PRODUCT_NAME_LENGTH;
import static gift.exception.ErrorMessage.PRODUCT_NOT_FOUND;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.dto.CategoryRequestDTO;
import gift.category.dto.CategoryResponseDTO;
import gift.product.dto.ProductRequestDTO;
import gift.product.dto.ProductResponseDTO;
import gift.token.JwtProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    // WebConfig에 의해, jwtProvider 생성 필요
    @MockBean
    private JwtProvider jwtProvider;

    @Autowired
    private ObjectMapper objectMapper;

    private final static String apiUrl = "/api/products";

    @Test
    @DisplayName("[Unit] getAllProduct method test")
    void getAllProductsTest() throws Exception {
        // given
        Page<ProductResponseDTO> expect = new PageImpl<>(
            List.of(
                new ProductResponseDTO(
                    1L,
                    "product1",
                    100,
                    "product1-imageUrl",
                    new CategoryResponseDTO(1L, "category-1")
                ),
                new ProductResponseDTO(
                    2L,
                    "Product2",
                    200,
                    "product2-image-url",
                    new CategoryResponseDTO(2L, "category-2")
                )
            )
        );

        //when
        when(productService.getAllProducts(PageRequest.of(0, 10)))
            .thenReturn(expect);

        //then
        mockMvc.perform(get(apiUrl)
                .param("page", "0")
                .param("size", "10")
            ).andExpect(status().isOk())
            .andExpect(content().string(objectMapper.writeValueAsString(expect)));
    }

    @Nested
    @DisplayName("[Unit] add product test")
    class addProductTest {

        @Test
        @DisplayName("success")
        void addProductTest() throws Exception {
            //given
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "product",
                100,
                "product-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when & then
            mockMvc.perform(post(apiUrl)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO))
                ).andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product name length error")
        void addProductNameLengthError() throws Exception {
            //given
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "ThisSequenceIsTooLongForProductName",
                300,
                "ThisSequenceIsTooLongForProductName-image-url",
                new CategoryRequestDTO("category-1")
            );

            doThrow(new IllegalArgumentException(PRODUCT_NAME_LENGTH))
                .when(productService)
                .addProduct(productRequestDTO);

            // when & then
            mockMvc.perform(post(apiUrl)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_LENGTH));
        }

        @Test
        @DisplayName("kakao name error")
        void addProductKakaoStringError() throws Exception {
            //given
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "kakaoProduct",
                100,
                "kakaoProduct-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NAME_KAKAO_STRING))
                .when(productService)
                .addProduct(productRequestDTO);

            //then
            mockMvc.perform(post(apiUrl)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_KAKAO_STRING));
        }

        @Test
        @DisplayName("special character error")
        void addProductSpecialCharacterError() throws Exception {
            //given
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "Special😀",
                200,
                "SpecialCharacter-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NAME_ALLOWED_CHARACTER))
                .when(productService)
                .addProduct(productRequestDTO);

            //then
            mockMvc.perform(post(apiUrl)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO))
                ).andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_ALLOWED_CHARACTER));
        }
    }

    @Nested
    @DisplayName("[Unit] update product test")
    class updateProductTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "product",
                100,
                "product-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when & then
            mockMvc.perform(patch(apiUrl + "/" + productId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product name length error")
        void updateProductNameLengthError() throws Exception {
            //given
            long productId = 1L;
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "ThisSequenceIsTooLongForProductName",
                300,
                "ThisSequenceIsTooLongForProductName-image-url",
                new CategoryRequestDTO("category-1")
            );

            doThrow(new IllegalArgumentException(PRODUCT_NAME_LENGTH))
                .when(productService)
                .updateProduct(productId, productRequestDTO);

            // when & then
            mockMvc.perform(patch(apiUrl + "/" + productId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_LENGTH));
        }

        @Test
        @DisplayName("kakao name error")
        void updateProductKakaoStringError() throws Exception {
            //given
            long productId = 1L;
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "kakaoProduct",
                100,
                "kakaoProduct-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NAME_KAKAO_STRING))
                .when(productService)
                .updateProduct(productId, productRequestDTO);

            //then
            mockMvc.perform(patch(apiUrl + "/" + productId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_KAKAO_STRING));
        }

        @Test
        @DisplayName("special character error")
        void updateProductSpecialCharacterError() throws Exception {
            //given
            long productId = 1L;
            ProductRequestDTO productRequestDTO = new ProductRequestDTO(
                "Special😀",
                200,
                "SpecialCharacter-image-url",
                new CategoryRequestDTO("category-1")
            );

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NAME_ALLOWED_CHARACTER))
                .when(productService)
                .updateProduct(productId, productRequestDTO);

            //then
            mockMvc.perform(patch(apiUrl + "/" + productId)
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(productRequestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NAME_ALLOWED_CHARACTER));
        }
    }

    @Nested
    @DisplayName("[Unit] delete product test")
    class deleteProductTest {

        @Test
        @DisplayName("success")
        void success() throws Exception {
            //given
            long productId = 1L;

            //when & then
            mockMvc.perform(delete(apiUrl + "/" + productId))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
        }

        @Test
        @DisplayName("product not found error")
        void productNotFoundError() throws Exception {
            //given
            long productId = 1L;

            //when
            doThrow(new IllegalArgumentException(PRODUCT_NOT_FOUND))
                .when(productService)
                .deleteProduct(productId);

            //then
            mockMvc.perform(delete(apiUrl + "/" + productId))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(PRODUCT_NOT_FOUND));
        }
    }
}