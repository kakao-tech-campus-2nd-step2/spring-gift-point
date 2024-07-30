package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.AddProductRequest;
import gift.dto.request.OptionRequest;
import gift.dto.request.UpdateProductRequest;
import gift.dto.response.AddedOptionIdResponse;
import gift.dto.response.AddedProductIdResponse;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import gift.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureRestDocs
@DisplayName("상품 컨트롤러 단위테스트")
class ProductControllerTest {

    private static final String URL = "/api/products";
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ProductService productService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("상품 조회")
    void getProducts() throws Exception {
        // Given
        List<ProductResponse> products = List.of(
                new ProductResponse(1L, "Product 1", 100, "img1.com", "Cloth"),
                new ProductResponse(2L, "Product 2", 200, "img2.com", "Food")
        );
        Page<ProductResponse> page = new PageImpl<>(products);
        when(productService.getProductResponses(any(Pageable.class))).thenReturn(page);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content").exists(),
                        jsonPath("$.content[1].name").value("Product 2")
                )
                .andDo(document("product-get",
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                responseFields(
                                        fieldWithPath("totalElements").description("사용 가능한 전체 요소 수"),
                                        fieldWithPath("totalPages").description("사용 가능한 전체 페이지 수"),
                                        fieldWithPath("first").description("이 페이지가 첫 페이지인지 여부를 나타내는 boolean 값"),
                                        fieldWithPath("last").description("이 페이지가 마지막 페이지인지 여부를 나타내는 boolean 값"),
                                        fieldWithPath("size").description("페이지당 요소 수"),
                                        fieldWithPath("content[]").description("상품 목록"),
                                        fieldWithPath("content[].id").description("상품의 ID"),
                                        fieldWithPath("content[].name").description("상품의 이름"),
                                        fieldWithPath("content[].price").description("상품의 가격"),
                                        fieldWithPath("content[].imageUrl").description("상품 이미지의 URL"),
                                        fieldWithPath("content[].categoryName").description("상품이 속한 카테고리"),
                                        fieldWithPath("number").description("현재 페이지 번호"),
                                        fieldWithPath("sort.empty").description("정렬이 비어 있는지 여부를 나타내는 boolean 값"),
                                        fieldWithPath("sort.sorted").description("결과가 정렬되어 있는지 여부를 나타내는 boolean 값"),
                                        fieldWithPath("sort.unsorted").description("결과가 정렬되지 않은 상태인지 여부를 나타내는 boolean 값"),
                                        fieldWithPath("numberOfElements").description("현재 페이지의 요소 수"),
                                        fieldWithPath("pageable").description("페이지네이션 정보"),
                                        fieldWithPath("empty").description("페이지가 비어 있는지 여부를 나타내는 boolean 값")
                                )
                        )
                );
    }

    @Test
    @DisplayName("상품 추가")
    void addProduct() throws Exception {
        // Given
        AddProductRequest addProductRequest = new AddProductRequest("Product1", 110, "img.com", 1L, List.of(new OptionRequest("option1", 100)));
        AddedProductIdResponse addedProductIdResponse = new AddedProductIdResponse(1L);

        when(productService.addProduct(addProductRequest)).thenReturn(addedProductIdResponse);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addProductRequest)))
                //Then
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("$.id").value(1L)
                )
                .andDo(document("product-post",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품의 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품의 가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지의 URL"),
                                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("상품이 속한 카테고리의 ID"),
                                        fieldWithPath("options").type(JsonFieldType.ARRAY).description("상품의 옵션 목록"),
                                        fieldWithPath("options[].name").type(JsonFieldType.STRING).description("옵션의 이름"),
                                        fieldWithPath("options[].quantity").type(JsonFieldType.NUMBER).description("옵션의 값")
                                ),
                                responseFields(
                                        fieldWithPath("id").description("추가된 상품 ID").type(JsonFieldType.NUMBER)
                                )
                        )
                );
    }


    @Test
    @DisplayName("상품 수정")
    void updateProduct() throws Exception {
        // Given
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(1L, "changeProduct1", 110, "img", 1L);

        // When
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                //Then
                .andExpect(status().isOk())
                .andDo(document("product-update",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                                requestFields(
                                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("상품의 ID"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("상품의 이름"),
                                        fieldWithPath("price").type(JsonFieldType.NUMBER).description("상품의 가격"),
                                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("상품 이미지의 URL"),
                                        fieldWithPath("categoryId").type(JsonFieldType.NUMBER).description("상품이 속한 카테고리의 ID")
                                )
                        )
                );
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() throws Exception {
        // Given
        Long deleteTargetId = 1L;

        // When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete(URL + "/{id}", deleteTargetId))
                //Then
                .andExpect(status().isOk())
                .andDo(document("product-delete",
                                pathParameters(
                                        parameterWithName("id").description("삭제하고 싶은 상품 ID")
                                )
                        )
                );
        verify(productService, times(1)).deleteProduct(deleteTargetId);
    }

    @Test
    @DisplayName("상품 옵션 조회")
    void getOptionResponses() throws Exception {
        //Given
        Long productId = 1L;
        List<OptionResponse> optionResponses = List.of(
                new OptionResponse(1L, "옵션1", 100),
                new OptionResponse(2L, "옵션1", 100)
        );

        when(productService.getOptionResponses(productId)).thenReturn(optionResponses);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .get(URL + "/{id}/options", 1L))
                //Then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("[0].name").value("옵션1"),
                        jsonPath("[0].id").value(1),
                        jsonPath("[1].name").value("옵션1"),
                        jsonPath("[1].id").value(2)
                )
                .andDo(document("product-option-get",
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("옵션을 조회 하고 싶은 상품 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("OptionResponse 객체의 배열"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("옵션의 ID"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("옵션의 이름"),
                                        fieldWithPath("[].quantity").type(JsonFieldType.NUMBER).description("옵션의 수량")
                                )
                        )
                );
    }

    @Test
    @DisplayName("상품 옵션 추가")
    void addOptionToProduct() throws Exception {
        //Given
        Long productId = 1L;
        OptionRequest optionRequest = new OptionRequest("옵션1", 9900);
        AddedOptionIdResponse addedOptionIdResponse = new AddedOptionIdResponse(1L);

        when(productService.addOptionToProduct(productId, optionRequest)).thenReturn(addedOptionIdResponse);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .post(URL + "/{id}/options", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(optionRequest)))
                //Then
                .andExpectAll(
                        status().isCreated(),
                        jsonPath("optionId").value(1)
                )
                .andDo(document("product-option-add",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                pathParameters(
                                        parameterWithName("id").description("옵션을 추가 하고 싶은 상품 ID")
                                ),
                                requestFields(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("옵션의 이름"),
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("옵션의 수량")
                                ),
                                responseFields(
                                        fieldWithPath("optionId").description("추가된 옵션 ID").type(JsonFieldType.NUMBER)
                                )
                        )
                );
    }
}
