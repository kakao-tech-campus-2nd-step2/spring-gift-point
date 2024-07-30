package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.WishRequest;
import gift.dto.response.WishProductResponse;
import gift.interceptor.AuthInterceptor;
import gift.service.TokenService;
import gift.service.WishService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = WishController.class)
@AutoConfigureRestDocs
@DisplayName("위시 컨트롤러 단위테스트")
class WishControllerTest {

    private static final String URL = "/api/wishlist";
    @MockBean
    private TokenService tokenService;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @MockBean
    private AuthInterceptor authInterceptor;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private WishService wishService;

    @Test
    @DisplayName("위시리스트 상품 추가")
    void addProductToWishList() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        WishRequest request = new WishRequest(1L, 100);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validTokenValue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //Then
                .andExpect(
                        status().isCreated()
                )
                .andDo(document("wish-add",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                                requestHeaders(
                                        headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청")
                                ),
                                requestFields(
                                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("위시에 추가할 상품 ID"),
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("위시에 추가할 상품의 수량")

                                )
                        )
                );
    }

    @Test
    @DisplayName("위시리스트 상품 조회")
    void getWishProducts() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        Page<WishProductResponse> page = new PageImpl<>(List.of(
                new WishProductResponse(1L, "product1", 100, "img", 1000),
                new WishProductResponse(2L, "product2", 400, "img", 2000)
        ));
        when(wishService.getWishProductResponses(any(), any())).thenReturn(page);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                //Then
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.content[0].productName").value("product1"),
                        jsonPath("$.content[1].productName").value("product2")
                )
                .andDo(document("wish-get",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청").optional()
                                ),
                                responseFields(
                                        fieldWithPath("totalElements").type(JsonFieldType.NUMBER)
                                                .description("전체 요소 수"),

                                        fieldWithPath("totalPages").type(JsonFieldType.NUMBER)
                                                .description("전체 페이지 수"),

                                        fieldWithPath("first").type(JsonFieldType.BOOLEAN)
                                                .description("첫 페이지인지 여부를 나타내는 boolean 값"),

                                        fieldWithPath("last").type(JsonFieldType.BOOLEAN)
                                                .description("마지막 페이지인지 여부를 나타내는 boolean 값"),

                                        fieldWithPath("size").type(JsonFieldType.NUMBER)
                                                .description("페이지당 요소 수"),

                                        fieldWithPath("content").type(JsonFieldType.ARRAY)
                                                .description("상품 목록"),

                                        fieldWithPath("content[].productId").type(JsonFieldType.NUMBER)
                                                .description("상품의 ID"),

                                        fieldWithPath("content[].productName").type(JsonFieldType.STRING)
                                                .description("상품의 이름"),

                                        fieldWithPath("content[].productPrice").type(JsonFieldType.NUMBER)
                                                .description("상품의 가격"),

                                        fieldWithPath("content[].productImageUrl").type(JsonFieldType.STRING)
                                                .description("상품 이미지의 URL"),

                                        fieldWithPath("content[].productAmount").type(JsonFieldType.NUMBER)
                                                .description("상품의 수량"),

                                        fieldWithPath("number").type(JsonFieldType.NUMBER)
                                                .description("현재 페이지 번호"),

                                        fieldWithPath("sort.empty").type(JsonFieldType.BOOLEAN)
                                                .description("정렬이 비어 있는지 여부를 나타내는 boolean 값"),

                                        fieldWithPath("sort.sorted").type(JsonFieldType.BOOLEAN)
                                                .description("결과가 정렬되어 있는지 여부를 나타내는 boolean 값"),

                                        fieldWithPath("sort.unsorted").type(JsonFieldType.BOOLEAN)
                                                .description("결과가 정렬되지 않은 상태인지 여부를 나타내는 boolean 값"),

                                        fieldWithPath("pageable").type(JsonFieldType.STRING)
                                                .description("페이지네이션 정보"),

                                        fieldWithPath("numberOfElements").type(JsonFieldType.NUMBER)
                                                .description("현재 페이지의 요소 수"),

                                        fieldWithPath("empty").type(JsonFieldType.BOOLEAN)
                                                .description("페이지가 비어 있는지 여부를 나타내는 boolean 값")
                                )
                        )
                );
    }

    @Test
    @DisplayName("위시리스트 상품 수정")
    void updateWishProductAmount() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        WishRequest updateRequest = new WishRequest(1L, 1000);

        //When
        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                //Then
                .andExpect(
                        status().isOk()
                ).andDo(document("wish-update",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청").optional()
                                ),

                                requestFields(
                                        fieldWithPath("productId").type(JsonFieldType.NUMBER)
                                                .description("수정할 상품의 ID")
                                        ,
                                        fieldWithPath("quantity").type(JsonFieldType.NUMBER)
                                                .description("수정할 수량")
                                )
                        )
                );
    }

    @Test
    @DisplayName("위시리스트 상품 삭제")
    void deleteWishProduct() throws Exception {
        //Given
        when(authInterceptor.preHandle(any(), any(), any())).thenReturn(true);

        //When
        mockMvc.perform(RestDocumentationRequestBuilders
                        .delete(URL + "/{id}", 1))
                //Then
                .andExpect(
                        status().isOk()
                ).andDo(document("wish-delete",
                                Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                                Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
                                requestHeaders(
                                        headerWithName("Authorization").description("Authorization: Bearer ${ACCESS_TOKEN} +\n인증방식, 액세스 토큰으로 인증요청").optional()
                                ),
                                pathParameters(
                                        parameterWithName("id").description("삭제하고 싶은 위시 ID")
                                )
                        )
                );
    }
}
