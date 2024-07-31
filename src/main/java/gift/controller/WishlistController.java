package gift.controller;

import gift.annotation.LoginMember;
import gift.domain.TokenAuth;
import gift.dto.request.WishlistRequest;
import gift.dto.response.WishlistPageResponse;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/wishes")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/new")
    @Operation(summary = "위시리스트 추가 화면", description = "위시리스트 추가 화면으로 이동한다.")
    public String newWishlistItemForm(Model model, @LoginMember TokenAuth tokenAuth) {
        model.addAttribute("wishlistItem", new WishlistRequest());
        model.addAttribute("token", tokenAuth.getToken());
        return "wishlist-add-form";
    }

    @PostMapping
    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    public void addToWishlist(@Valid @RequestBody WishlistRequest request, @LoginMember TokenAuth tokenAuth) {
        Long memberId = tokenAuth.getMemberId();
        wishlistService.addItemToWishlist(request, memberId);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
    public void deleteItemFromWishlist(@PathVariable Long productId, @LoginMember TokenAuth tokenAuth) {
        Long memberId = tokenAuth.getMemberId();
        wishlistService.deleteItemFromWishlist(productId, memberId);
    }

    @GetMapping
    @Operation(summary = "위시 리스트 상품 조회 (페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    public WishlistPageResponse getWishlistForCurrentUser(@RequestParam(defaultValue = "20") int size,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "createdDate,desc") String sort,
                                                          @LoginMember TokenAuth tokenAuth) {
        Long memberId = tokenAuth.getMember().getId();
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort)));
        return wishlistService.getWishlistByMemberId(memberId, pageable);
    }

}
