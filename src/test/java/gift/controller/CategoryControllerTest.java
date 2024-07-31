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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.category.controller.CategoryController;
import gift.category.dto.CategoryRequest;
import gift.category.model.Category;
import gift.product.model.Product;
import gift.security.LoginMemberArgumentResolver;
import gift.category.service.CategoryService;
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
    private CategoryRequest categoryRequest;
    private Product product;
    private List<CategoryRequest> categoryRequestArrayList = new ArrayList<CategoryRequest>();

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
        categoryRequest = new CategoryRequest(category.getId(), category.getName(),
            productNameList);
        categoryRequestArrayList.add(categoryRequest);
    }

    @Test
    void getAllCategoriesTest() throws Exception {
        // given
        given(categoryService.getAllCategories()).willReturn(categoryRequestArrayList);

        // when & then
        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("카테고리 전체 조회 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data[0].id").value(1))
            .andExpect(jsonPath("$.data[0].name").value("상품권"))
            .andExpect(jsonPath("$.data[0].productName[0]").value("상품"))
            .andExpect(jsonPath("$.data[0].color").isEmpty())
            .andExpect(jsonPath("$.data[0].imageUrl").isEmpty())
            .andExpect(jsonPath("$.data[0].description").isEmpty())
            .andDo(document("categories/get-categories",
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("카테고리 목록"),
                    fieldWithPath("data[].id").description("카테고리 ID"),
                    fieldWithPath("data[].name").description("카테고리 이름"),
                    fieldWithPath("data[].productName").description("카테고리에 속한 상품 이름 목록"),
                    fieldWithPath("data[].color").description("카테고리 색상").optional(),
                    fieldWithPath("data[].imageUrl").description("카테고리 이미지 URL").optional(),
                    fieldWithPath("data[].description").description("카테고리 설명").optional()
                )
            ));
    }

    @Test
    void getCategoryByIdTEst() throws Exception {
        // given
        given(categoryService.getCategoryById(1L)).willReturn(category);

        // when & then
        mockMvc.perform(get("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isMap())
            .andDo(print())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value(containsString("카테고리 검색 성공")))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/get-category", responseFields(
                fieldWithPath("result").description("API 호출 결과"),
                fieldWithPath("message").description("호출 내용"),
                fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                fieldWithPath("data.id").description("카테고리 ID"),
                fieldWithPath("data.name").description("카테고리명"),
                fieldWithPath("data.color").description("카테고리 색상").optional(),
                fieldWithPath("data.imageUrl").description("카테고리 이미지 URL").optional(),
                fieldWithPath("data.description").description("카테고리 설명").optional()
            )));
    }

    @Test
    void addCategoryTest() throws Exception {
        // given
        Category savedCategory = new Category(1L, "상품권", null);
        given(categoryService.addCategory(any(CategoryRequest.class))).willReturn(savedCategory);

        // when, then
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category))).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("카테고리 추가 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andExpect(jsonPath("$.data").value(savedCategory))
            .andDo(document("categories/add-category",
                requestFields(
                    fieldWithPath("id").description("카테고리 ID"),
                    fieldWithPath("name").description("카테고리명"),
                    fieldWithPath("color").description("색상"),
                    fieldWithPath("imageUrl").description("이미지주소"),
                    fieldWithPath("description").description("상세정보")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data.id").description("카테고리 ID"),
                    fieldWithPath("data.name").description("카테고리 이름"),
                    fieldWithPath("data.color").description("카테고리 색상").optional(),
                    fieldWithPath("data.imageUrl").description("카테고리 이미지 URL").optional(),
                    fieldWithPath("data.description").description("카테고리 설명").optional()
                )));
    }

    @Test
    void updateCategoryTest() throws Exception {
        // given
        var categoryDto = new CategoryRequest(1L, "새상품권", Collections.singletonList("productName"));
        given(categoryService.updateCategory(any(CategoryRequest.class))).willReturn(
            category);

        // when & then
        mockMvc.perform(put("/api/categories").param("id", String.valueOf(categoryDto.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryDto)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("카테고리 수정 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/update-category",
                requestFields(
                    fieldWithPath("id").description("카테고리 ID"),
                    fieldWithPath("name").description("카테고리명"),
                    fieldWithPath("productName").description("상품명 리스트"),
                    fieldWithPath("color").description("색상"),
                    fieldWithPath("imageUrl").description("이미지주소"),
                    fieldWithPath("description").description("상세정보")
                ),
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data.id").description("카테고리 ID"),
                    fieldWithPath("data.name").description("카테고리명"),
                    fieldWithPath("data.color").description("카테고리 색상").optional(),
                    fieldWithPath("data.imageUrl").description("카테고리 이미지 URL").optional(),
                    fieldWithPath("data.description").description("카테고리 설명").optional()
                )));
    }

    @Test
    void deleteCategoryTest() throws Exception {
        // given
        willDoNothing().given(categoryService).deleteCategoryById(any(Long.class));

        // when & then
        mockMvc.perform(delete("/api/categories")
                .param("id", String.valueOf(1L))
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result").value("OK"))
            .andExpect(jsonPath("$.message").value("카테고리 삭제 성공"))
            .andExpect(jsonPath("$.httpStatus").value("OK"))
            .andDo(document("categories/delete-category",
                responseFields(
                    fieldWithPath("result").description("API 호출 결과"),
                    fieldWithPath("message").description("호출 내용"),
                    fieldWithPath("httpStatus").description("HTTP 응답 상태"),
                    fieldWithPath("data").description("데이터")
                )));
    }


}