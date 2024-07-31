package gift.Controller;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import gift.DTO.CategoryDto;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CategoryControllerTest {

  private final CategoryController categoryController;

  @Autowired
  public CategoryControllerTest(CategoryController categoryController) {
    this.categoryController = categoryController;
  }

  @DirtiesContext
  @Test
  void getAllCategories() {
    CategoryDto categoryDto1 = new CategoryDto(1L, "c1", "#ffffff", "imageUrl", "설명칸");
    CategoryDto categoryDto2 = new CategoryDto(2L, "c2", "#ffffff", "imageUrl", "설명칸");

    CategoryDto addedCategoryDto1 = categoryController.addCategory(categoryDto1).getBody();
    CategoryDto addedCategoryDto2 = categoryController.addCategory(categoryDto2).getBody();

    List<CategoryDto> categoryDtos = categoryController.getAllCategories().getBody();

    assertThat(categoryDtos.get(0).getId()).isEqualTo(addedCategoryDto1.getId());
    assertThat(categoryDtos.get(0).getName()).isEqualTo(addedCategoryDto1.getName());
    assertThat(categoryDtos.get(0).getColor()).isEqualTo(addedCategoryDto1.getColor());
    assertThat(categoryDtos.get(0).getImageUrl()).isEqualTo(addedCategoryDto1.getImageUrl());
    assertThat(categoryDtos.get(0).getDescription()).isEqualTo(addedCategoryDto1.getDescription());

    assertThat(categoryDtos.get(1).getId()).isEqualTo(addedCategoryDto2.getId());
    assertThat(categoryDtos.get(1).getName()).isEqualTo(addedCategoryDto2.getName());
    assertThat(categoryDtos.get(1).getColor()).isEqualTo(addedCategoryDto2.getColor());
    assertThat(categoryDtos.get(1).getImageUrl()).isEqualTo(addedCategoryDto2.getImageUrl());
    assertThat(categoryDtos.get(1).getDescription()).isEqualTo(addedCategoryDto2.getDescription());
  }

  @DirtiesContext
  @Test
  void getCategoryById() {
    CategoryDto categoryDto = new CategoryDto(1L, "c3", "#ffffff", "imageUrl", "설명칸");
    CategoryDto addedCategoryDto = categoryController.addCategory(categoryDto).getBody();

    CategoryDto findCategoryDto = categoryController.getCategoryById(1L).getBody();

    assertThat(findCategoryDto.getId()).isEqualTo(categoryDto.getId());
    assertThat(findCategoryDto.getName()).isEqualTo(categoryDto.getName());
    assertThat(findCategoryDto.getColor()).isEqualTo(categoryDto.getColor());
    assertThat(findCategoryDto.getImageUrl()).isEqualTo(categoryDto.getImageUrl());
    assertThat(findCategoryDto.getDescription()).isEqualTo(categoryDto.getDescription());
  }

  @DirtiesContext
  @Test
  void addCategory() {
    CategoryDto categoryDto = new CategoryDto(1L, "c4", "#ffffff", "imageUrl", "설명칸");
    CategoryDto addedCategoryDto = categoryController.addCategory(categoryDto).getBody();

    assertThat(addedCategoryDto.getId()).isEqualTo(categoryDto.getId());
    assertThat(addedCategoryDto.getName()).isEqualTo(categoryDto.getName());
    assertThat(addedCategoryDto.getColor()).isEqualTo(categoryDto.getColor());
    assertThat(addedCategoryDto.getImageUrl()).isEqualTo(categoryDto.getImageUrl());
    assertThat(addedCategoryDto.getDescription()).isEqualTo(categoryDto.getDescription());
  }

  @DirtiesContext
  @Test
  void updateCategory() {
    CategoryDto categoryDto = new CategoryDto(1L, "c5", "#ffffff", "imageUrl", "설명칸");
    CategoryDto addedCategoryDto = categoryController.addCategory(categoryDto).getBody();

    CategoryDto updateCategoryDto = new CategoryDto(1L, "c6", "#ffffff", "imageUrl", "설명칸");
    CategoryDto updatedCategoryDto = categoryController.updateCategory(1L, updateCategoryDto)
      .getBody();

    assertThat(updateCategoryDto.getId()).isEqualTo(updatedCategoryDto.getId());
    assertThat(updateCategoryDto.getName()).isEqualTo(updatedCategoryDto.getName());
    assertThat(updateCategoryDto.getColor()).isEqualTo(updatedCategoryDto.getColor());
    assertThat(updateCategoryDto.getImageUrl()).isEqualTo(updatedCategoryDto.getImageUrl());
    assertThat(updateCategoryDto.getDescription()).isEqualTo(updatedCategoryDto.getDescription());

  }

  @DirtiesContext
  @Test
  void deleteCategory() {
    CategoryDto categoryDto = new CategoryDto(1L, "c7", "#ffffff", "imageUrl", "설명칸");
    CategoryDto addedCategoryDto = categoryController.addCategory(categoryDto).getBody();

    ResponseEntity<CategoryDto> responseDto = categoryController.deleteCategory(1L);
    assertEquals(HttpStatus.OK, responseDto.getStatusCode());
  }
}