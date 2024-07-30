package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.CategoryRequestDto;
import gift.dto.response.CategoryResponseDto;
import gift.filter.AuthFilter;
import gift.filter.LoginFilter;
import gift.repository.token.TokenRepository;
import gift.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;

import static gift.utils.FilterConstant.NO_AUTHORIZATION_REDIRECT_URL;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mvc;

    @MockBean
    CategoryService categoryService;


    @MockBean
    TokenRepository tokenRepository;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("필터 통과 실패 테스트")
    void 필터_통과_실패_테스트() throws Exception {
        MockMvc mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new AuthFilter(tokenRepository))
                .addFilter(new LoginFilter(tokenRepository))
                .build();

        mockMvc.perform(get("/api/categories"))
                .andExpect(redirectedUrl(NO_AUTHORIZATION_REDIRECT_URL))
                .andExpect(status().is3xxRedirection())
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 전체 조회 API 테스트")
    void 카테고리_전체_조회_API_테스트() throws Exception{
        //given
        CategoryResponseDto categoryResponseDto1 = new CategoryResponseDto(1L, "상품권", "#0000", "abc.png", "");
        CategoryResponseDto categoryResponseDto2 = new CategoryResponseDto(2L, "고기", "#0001", "abc.png", "");
        CategoryResponseDto categoryResponseDto3 = new CategoryResponseDto(3L, "생선", "#0002", "abc.png", "");

        List<CategoryResponseDto> categoryResponseDtos = Arrays.asList(categoryResponseDto1, categoryResponseDto2, categoryResponseDto3);

        given(categoryService.findAllCategories()).willReturn(categoryResponseDtos);

        //expected
        mvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 단건 조회 API 테스트")
    void 카테고리_단건_조회_API_테스트() throws Exception{
        //given
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "상품권", "#0000", "abc.png", "");

        given(categoryService.findOneCategoryById(categoryResponseDto.id())).willReturn(categoryResponseDto);

        //expected
        mvc.perform(get("/api/categories/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("상품권"))
                .andExpect(jsonPath("$.color").value("#0000"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 저장,수정 시 CategoryDto Invalid 테스트")
    void 카테고리_저장_수정_DTO_INVALID_테스트() throws Exception{
        //given
        CategoryRequestDto inValidcategoryRequestDto = new CategoryRequestDto(null, null, null, null);

        //expected
        mvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(inValidcategoryRequestDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.color").value("카테고리 색상을 입력해 주세요"))
                .andExpect(jsonPath("$.validation.name").value("카테고리 이름을 입력해 주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 저장 API 테스트")
    void 카테고리_저장_API_테스트() throws Exception{
        //given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("상품권", "#0000", "abc.png", "");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "상품권", "#0000", "abc.png", "");

        given(categoryService.saveCategory(categoryRequestDto)).willReturn(categoryResponseDto);

        //expected
        mvc.perform(post("/api/categories")
                        .content(objectMapper.writeValueAsString(categoryRequestDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("상품권"))
                .andExpect(jsonPath("$.color").value("#0000"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 수정 API 테스트")
    void 카테고리_수정_API_테스트() throws Exception{
        //given
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("상품권", "#0000", "abc.png", "");
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "상품권", "#0000", "abc.png", "");

        given(categoryService.updateCategory(1L, categoryRequestDto)).willReturn(categoryResponseDto);

        //expected
        mvc.perform(put("/api/categories/{id}",1L)
                        .content(objectMapper.writeValueAsString(categoryRequestDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("상품권"))
                .andExpect(jsonPath("$.color").value("#0000"))
                .andDo(print());
    }

    @Test
    @DisplayName("카테고리 삭제 API 테스트")
    void 카테고리_삭제_API_테스트() throws Exception{
        //given
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto(1L, "상품권", "#0000", "abc.png", "");

        given(categoryService.deleteCategory(categoryResponseDto.id())).willReturn(categoryResponseDto);

        //expected
        mvc.perform(delete("/api/categories/{id}",1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("상품권"))
                .andExpect(jsonPath("$.color").value("#0000"))
                .andDo(print());
    }

}