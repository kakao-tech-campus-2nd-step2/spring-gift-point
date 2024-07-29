package gift.controller;

import gift.domain.WishList;
import gift.domain.WishListRequest;
import gift.domain.WishListResponse;
import gift.service.JwtService;
import gift.service.MemberService;
import gift.service.MenuService;
import gift.service.WishListService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishlists")
public class WishListController {
    private final WishListService wishListService;
    private final JwtService jwtService;
    private final MenuService menuService;
    private final MemberService memberService;

    public WishListController(WishListService wishListService, JwtService jwtService, MenuService menuService, MemberService memberService) {
        this.wishListService = wishListService;
        this.jwtService = jwtService;
        this.menuService = menuService;
        this.memberService = memberService;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(
            @RequestHeader("Authorization") String token,
            @RequestParam("menuId") Long menuId
    ) {
        String jwtId = jwtService.getMemberId();
        WishList wishList = new WishList(
                memberService.findById(jwtId),
                menuService.findById(menuId)
        );
        wishListService.save(wishList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token.replace("Bearer ", ""));
        return ResponseEntity.ok().headers(headers).body("success");
    }

    @GetMapping("/read")
    public ResponseEntity<List<WishListResponse>> read(
            Pageable pageable
    ) {
        String jwtId = jwtService.getMemberId();
        List<WishListResponse> nowWishList = wishListService.findById(jwtId, pageable);
        return ResponseEntity.ok().body(nowWishList);
    }

    @DeleteMapping
    public ResponseEntity<String> delete(
            @RequestBody WishListRequest wishListRequest
    ) {
        jwtService.getMemberId();
        wishListService.delete(wishListRequest.id());
        return ResponseEntity.ok().body("성공적으로 삭제되었습니다.");
    }

}
