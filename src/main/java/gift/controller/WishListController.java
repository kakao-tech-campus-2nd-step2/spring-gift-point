package gift.controller;

import gift.CustomAnnotation.RequestRole;
import gift.model.entity.Role;
import gift.model.form.WishForm;
import gift.model.response.WishListResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish List Api")
@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @RequestRole(Role.ROLE_USER)
    @Operation(summary = "위시 리스트 조회")
    @GetMapping
    public Page<WishListResponse> getWishList(@RequestAttribute("userId") Long userId,
        @PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable) {
        return wishListService.getWishList(userId, pageable);
    }

    @RequestRole(Role.ROLE_USER)
    @Operation(summary = "위시 상품 추가")
    @PostMapping
    public ResponseEntity<Long> addToWishList(
        @Parameter(description = "추가할 상품 id") @RequestBody WishForm wishForm,
        @RequestAttribute("userId") Long userId) {
        Long wishId = wishListService.addToWishList(userId, wishForm.getProductId());
        return ResponseEntity.ok(wishId);
    }

    @RequestRole(Role.ROLE_USER)
    @Operation(summary = "위시 상품 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Long> deleteFromWishList(
        @Parameter(description = "삭제할 상품 id") @PathVariable("id") Long id,
        @RequestAttribute("userId") Long userId) {
        wishListService.deleteFromWishList(id);
        return ResponseEntity.ok(id);
    }
}