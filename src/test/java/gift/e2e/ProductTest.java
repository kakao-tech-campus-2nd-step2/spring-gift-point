package gift.e2e;


import com.fasterxml.jackson.databind.ObjectMapper;
import gift.dto.request.AddCategoryRequest;
import gift.dto.request.AddProductRequest;
import gift.dto.request.OptionRequest;
import gift.dto.request.UpdateProductRequest;
import gift.service.CategoryService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("상품/옵션 조회,추가,수정 기능 테스트")
public class ProductTest {

    private static Long savedProductId;
    private static Long categoryId;
    private final String URL = "/api/products";
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @Order(1)
    @DisplayName("상품 추가")
    void productAdd() throws Exception {
        //Given
        categoryId = categoryService.addCategory(new AddCategoryRequest("카테고리", "color", "imageUrl", "des")).id();
        AddProductRequest request = new AddProductRequest("상품이름", 100, "url", categoryId, List.of(new OptionRequest("option1", 100)));

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //Then
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("id").exists()
                ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        String substring = contentAsString.substring(1, contentAsString.length() - 1);
        savedProductId = Long.parseLong(substring.split(":")[1]);
    }

    @Test
    @Order(2)
    @DisplayName("상품 수정")
    void update() throws Exception {
        //Given
        UpdateProductRequest request = new UpdateProductRequest(savedProductId, "UPDATENAME", 100, "url", categoryId);

        //When
        mockMvc.perform(MockMvcRequestBuilders.put(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //Then
                .andExpectAll(
                        status().isOk()
                );
    }

    @Test
    @Order(3)
    @DisplayName("상품들 조회")
    void getProducts() throws Exception {
        //When
        mockMvc.perform(MockMvcRequestBuilders.get(URL))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @Test
    @Order(4)
    @DisplayName("상품 옵션 추가")
    void optionAdd() throws Exception {
        //Given
        OptionRequest request = new OptionRequest("New Option", 999);
        //When
        mockMvc.perform(MockMvcRequestBuilders.post(URL + "/" + savedProductId + "/options")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                //Then
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("optionId").exists()
                );
    }

    @Test
    @Order(5)
    @DisplayName("상품 옵션 조회")
    void optionGet() throws Exception {
        //When
        mockMvc.perform(MockMvcRequestBuilders.get(URL + "/" + savedProductId + "/options"))
                //Then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        jsonPath("[1].quantity").value(999)
                );
    }
}
