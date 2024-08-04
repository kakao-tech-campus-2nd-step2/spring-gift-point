package gift.category.controller;

import gift.category.domain.CategoryDTO;
import gift.category.repository.CategoryRepository;
import gift.category.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
@Tag(name = "카테고리 API")
public class CategoryController {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryService categoryService,
        CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    @Operation(summary = "카테고리 목록 조회")
    public ResponseEntity<List<CategoryDTO>> findAll(){
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }
/*
    @GetMapping("/{categoryId}")
    @Operation(summary = "id로 category 받아오기")
    public ResponseEntity<Optional<CategoryDTO>> findById(@PathVariable("categoryId") Long CategoryId){
        return new ResponseEntity<>(categoryService.findById(CategoryId), HttpStatus.OK);
    }
*/
    @PostMapping
    @Operation(summary = "카테고리 생성")
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.save(categoryDTO), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정")
    public ResponseEntity<CategoryDTO> updateCategory(
        @PathVariable("categoryId") Long categoryId,
        @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.update(categoryId, categoryDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제")
    public ResponseEntity<CategoryDTO> deleteCategory(@RequestParam("categoryId") Long categoryId, @RequestBody CategoryDTO categoryDTO){
        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
