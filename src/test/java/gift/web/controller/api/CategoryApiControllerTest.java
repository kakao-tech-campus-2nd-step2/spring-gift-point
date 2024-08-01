package gift.web.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.authentication.token.JwtProvider;
import gift.config.RestDocsConfiguration;
import gift.domain.Member;
import gift.domain.Member.Builder;
import gift.domain.vo.Email;
import gift.service.CategoryService;
import gift.web.dto.request.category.CreateCategoryRequest;
import gift.web.dto.request.category.UpdateCategoryRequest;
import gift.web.dto.response.category.CreateCategoryResponse;
import gift.web.dto.response.category.ReadAllCategoriesResponse;
import gift.web.dto.response.category.ReadCategoryResponse;
import gift.web.dto.response.category.UpdateCategoryResponse;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("test")
@SpringBootTest
@Import(RestDocsConfiguration.class)
@ExtendWith(RestDocumentationExtension.class)
class CategoryApiControllerTest {

    private MockMvc mockMvc;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private CategoryService categoryService;

    private String accessToken;

    private static final String BASE_URL = "/api/categories";

    @BeforeEach
    void setUp(
        final RestDocumentationContextProvider provider
    ) {
        mockMvc = MockMvcBuilders
            .standaloneSetup(new CategoryApiController(categoryService))
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
            .alwaysDo(restDocs)
            .build();

        Member member = new Builder().id(1L).name("회원01").email(Email.from("member01@gmail.com"))
            .build();
        accessToken = jwtProvider.generateToken(member).getValue();
    }


    @Test
    @DisplayName("카테고리 생성")
    void createCategory() throws Exception {
        CreateCategoryRequest request = new CreateCategoryRequest("카테고리01",
            "카테고리01 설명", "https://via.placeholder.com/150", "#FFFFFF");

        String content = objectMapper.writeValueAsString(request);

        given(categoryService.createCategory(any(CreateCategoryRequest.class)))
            .willReturn(new CreateCategoryResponse(1L, "카테고리01", "카테고리01 설명", "https://via.placeholder.com/150", "#FFFFFF"));

        mockMvc
            .perform(
                post(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("색상 코드")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("색상 코드")
                    )
                )
            );
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void readAllCategories() throws Exception {
        given(categoryService.readAllCategories(any()))
            .willReturn(new ReadAllCategoriesResponse(List.of(
                new ReadCategoryResponse(1L, "카테고리01", "카테고리01 설명", "https://via.placeholder.com/150", "#FFFFFF"),
                new ReadCategoryResponse(2L, "카테고리02", "카테고리02 설명", "https://via.placeholder.com/150", "#FFFFFF")
            )));

        mockMvc
            .perform(
                get(BASE_URL)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    queryParameters(
                        parameterWithName("page").optional().description("페이지 번호"),
                        parameterWithName("size").optional().description("페이지 크기"),
                        parameterWithName("sort").optional().description("정렬 조건")
                    ),
                    responseFields(
                        fieldWithPath("categories[].id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("categories[].name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("categories[].description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("categories[].imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("categories[].color").type(JsonFieldType.STRING).description("색상 코드")
                    )
                )
            );
    }

    @Test
    @DisplayName("단일 카테고리 조회")
    void readCategory() throws Exception {
        given(categoryService.readCategory(any(Long.class)))
            .willReturn(new ReadCategoryResponse(1L, "카테고리01", "카테고리01 설명", "https://via.placeholder.com/150", "#FFFFFF"));

        mockMvc
            .perform(
                get(BASE_URL + "/{id}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("색상 코드")
                    )
                )
            );
    }

    @Test
    @DisplayName("카테고리 수정")
    void updateCategory() throws Exception {
        UpdateCategoryRequest request = new UpdateCategoryRequest("카테고리01", "카테고리01 설명",
            "https://via.placeholder.com/150", "#FFFFFF");

        String content = objectMapper.writeValueAsString(request);

        given(categoryService.updateCategory(any(Long.class), any(UpdateCategoryRequest.class)))
            .willReturn(new UpdateCategoryResponse(1L, "카테고리01", "카테고리01 설명", "https://via.placeholder.com/150", "#FFFFFF"));

        mockMvc
            .perform(
                put(BASE_URL + "/{id}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andDo(
                restDocs.document(
                    requestFields(
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("색상 코드")
                    ),
                    responseFields(
                        fieldWithPath("id").type(JsonFieldType.NUMBER).description("카테고리 ID"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("카테고리명"),
                        fieldWithPath("description").type(JsonFieldType.STRING).description("카테고리 설명"),
                        fieldWithPath("imageUrl").type(JsonFieldType.STRING).description("이미지 URL"),
                        fieldWithPath("color").type(JsonFieldType.STRING).description("색상 코드")
                    )
                )
            );
    }

    @Test
    void deleteCategory() throws Exception {
        mockMvc
            .perform(
                delete(BASE_URL + "/{id}", 1L)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            )
            .andExpect(status().isNoContent())
            .andDo(
                restDocs.document()
            );
    }
}