package gift.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CategoryDto;
import gift.model.Category;
import gift.model.Product;
import gift.security.LoginMemberArgumentResolver;
import gift.service.CategoryService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({RestDocumentationExtension.class, SpringExtension.class})
@WebMvcTest({CategoryController.class})
@AutoConfigureRestDocs(outputDir = "target/snippets")
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;


    @MockBean
    private LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    private Category category;
    private CategoryDto categoryDto;
    private Product product;
    private List<CategoryDto> categoryDtoArrayList = new ArrayList<CategoryDto>();

    @BeforeEach
    void setUp(RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation))
            .alwaysDo(document("{method-name}",
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())))
            .build();

        category = new Category("상품권");
        category.setId(1L);
        product = new Product(1L, "상품", 10000, "image.jpg", category);
        var productNameList = new ArrayList<String>();
        productNameList.add(product.getName());
        categoryDto = new CategoryDto(category.getId(), category.getName(), productNameList);
        categoryDtoArrayList.add(categoryDto);
    }

    @Test
    void getAllCategoriesTest() throws Exception {
        // given
        given(categoryService.getAllCategories()).willReturn(categoryDtoArrayList);

        // when & then
        mockMvc.perform(get("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/get-categories", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void getCategoryByIdTEst() throws Exception {
        // given
        given(categoryService.getCategoryById(1L)).willReturn(category);

        // when & then
        mockMvc.perform(get("/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap())
            .andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value(containsString("name='상품권'")))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/get-category", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void addCategoryTest() throws Exception {
        // given
        Category savedCategory = new Category(1L, "상품권", new ArrayList<>());
        given(categoryService.addCategory(any(Category.class))).willReturn(savedCategory);

        // when, then
        mockMvc.perform(post("/add/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category))).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value(savedCategory.toString()))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/add-category", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void updateCategoryTest() throws Exception {
        // given
        var categoryDto = new CategoryDto(1L, "새상품권", Collections.singletonList("productName"));
        given(categoryService.updateCategory(any(Long.class), any(Category.class))).willReturn(
            categoryDto);

        // when & then
        mockMvc.perform(put("/update/category").param("id", String.valueOf(categoryDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value(categoryDto.toString()))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/update-category", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }

    @Test
    void deleteCategoryTest() throws Exception {
        // given
        willDoNothing().given(categoryService).deleteCategoryById(any(Long.class));

        // when & then
        mockMvc.perform(delete("/delete/category")
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("삭제 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/delete-category", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태")
            )));
    }


}