package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.categoryDTO.CategoryRequestDTO;
import gift.dto.categoryDTO.CategoryResponseDTO;
import gift.exception.AuthorizationFailedException;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.exception.ServerErrorException;
import gift.model.Member;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/categories")
@Tag(name = "카테고리 관리 API", description = "카테고리 관리를 위한 API")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "카테고리 조회", description = "모든 카테고리를 조회합니다.")
    public ResponseEntity<List<CategoryResponseDTO>> getCategories() {
        try {
            List<CategoryResponseDTO> categories = categoryService.findAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            throw new ServerErrorException("서버 내부 오류가 발생했습니다.");
        }
    }

    @PostMapping
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 추가합니다.")
    public ResponseEntity<CategoryResponseDTO> addCategory(
        @Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            CategoryResponseDTO categoryResponseDTO = categoryService.saveCategory(
                categoryRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponseDTO);
        } catch (InvalidInputValueException e) {
            throw new InvalidInputValueException("중복된 카테고리 이름입니다.");
        } catch (Exception e) {
            throw new ServerErrorException("서버 내부 오류가 발생했습니다.");
        }
    }

    @PutMapping("/{categoryId}")
    @Operation(summary = "카테고리 수정", description = "카테고리를 수정합니다.")
    public ResponseEntity<CategoryResponseDTO> updateCategory(@PathVariable Long categoryId,
        @Valid @RequestBody CategoryRequestDTO categoryRequestDTO, @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            CategoryResponseDTO categoryResponseDTO = categoryService.updateCategory(categoryId,
                categoryRequestDTO);
            return ResponseEntity.ok(categoryResponseDTO);
        } catch (NotFoundException e) {
            throw new NotFoundException("존재하지 않는 카테고리입니다.");
        } catch (Exception e) {
            throw new ServerErrorException("서버 오류가 발생했습니다.");
        }
    }

    @DeleteMapping("/{categoryId}")
    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long categoryId,
        @LoginMember Member member) {
        if (member == null) {
            throw new AuthorizationFailedException("인증되지 않은 사용자입니다.");
        }
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            throw new NotFoundException("존재하지 않는 카테고리입니다.");
        } catch (Exception e) {
            throw new ServerErrorException("서버 오류가 발생했습니다.");
        }
    }

}
