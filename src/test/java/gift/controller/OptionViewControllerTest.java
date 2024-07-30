package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.CategoryDto;
import gift.dto.OptionSaveRequest;
import gift.dto.ProductRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
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
class OptionViewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() throws Exception {
        CategoryDto categoryDto = new CategoryDto(null, "생일 선물", "노랑", "http", "생일 선물 카테고리");
        String category = new ObjectMapper().writeValueAsString(categoryDto);
        mockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(category));

        ProductRequest request = new ProductRequest(null, "선물", 4500L, "https", 1L, "생일 선물",
            List.of(new OptionSaveRequest("케잌", 30, null)));

        String product = new ObjectMapper().writeValueAsString(request);
        mockMvc.perform(post("/api/products/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(product));
    }

    @Test
    @DisplayName("옵션 목록 조회 테스트")
    void getOptions() throws Exception {
        mockMvc.perform(get("/api/products/product/1/options"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("옵션 추가 페이지 테스트")
    void getOptionAddForm() throws Exception {
        mockMvc.perform(get("/api/products/product/1/options/addForm"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("옵션 수정 페이지 테스트")
    void getOptionEditForm() throws Exception {
        OptionSaveRequest optionDto = new OptionSaveRequest("초코 케익", 30, 1L);
        String option = new ObjectMapper().writeValueAsString(optionDto);
        mockMvc.perform(post("/api/products/product/1/options")
            .contentType(MediaType.APPLICATION_JSON)
            .content(option));

        mockMvc.perform(get("/api/products/product/1/options/1"))
            .andExpect(status().isOk());
    }
}