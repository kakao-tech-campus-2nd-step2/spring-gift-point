package gift.category.presentation;

import gift.category.presentation.request.CategoryCreateRequest;
import gift.category.presentation.request.CategoryUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "카테고리 API")
@RequestMapping("/api/categories")
public interface CategoryApi {

    @Operation(summary = "카테고리 생성")
    @PostMapping("")
    void create(
            @Parameter(description = "카테고리 생성 요청 정보", required = true)
            @RequestBody CategoryCreateRequest request
    );

    @Operation(summary = "카테고리 목록 조회")
    @GetMapping("")
    ResponseEntity<Page<CategoryControllerResponse>> findAll(
            @Parameter(description = "페이징 정보", in = ParameterIn.QUERY) Pageable pageable
    );

    @Operation(summary = "카테고리 상세 조회")
    @GetMapping("/{id}")
    ResponseEntity<CategoryControllerResponse> findById(
            @Parameter(
                    description = "카테고리 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long categoryId
    );

    @Operation(summary = "카테고리 수정")
    @PutMapping("/{id}")
    void update(
            @Parameter(
                    description = "카테고리 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long categoryId,
            @Parameter(description = "카테고리 수정 요청 정보", required = true)
            @RequestBody CategoryUpdateRequest request
    );

    @Operation(summary = "카테고리 삭제")
    @DeleteMapping("/{id}")
    void delete(
            @Parameter(
                    description = "카테고리 ID",
                    in = ParameterIn.PATH,
                    required = true
            )
            @PathVariable("id") Long categoryId
    );
}
