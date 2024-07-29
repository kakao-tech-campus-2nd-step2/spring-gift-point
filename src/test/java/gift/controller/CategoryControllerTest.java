package gift.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CategoryDto;
import gift.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class CategoryControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;

    @DisplayName("[GET] 카테고리를 전체 조회한다.")
    @Test
    void categoryList() throws Exception {
        //given
        given(categoryService.getCategories()).willReturn(List.of());

        //when
        ResultActions result = mvc.perform(get("/api/categories"));

        //then
        result
                .andExpect(status().isOk());

        then(categoryService).should().getCategories();
    }

    @DisplayName("[POST] 카테고리 하나를 추가한다.")
    @Test
    void categoryAdd() throws Exception {
        //given
        CategoryDto request = new CategoryDto("교환권");

        willDoNothing().given(categoryService).addCategory(any(CategoryDto.class));

        //when
        ResultActions result = mvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isCreated());

        then(categoryService).should().addCategory(any(CategoryDto.class));
    }

    @DisplayName("[PUT] 카테고리 정보를 수정한다.")
    @Test
    void categoryEdit() throws Exception {
        //given
        Long categoryId = 1L;
        CategoryDto request = new CategoryDto("교환권");

        willDoNothing().given(categoryService).editCategory(anyLong(), any(CategoryDto.class));

        //when
        ResultActions result = mvc.perform(put("/api/categories/{categoryId}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result
                .andExpect(status().isOk());

        then(categoryService).should().editCategory(anyLong(), any(CategoryDto.class));
    }

    @DisplayName("[DELETE] 카테고리 하나를 삭제한다.")
    @Test
    void categoryRemove() throws Exception {
        //given
        Long categoryId = 1L;
        willDoNothing().given(categoryService).removeCategory(categoryId);

        //when
        ResultActions result = mvc.perform(delete("/api/categories/{categoryId}", categoryId));

        //then
        result
                .andExpect(status().isOk());

        then(categoryService).should().removeCategory(categoryId);
    }

}
