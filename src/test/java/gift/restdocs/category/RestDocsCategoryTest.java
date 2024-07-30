package gift.restdocs.category;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.auth.JwtTokenProvider;
import gift.config.LoginWebConfig;
import gift.controller.CategoryApiController;
import gift.model.Category;
import gift.model.Product;
import gift.request.CategoryAddRequest;
import gift.request.CategoryUpdateRequest;
import gift.request.ProductUpdateRequest;
import gift.response.CategoryResponse;
import gift.restdocs.AbstractRestDocsTest;
import gift.service.CategoryService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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
    private CategoryService categoryService;

    private String token = "{ACCESS_TOKEN}";

    @Test
    void getAllCategories() throws Exception {
        //given
        int dataCounts = 5;
        List<CategoryResponse> categories = new ArrayList<>();
        LongStream.range(0, dataCounts)
            .forEach(i -> {
                Category category = new Category(i + 1, "카테고리 " + (i + 1));
                categories.add(CategoryResponse.createCategoryResponse(category));
            });

        given(categoryService.getAllCategories())
            .willReturn(categories);

        //when then
        mockMvc.perform(get("/api/categories")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    void getCategory() throws Exception {
        //given
        Long categoryId = 1L;
        Category category = new Category(categoryId, "카테고리");
        CategoryResponse categoryResponse = CategoryResponse.createCategoryResponse(category);
        given(categoryService.getCategory(categoryId))
            .willReturn(categoryResponse);

        //when then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/api/categories/{categoryId}", categoryId)
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(equalTo(categoryId.intValue())))
            .andDo(document("rest-docs-category-test/get-category",
                pathParameters(
                    parameterWithName("categoryId").description("Category id")
                )));
    }

    @Test
    void addCategory() throws Exception {
        //given
        CategoryAddRequest categoryAddRequest = new CategoryAddRequest("카테고리");
        String content = objectMapper.writeValueAsString(categoryAddRequest);
        Category category = new Category(1L, categoryAddRequest.name());

        given(categoryService.addCategory(any(String.class)))
            .willReturn(category);

        //when //then
        mockMvc.perform(post("/api/categories")
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andDo(print());
    }

    @Test
    void updateCategory() throws Exception {
        //given
        Long savedCategoryId = 1L;
        Category savedCategory = new Category(savedCategoryId, "카테고리");
        CategoryUpdateRequest updateRequest = new CategoryUpdateRequest(
            "수정된 카테고리명");
        String content = objectMapper.writeValueAsString(updateRequest);
        given(categoryService.updateCategory(savedCategoryId, updateRequest.name()))
            .willReturn(savedCategory);

        //when //then
        mockMvc.perform(RestDocumentationRequestBuilders.put("/api/categories/{categoryId}", savedCategoryId)
                .header("Authorization", "Bearer " + token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent())
            .andDo(document("rest-docs-category-test/update-category",
                pathParameters(
                    parameterWithName("categoryId").description("Category id")
                )));
        then(categoryService).should().updateCategory(savedCategoryId, updateRequest.name());
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
                    queryParameters(
                        parameterWithName("id").description("Category id")
                    )));
    }


}
