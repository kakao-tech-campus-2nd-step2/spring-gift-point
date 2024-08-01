package gift.controller;

import gift.dto.memberDto.MemberDto;
import gift.dto.productDto.ProductResponseDto;
import gift.dto.wishDto.WishDto;
import gift.model.member.LoginMember;
import gift.model.wish.Wish;
import gift.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/wishes")
@Tag(name = "Wish Management", description = "Wish Management API")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @Operation(summary = "전체 장바구니 호출", description = "회원의 장바구니를 불러올 때 사용하는 API")
    public String getAllWishes(@LoginMember MemberDto memberDto,@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Wish> wishPage = wishListService.getAllWishes(memberDto, PageRequest.of(page, 20));
        model.addAttribute("wishes", wishPage.getContent());
        model.addAttribute("totalPages", wishPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "manageWishList";
    }

    @PostMapping
    @Operation(summary = "장바구니에 물건 추가", description = "회원이 물건을 장바구니에 추가할 때 사용하는 API")
    public ResponseEntity<ProductResponseDto> insertWish(@LoginMember MemberDto memberDto, @RequestBody WishDto wishDto) {
        ProductResponseDto productResponseDto = wishListService.insertWish(wishDto,memberDto);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "장바구니의 물건 삭제", description = "장바구니에 담긴 물건 삭제할 때 사용하는 API")
    public ResponseEntity<Void> removeWish(@LoginMember MemberDto memberDto, @PathVariable Long wishId) {
        wishListService.deleteWish(wishId);
        return ResponseEntity.ok().build();
    }

    /*@PutMapping("/{wishId}")
    @Operation(summary = "장바구니에 담긴 상품 업데이트", description = "장바구니 상품을 수정할 때 사용하는 API")
    public ResponseEntity<Void> updateWish(@LoginMember MemberDto memberDto, @PathVariable Long wishId, @RequestBody WishDto wishDto) {
        wishListService.updateWish(wishId,wishDto);
        return ResponseEntity.ok().build();
    }*/
}