package gift.wishlist.presentation;

import gift.member.presentation.request.ResolvedMember;
import gift.wishlist.presentation.request.WishlistCreateRequest;
import gift.wishlist.presentation.response.WishlistReadResponse;
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
    @PostMapping
    ResponseEntity<?> add(
            @Parameter(hidden = true) ResolvedMember resolvedMember,
            @Parameter(description = "상품 ID", required = true) WishlistCreateRequest request
    );

    @Operation(summary = "회원의 모든 위시리스트 조회")
    @GetMapping
    ResponseEntity<Page<WishlistReadResponse>> findAll(
            @Parameter(hidden = true) ResolvedMember resolvedMember,
            @Parameter(description = "페이징 정보") Pageable pageable
    );

    @Operation(summary = "위시리스트에서 상품 삭제")
    @DeleteMapping("/{id}")
    void delete(
            @Parameter(description = "위시리스트 ID", required = true) @PathVariable("id") Long wishlistId
    );
}
