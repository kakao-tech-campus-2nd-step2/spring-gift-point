package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import gift.controller.CategoryController;
import gift.dto.CategoryRequestDto;
import gift.dto.CategoryResponseDto;
import gift.entity.Category;
import gift.service.CategoryService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
public class CategoryControllerTest {

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;


    private MockMvc mockMvc;



    @Test
    @DisplayName("전건 조회 테스트")
    public void getCategoriesTest() throws Exception {
        List<Category> categories = Arrays.asList(
            new Category("카테고리1", "컬러1", "이미지1", "설명1"),
            new Category("카테고리2", "컬러2", "이미지2", "설명2")
        );

        when(categoryService.findAll()).thenReturn(categories);


        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("카테고리1"))
            .andExpect(jsonPath("$[1].name").value("카테고리2"));
    }

    @Test
    @DisplayName("카테고리 추가 테스트")
    public void addCategoryTest() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("카테고리1", "컬러1", "이미지1", "설명1");
        Category category = new Category("카테고리1", "컬러1", "이미지1", "설명1");



        when(categoryService.save(any(CategoryRequestDto.class))).thenReturn(category);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"카테고리1\", \"color\": \"컬러1\", \"imageUrl\": \"이미지1\", \"description\": \"설명1\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("카테고리1"));

    }



    @Test
    @DisplayName("카테고리 수정 테스트")
    public void updateCategoryTest() throws Exception {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto("카테고리2", "컬러2", "이미지2", "설명2");
        Category category = new Category("카테고리2", "컬러2", "이미지2", "설명2");

        when(categoryService.update(any(CategoryRequestDto.class), anyLong())).thenReturn(category);

        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

        mockMvc.perform(put("/api/categories/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\": \"카테고리2\", \"color\": \"컬러2\", \"imageUrl\": \"이미지2\", \"description\": \"설명2\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("카테고리2"));
    }


}
