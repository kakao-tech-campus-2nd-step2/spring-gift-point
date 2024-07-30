package gift.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.AddCategoryRequest;
import gift.dto.request.UpdateCategoryRequest;
import gift.dto.response.CategoryIdResponse;
import gift.dto.response.CategoryResponse;
import gift.service.CategoryService;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CategoryController.class)
@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@DisplayName("카테고리 컨트롤러 단위테스트")
class CategoryControllerTest {
    private static final String URL = "/api/categories";
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private JpaMetamodelMappingContext jpaMetamodelMappingContext;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext, RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation).snippets().withDefaults(httpRequest(), httpResponse(), requestBody(), responseBody()))
                .build();
    }

    @Test
    @DisplayName("카테고리 추가")
    void addCategory() throws Exception {
        //Given
        AddCategoryRequest addCategoryRequest = new AddCategoryRequest("색종이", "파란색", "https://pixabay.com/photos/cars-super-cars-luxury-cars-8891625/", "파란색 종이");
        CategoryIdResponse addedCategoryIdResponse = new CategoryIdResponse(1L);

        //When
        when(categoryService.addCategory(addCategoryRequest)).thenReturn(addedCategoryIdResponse);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addCategoryRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L)
                )
                .andDo(document("category-add",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        requestFields(
                                fieldWithPath("name").description("카테고리 이름").type(STRING),
                                fieldWithPath("color").description("카테고리 색깔").type(STRING),
                                fieldWithPath("imageUrl").description("카테고리 이미지주소").type(STRING),
                                fieldWithPath("description").description("카테고리 설명").type(STRING).optional()
                        ),
                        responseFields(
                                fieldWithPath("id").description("생성된 카테고리 ID").type(NUMBER)
                        )
                ));
    }

    @Test
    @DisplayName("모든 카테고리 조회")
    void getCategories() throws Exception {
        //Given
        List<CategoryResponse> categoryResponses = new ArrayList<>();
        categoryResponses.add(new CategoryResponse(1L, "아이패드", "화이트", "pineappleipad.com", "아이패드 화이트"));
        categoryResponses.add(new CategoryResponse(2L, "티셔츠", "회색", "image.com", "회색 티셔츠"));

        //When
        when(categoryService.getAllCategoryResponses()).thenReturn(categoryResponses);

        //Then
        mockMvc.perform(MockMvcRequestBuilders.get(URL)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("[0].id").value(1L),
                        jsonPath("[1].id").value(2L)
                )
                .andDo(document("category-get",
                        Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),

                        responseFields(
                                fieldWithPath("[]").description("The array of categories"),
                                fieldWithPath("[].id").description("카테고리 ID").type(NUMBER),
                                fieldWithPath("[].name").description("카테고리 이름").type(STRING),
                                fieldWithPath("[].color").description("카테고리 색").type(STRING),
                                fieldWithPath("[].imageUrl").description("카테고리 이미지 주소").type(STRING),
                                fieldWithPath("[].description").description("카테고리 설명").type(STRING)
                        )
                ));
    }

    @Test
    void updateCategory() throws Exception {
        //Given
        UpdateCategoryRequest updateCategoryRequest = new UpdateCategoryRequest(1L, "색종이", "노란색", "https://pixabay.com/photos/cars-super-cars-luxury-cars-8891625/", "노란색 종이");
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(UpdateCategoryRequest.class);
        List<String> id = constraintDescriptions.descriptionsForProperty("id");
        List<String> name = constraintDescriptions.descriptionsForProperty("name");
        List<String> color = constraintDescriptions.descriptionsForProperty("color");
        List<String> imageUrl = constraintDescriptions.descriptionsForProperty("imageUrl");

        //When Then
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateCategoryRequest)))
                .andExpectAll(
                        status().isOk()
                )
                .andDo(document("category-update",
                        Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),

                        requestFields(
                                fieldWithPath("id").description("카테고리 ID").type(NUMBER),
                                fieldWithPath("name").description("카테고리 이름").type(STRING),
                                fieldWithPath("color").description("카테고리 색").type(STRING),
                                fieldWithPath("imageUrl").description("카테고리 이미지주소").type(STRING),
                                fieldWithPath("description").description("카테고리 설명").type(STRING).optional()
                        )
                ));
    }

    @Test
    void deleteCategory() throws Exception {
        //When Then
        mockMvc.perform(RestDocumentationRequestBuilders.delete("/api/categories/{id}", 1L)
                )
                .andExpect(
                        status().isOk()
                )
                .andDo(document("category-delete",
                        pathParameters(
                                parameterWithName("id").description("삭제하고 싶은 카테고리 ID")
                        )
                ));
    }
}
