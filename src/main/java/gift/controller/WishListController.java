package gift.controller;

import gift.common.annotation.LoginMember;
import gift.model.user.LoginUserDTO;
import gift.model.wishlist.WishList;
import gift.model.wishlist.WishRequest;
import gift.model.wishlist.WishResponse;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위시리스트 API", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishListController {
    private final WishListService wishListService;
    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "사용자의 위시리스트 조회", description = "사용자의 위시리스트를 조회한다.")
    @GetMapping
    public ResponseEntity<Page<WishResponse>> getWishList(@LoginMember LoginUserDTO member,
    @PageableDefault(size = 10, sort =  "name") Pageable pageable) {
        Page<WishResponse> wishLists = wishListService.getWishListByUserId(member.getId(), pageable);
        return ResponseEntity.ok(wishLists);
    }

    @Operation(summary = "위시리스트 추가", description = "사용자의 위시리스트에 상품을 추가한다.")
    @PostMapping
    public ResponseEntity<WishResponse> addProductToWishList(@RequestBody WishRequest wishRequest, @LoginMember
    LoginUserDTO member) {
        WishResponse addedWish = wishListService.addWishList(member.getId(), wishRequest);
        return ResponseEntity.ok(addedWish);
    }

    @Operation(summary = "위시리스트 수정", description = "사용자의 위시리스트를 수정한다.")
    @PostMapping("{id}")
    public ResponseEntity<WishResponse> updateQuantityToWishList(@PathVariable("id") Long wishId, Long userId, int quantity) {
        WishResponse updatedWish = wishListService.updateProductQuantity(wishId, userId, quantity);
        return ResponseEntity.ok(updatedWish);
    }

    @Operation(summary = "위시리스트 삭제", description = "사용자의 위시리스트를 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeWishList(@PathVariable("id") Long userId, @PathVariable("product_id") Long productId, @LoginMember
    LoginUserDTO member) {
        wishListService.removeWishList(userId, productId);
        return ResponseEntity.noContent().build();
    }
}
