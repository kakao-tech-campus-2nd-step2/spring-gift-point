package gift.restdocs.category;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.auth.OAuthService;
import gift.config.LoginWebConfig;
import gift.controller.CategoryApiController;
import gift.model.Category;
import gift.request.CategoryAddRequest;
import gift.request.CategoryUpdateRequest;
import gift.response.category.CategoryResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;

@WebMvcTest(value = CategoryApiController.class,
    excludeFilters = {@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = LoginWebConfig.class)})
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class RestDocsCategoryTest extends AbstractRestDocsTest {

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtTokenProvider tokenProvider;
    @MockBean
    private OAuthService oAuthService;
    @MockBean
    private CategoryService categoryService;

    private String token = "{ACCESS_TOKEN}";

    @Test
    void getAllCategories() throws Exception {
        //given
        int dataCounts = 5;
        List<CategoryResponse> categories = new ArrayList<>();
        LongStream.range(0, dataCounts)
            .forEach(i -> {
                Category category = new Category(i + 1, "카테고리 " + (i + 1) , "color", "imageUrl", "description");
                categories.add(CategoryResponse.createCategoryResponse(category));
            });

        given(categoryService.getAllCategories())
            .willReturn(categories);

        //when then
        mockMvc.perform(get("/api/categories")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-category-test/get-all-categories",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                )));
    }

    @Test
    void getCategory() throws Exception {
        //given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "카테고리", "color", "imageUrl", "description");
        CategoryResponse categoryResponse = CategoryResponse.createCategoryResponse(category);
        given(categoryService.getCategory(categoryId))
            .willReturn(categoryResponse);

        //when then
        mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                    .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(equalTo(categoryId.intValue())))
            .andDo(document("rest-docs-category-test/get-category",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("categoryId").description("Category id")
                )));
    }

    @Test
    void addCategory() throws Exception {
        //given
        CategoryAddRequest categoryAddRequest = new CategoryAddRequest("카테고리", "color", "imageUrl", "description");
        String content = objectMapper.writeValueAsString(categoryAddRequest);
        Category category = new Category(1L, categoryAddRequest.name());

        given(categoryService.addCategory(any(String.class), any(String.class),
            any(String.class), any(String.class)))
            .willReturn(category);

        //when //then
        mockMvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(document("rest-docs-category-test/add-category",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                )));
    }

    @Test
    void updateCategory() throws Exception {
        //given
        Long savedCategoryId = 1L;
        Category savedCategory = new Category(savedCategoryId, "카테고리", "color", "imageUrl", "description");
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest(
            "수정된 카테고리명", "수정된 컬러", "수정된 주소", "수정된 설명");
        String content = objectMapper.writeValueAsString(updateRequest);
        given(categoryService.updateCategory(savedCategoryId, updateRequest.name(), updateRequest.color(),
            updateRequest.imageUrl(), updateRequest.description()))
            .willReturn(savedCategory);

        //when //then
        mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/categories/{categoryId}", savedCategoryId)
                    .header("Authorization", "Bearer " + token)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(document("rest-docs-category-test/update-category",
                requestHeaders(
                    headerWithName("Authorization").description("service access token")
                ),
                pathParameters(
                    parameterWithName("categoryId").description("Category id")
                )));
        then(categoryService).should().updateCategory(savedCategoryId, updateRequest.name(),
            updateRequest.color(), updateRequest.imageUrl(), updateRequest.description());
    }

    @Test
    void deleteCategory() throws Exception {
        //given
        Long categoryId = 1L;
        doNothing().when(categoryService).deleteCategory(categoryId);
        //when //then
        mockMvc.perform(delete("/api/categories?id=" + categoryId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isNoContent())
            .andDo(
                document("rest-docs-category-test/delete-category",
                    requestHeaders(
                        headerWithName("Authorization").description("service access token")
                    ),
                    queryParameters(
                        parameterWithName("id").description("Category id")
                    )));
    }


}
