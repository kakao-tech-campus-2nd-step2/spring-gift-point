package gift.wishlist.presentation;

import gift.member.presentation.request.ResolvedMember;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "위시리스트 API")
@RequestMapping("/api/wishlist")
public interface WishlistApi {

    @Operation(summary = "위시리스트에 상품 추가")
    @PostMapping("")
    void add(
            @Parameter(hidden = true) ResolvedMember resolvedMember,
            @Parameter(description = "상품 ID", required = true) @RequestParam("productId") Long productId
    );

    @Operation(summary = "회원의 모든 위시리스트 조회")
    @GetMapping("")
    ResponseEntity<Page<WishlistControllerResponse>> findAll(
            @Parameter(hidden = true) ResolvedMember resolvedMember,
            @Parameter(description = "페이징 정보") Pageable pageable
    );

    @Operation(summary = "특정 상품의 위시리스트 조회")
    @GetMapping("/{productId}")
    ResponseEntity<Page<WishlistControllerResponse>> findAllByProductId(
            @Parameter(description = "상품 ID", required = true) @PathVariable("productId") Long productId,
            @Parameter(description = "페이징 정보") Pageable pageable
    );

    @Operation(summary = "위시리스트에서 상품 삭제")
    @DeleteMapping("/{id}")
    void delete(
            @Parameter(description = "위시리스트 ID", required = true) @PathVariable("id") Long wishlistId
    );
}
