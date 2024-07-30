package gift.controller;

import gift.domain.Product;
import gift.domain.WishList;
import gift.service.WishListService;
import gift.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wish")
@Tag(name = "WishList", description = "위시리스트 API")
public class WishListController {

    private final WishListService wishListService;
    private final JwtUtil jwtUtil;

    public WishListController(WishListService wishListService, JwtUtil jwtUtil) {
        this.wishListService = wishListService;
        this.jwtUtil = jwtUtil;
    }

//    @GetMapping
//    @Operation(summary = "전체 위시리스트 출력", description = "해당 멤버의 전체 위시리스트 출력")
//    public ResponseEntity<?> getWishListItems(HttpServletRequest request) {
//        Long memberId = jwtUtil.extractMemberId(request);
//        List<WishList> wishLists = wishListService.getWishListItems(memberId);
//        return ResponseEntity.ok(wishLists);
//    }

    @GetMapping("")
    @Operation(summary = "전체 위시리스트 출력", description = "해당 멤버의 전체 위시리스트를 페이지네이션 방식으로 출력")
    public ResponseEntity<?> getWishListItems(
        HttpServletRequest request,
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {

        Long memberId = jwtUtil.extractMemberId(request);
        Page<WishList> wishLists = wishListService.getWishListItems(memberId, page, size);
        return ResponseEntity.ok(wishLists);
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "해당 멤버의 위시리스트 추가")
    public ResponseEntity<?> addWishListItem(HttpServletRequest request,
        @Valid @RequestBody Product product) {
        Long memberId = jwtUtil.extractMemberId(request);
        WishList wishList = new WishList(memberId, product.getId());
        wishListService.addWishListItem(wishList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to wishlist");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시리스트 삭제", description = "해당 위시리스트 삭제")
    public ResponseEntity<?> deleteWishListItem(@PathVariable("id") Long id) {
        wishListService.deleteWishListItem(id);
        return ResponseEntity.status(HttpStatus.OK).body("Product removed from wishlist");
    }

}
