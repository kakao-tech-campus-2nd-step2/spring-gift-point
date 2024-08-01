package gift.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.OptionSaveRequest;
import gift.dto.ProductRequest;
import java.util.List;
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
class ProductViewControllerTest {

    private @Autowired MockMvc mockMvc;

    void addCategory() throws Exception {
        String category = """ 
            {"name": "음식", "color": "Red", "imageUrl": "http", "description": "description"}
            """;
        mockMvc.perform(post("/api/categories")
            .contentType(MediaType.APPLICATION_JSON)
            .content(category));
    }

    @Test
    @DisplayName("상품 목록 가져오기 테스트")
    void getProducts() throws Exception {
        mockMvc.perform(get("/api/products"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 추가 폼 페이지 가져오기 테스트")
    void addProductForm() throws Exception {
        mockMvc.perform(get("/api/products/product"))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("상품 수정 폼 페이지 테스트")
    void editProductForm() throws Exception {
        ProductRequest request = new ProductRequest(null, "선물", 4500L, "https", 1L, "생일 선물",
            List.of(new OptionSaveRequest("케잌", 30, null)));
        String requestJson = new ObjectMapper().writeValueAsString(request);

        addCategory();
        mockMvc.perform(post("/api/products/product")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson));

        mockMvc.perform(get("/api/products/product/1"))
            .andExpect(status().isOk());
    }
}