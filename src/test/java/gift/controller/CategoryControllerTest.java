package gift.controller;

import gift.dto.CategoryDto;
import gift.service.CategoryService;
import gift.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@ActiveProfiles("test")
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TokenService tokenService;

    private CategoryDto category1;
    private CategoryDto category2;

    @BeforeEach
    void setUp() {
        category1 = new CategoryDto(1L, "Category 1", "Red", "img1.jpg", "Description 1");
        category2 = new CategoryDto(2L, "Category 2", "Blue", "img2.jpg", "Description 2");
    }

    @Test
    @DisplayName("카테고리 조회 테스트")
    void getCategories() throws Exception {
        given(categoryService.findAll()).willReturn(Arrays.asList(category1, category2));

        mockMvc.perform(get("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(category1.getId()))
                .andExpect(jsonPath("$[0].name").value(category1.getName()))
                .andExpect(jsonPath("$[0].color").value(category1.getColor()))
                .andExpect(jsonPath("$[0].imgUrl").value(category1.getImgUrl()))
                .andExpect(jsonPath("$[0].description").value(category1.getDescription()))
                .andExpect(jsonPath("$[1].id").value(category2.getId()))
                .andExpect(jsonPath("$[1].name").value(category2.getName()))
                .andExpect(jsonPath("$[1].color").value(category2.getColor()))
                .andExpect(jsonPath("$[1].imgUrl").value(category2.getImgUrl()))
                .andExpect(jsonPath("$[1].description").value(category2.getDescription()));
    }
}