package gift.category.presentation;

import gift.auth.TokenService;
import gift.category.application.CategoryService;
import gift.category.application.CategoryServiceResponse;
import gift.category.presentation.request.CategoryCreateRequest;
import gift.category.presentation.request.CategoryUpdateRequest;
import gift.member.application.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private MemberService memberService;

    private CategoryServiceResponse categoryServiceResponse;


    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        categoryServiceResponse = new CategoryServiceResponse(1L, "Category", "#FFFFFF", "Description", "http://example.com/image.jpg");

        token = "testToken";
    }

    @Test
    void 카테고리_생성_테스트() throws Exception {
        // Given
        CategoryCreateRequest request = new CategoryCreateRequest("Category", "#FFFFFF", "Description", "http://example.com/image.jpg");

        doNothing().when(categoryService).create(any());

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Category",
                                    "color": "#FFFFFF",
                                    "description": "Description",
                                    "imageUrl": "http://example.com/image.jpg"
                                }
                                """))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).create(any());
    }

    @Test
    void 모든카테고리_조회_테스트() throws Exception {
        // Given
        Page<CategoryServiceResponse> page = new PageImpl<>(List.of(categoryServiceResponse), PageRequest.of(0, 10), 1);
        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/categories")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"id\":1,\"name\":\"Category\",\"color\":\"#FFFFFF\",\"description\":\"Description\",\"imageUrl\":\"http://example.com/image.jpg\""));
    }

    @Test
    void 카테고리_아이디로_조회_테스트() throws Exception {
        // Given
        when(categoryService.findById(1L)).thenReturn(categoryServiceResponse);

        // When
        MvcResult mvcResult = mockMvc.perform(get("/api/categories/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String responseContent = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        assertTrue(responseContent.contains("\"id\":1"));
        assertTrue(responseContent.contains("\"name\":\"Category\""));
        assertTrue(responseContent.contains("\"color\":\"#FFFFFF\""));
        assertTrue(responseContent.contains("\"description\":\"Description\""));
        assertTrue(responseContent.contains("\"imageUrl\":\"http://example.com/image.jpg\""));
    }

    @Test
    void 카테고리_수정_테스트() throws Exception {
        // Given
        CategoryUpdateRequest request = new CategoryUpdateRequest("Updated Category", "#000000", "Updated Description", "http://example.com/updated-image.jpg");

        doNothing().when(categoryService).update(any());

        // When & Then
        mockMvc.perform(put("/api/categories/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Updated Category",
                                    "color": "#000000",
                                    "description": "Updated Description",
                                    "imageUrl": "http://example.com/updated-image.jpg"
                                }
                                """))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).update(any());
    }

    @Test
    void 카테고리_삭제_테스트() throws Exception {
        // Given
        doNothing().when(categoryService).delete(1L);

        // When & Then
        mockMvc.perform(delete("/api/categories/1")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).delete(1L);
    }
}
