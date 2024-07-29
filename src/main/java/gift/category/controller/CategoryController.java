package gift.category.controller;

import gift.category.domain.CategoryDTO;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/categories")
@Tag(name = "category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "category list 받기")
    public ResponseEntity<List<CategoryDTO>> findAll(){
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "id로 category 받아오기")
    public ResponseEntity<Optional<CategoryDTO>> findById(@PathVariable("id") Long CategoryId){
        return new ResponseEntity<>(categoryService.findById(CategoryId), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "새 카테고리를 등록한다.")
    public ResponseEntity<CategoryDTO> save(@RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.save(categoryDTO), HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "category 정보 업데이트")
    public ResponseEntity<CategoryDTO> updateCategory(
        @PathVariable("categoryId") Long categoryId,
        @RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(categoryService.update(categoryId, categoryDTO), HttpStatus.OK);
    }

    @DeleteMapping
    @Operation(summary = "category 정보 삭제")
    public ResponseEntity<CategoryDTO> deleteCategory(@RequestBody CategoryDTO categoryDTO){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
