package gift.service;

import gift.controller.dto.request.CategoryRequest;
import gift.model.Category;
import gift.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryServiceTest {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;
    @MockBean
    private RedissonClient redissonClient;

    @Test
    @DisplayName("카테고리 업데이트 테스트[성공]")
    void updateCategory() {
        // given
        String name = "카테고리";
        String color = "#123456";
        String imageUrl = "이미지url";
        String description = "설명";
        String description2 = "설명2";
        var request = new CategoryRequest.CreateCategory(name, color, imageUrl, description);
        Long id = categoryService.save(request);
        var request2 = new CategoryRequest.UpdateCategory(id, name, color, imageUrl, description2);

        // when
        categoryService.updateById(request2);
        Category category = categoryRepository.findById(id).get();

        // then
        assertThat(category.getName()).isEqualTo(name);
        assertThat(category.getColor()).isEqualTo(color);
        assertThat(category.getImageUrl()).isEqualTo(imageUrl);
        assertThat(category.getDescription()).isEqualTo(description2);
    }

}