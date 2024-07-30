package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CategoryDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/sql/truncateIdentity.sql")
class CategoryViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("카테고리 목록 조회 테스트")
    void getAllCategoriesView() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 추가 뷰 반환 테스트")
    void getCategoryAddForm() throws Exception {
        mockMvc.perform(get("/api/categories/addForm"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("카테고리 수정 뷰 반환 테스트")
    void getCategoryEditForm() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "생일 선물", "노랑", "http", "생일 선물 카테고리");
        String inputJson = new ObjectMapper().writeValueAsString(categoryDto);

        mockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(inputJson));

        mockMvc.perform(get("/api/categories/1"))
            .andExpect(status().isOk());
    }
}