package gift.controller;

import gift.domain.Category;
import gift.dto.CategoryRequestDto;
import gift.dto.CommonResponse;
import gift.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<Void> addCategory(@RequestBody CategoryRequestDto categoryRequestDto){
        Category category = new Category(categoryRequestDto.getName(),categoryRequestDto.getColor(),categoryRequestDto.getDescription(),categoryRequestDto.getImageUrl());
        categoryService.save(category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<CommonResponse> findAll(){
        return ResponseEntity.status(HttpStatus.OK).body(new CommonResponse(categoryService.findAll(), "모든 카테고리 조회 성공", true));
    }
}
