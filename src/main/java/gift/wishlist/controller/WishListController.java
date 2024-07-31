package gift.wishlist.controller;

import gift.auth.LoginMember;
import gift.member.dto.MemberResDto;
import gift.wishlist.dto.WishListReqDto;
import gift.wishlist.dto.WishListResDto;
import gift.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "장바구니 API", description = "장바구니 관리 API")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "장바구니 목록 조회", description = "회원의 장바구니 목록을 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "장바구니 목록 조회 성공"),
            })
    public ResponseEntity<Page<WishListResDto>> getWishLists(@LoginMember MemberResDto member, Pageable pageable) {
        Page<WishListResDto> wishList = wishListService.getWishListsByMemberId(member.id(), pageable);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping
    @Operation(summary = "장바구니 추가", description = "상품을 장바구니에 추가합니다.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "장바구니 추가 성공"),
            })
    public ResponseEntity<String> addWishList(@LoginMember MemberResDto member, @RequestBody WishListReqDto wishListReqDto) {
        wishListService.addWishList(member.id(), wishListReqDto);
        return ResponseEntity.created(URI.create("/api/wishes")).body("상품을 장바구니에 담았습니다.");
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "장바구니 삭제", description = "장바구니에 담긴 상품을 삭제합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "장바구니 삭제 성공"),
            })
    public ResponseEntity<String> deleteWishList(
            @LoginMember MemberResDto member,
            @PathVariable("wishId") Long wishListId
    ) {
        wishListService.deleteWishListById(member.id(), wishListId);
        return ResponseEntity.ok("상품을 장바구니에서 삭제했습니다.");
    }
}
