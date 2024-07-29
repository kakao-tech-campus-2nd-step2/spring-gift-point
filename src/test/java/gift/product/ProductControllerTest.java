package gift.product;

import static gift.exception.ErrorMessage.PRODUCT_NAME_ALLOWED_CHARACTER;
import static gift.exception.ErrorMessage.PRODUCT_NAME_KAKAO_STRING;
import static gift.exception.ErrorMessage.PRODUCT_NAME_LENGTH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.Category;
import gift.category.CategoryDTO;
import gift.token.JwtProvider;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        Product product1 = new Product(
            1L,
            "Product1",
            100,
            "product1-image-url",
            new Category(1L, "category-1")
        );
        Product product2 = new Product(
            2L,
            "Product2",
            200,
            "product2-image-url",
            new Category(2L, "category-2")
        );
        List<Product> products = Arrays.asList(product1, product2);

        //when
        Mockito.when(productService.getAllProducts()).thenReturn(products);

        //then
        mockMvc.perform(get(apiUrl))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(objectMapper.writeValueAsString(products)));
    }

    @ParameterizedTest
    @DisplayName("[Unit] addProduct test")
    @MethodSource(value = "addProductTestValues")
    void addProductTest(
        ProductDTO productDTO,
        String errorMessage,
        HttpStatus httpStatus
    ) throws Exception {
        mockMvc.perform(post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO))
            ).andExpect(status().is(httpStatus.value()))
            .andExpect(content().string(errorMessage));
    }

    private static Stream<Arguments> addProductTestValues() {
        return Stream.of(
            Arguments.of(
                new ProductDTO(
                    "product1",
                    100,
                    "product1-image-url",
                    new CategoryDTO("category-1")
                ),
                "",
                HttpStatus.OK
            ),
            Arguments.of(
                new ProductDTO(
                    "kakaoProduct",
                    100,
                    "kakaoProduct-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_KAKAO_STRING,
                HttpStatus.BAD_REQUEST
            ),
            Arguments.of(
                new ProductDTO(
                    "Special😀",
                    200,
                    "SpecialCharacter-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_ALLOWED_CHARACTER,
                HttpStatus.BAD_REQUEST
            ),
            Arguments.of(
                new ProductDTO(
                    "ThisSequenceIsTooLongForProductName",
                    300,
                    "ThisSequenceIsTooLongForProductName-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_LENGTH,
                HttpStatus.BAD_REQUEST
            )
        );
    }

    @ParameterizedTest
    @DisplayName("[Unit] updateProduct test")
    @MethodSource("updateProductTest")
    void updateProductTest(
        long id,
        ProductDTO productDTO,
        String errorMessage,
        HttpStatus httpStatus
    ) throws Exception {
        mockMvc.perform(patch(apiUrl + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productDTO))
            ).andExpect(status().is(httpStatus.value()))
            .andExpect(content().string(errorMessage));
    }

    private static Stream<Arguments> updateProductTest() {
        return Stream.of(
            Arguments.of(
                1L,
                new ProductDTO(
                    "product1",
                    100,
                    "product1-image-url",
                    new CategoryDTO("category-1")
                ),
                "",
                HttpStatus.OK
            ),
            Arguments.of(
                2L,
                new ProductDTO(
                    "kakaoProduct",
                    100,
                    "kakaoProduct-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_KAKAO_STRING,
                HttpStatus.BAD_REQUEST
            ),
            Arguments.of(
                3L,
                new ProductDTO(
                    "Special😀",
                    200,
                    "SpecialCharacter-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_ALLOWED_CHARACTER,
                HttpStatus.BAD_REQUEST
            ),
            Arguments.of(
                4L,
                new ProductDTO(
                    "ThisSequenceIsTooLongForProductName",
                    300,
                    "ThisSequenceIsTooLongForProductName-image-url",
                    new CategoryDTO("category-1")
                ),
                PRODUCT_NAME_LENGTH,
                HttpStatus.BAD_REQUEST
            )
        );
    }

    @Test
    @DisplayName("[Unit] deleteProduct test")
    void deleteProduct() throws Exception {
        mockMvc.perform(delete(apiUrl + "/" + 1L))
            .andExpect(status().isOk())
            .andExpect(content().string(""));
    }
}