package gift.controller;

import gift.domain.Wish.WishRequest;
import gift.domain.Wish.WishResponse;
import gift.service.WishService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wish")
@Tag(name = "Wish", description = "위시리스트 API")
public class WishListController {

    private final WishService wishService;
    private final JwtUtil jwtUtil;

    public WishListController(WishService wishService, JwtUtil jwtUtil) {
        this.wishService = wishService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("")
    @Operation(summary = "전체 위시리스트 출력", description = "해당 멤버의 전체 위시리스트를 페이지네이션 방식으로 출력")
    public ResponseEntity<Page<WishResponse>> getWishListItems(
        @RequestHeader("Authorization") String header,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        Long memberId = jwtUtil.extractMemberId(header);
        Page<WishResponse> pages = wishService.getWishListItems(memberId, page, size);
        return ResponseEntity.ok().body(pages);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "해당 멤버의 위시리스트 추가")
    public ResponseEntity<?> addWishListItem(
        @RequestHeader("Authorization") String header,
        @Valid @RequestBody WishRequest wishRequest) {
        Long memberId = jwtUtil.extractMemberId(header);
        wishService.addWishListItem(memberId, wishRequest.productId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Wish added");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시리스트 삭제", description = "해당 위시리스트 삭제")
    public ResponseEntity<?> deleteWishListItem(@PathVariable("id") Long id) {
        wishService.deleteWishListItem(id);
        return ResponseEntity.status(HttpStatus.OK).body("Wish removed");
    }

}
