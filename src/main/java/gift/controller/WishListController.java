package gift.controller;

import gift.domain.WishList;
import gift.domain.WishListRequest;
import gift.domain.WishListResponse;
import gift.repository.MenuRepository;
import gift.service.JwtService;
import gift.service.MemberService;
import gift.service.MenuService;
import gift.service.WishListService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/wishes")
public class WishListController {
    private final WishListService wishListService;
    private final JwtService jwtService;
    private final MenuRepository menuRepository;
    private final MemberService memberService;

    public WishListController(WishListService wishListService, JwtService jwtService, MenuRepository menuRepository, MemberService memberService) {
        this.wishListService = wishListService;
        this.jwtService = jwtService;
        this.menuRepository = menuRepository;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<String> save(
            @RequestHeader("Authorization") String token,
            @RequestBody WishListRequest wishListRequest
    ) {
        String jwtId = jwtService.getMemberId();
        WishList wishList = new WishList(
                memberService.findById(jwtId),
                menuRepository.findById(wishListRequest.id()).get()
        );
        wishListService.save(wishList);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token.replace("Bearer ", ""));
        return ResponseEntity.status(HttpStatus.CREATED).headers(headers).body("success");
    }

    @GetMapping
    public ResponseEntity<List<WishListResponse>> read(
            Pageable pageable
    ) {
        String jwtId = jwtService.getMemberId();
        List<WishListResponse> nowWishList = wishListService.findById(jwtId, pageable);
        return ResponseEntity.ok().body(nowWishList);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<String> delete(
            @PathVariable("wishId") Long wishId,
            @RequestBody WishListRequest wishListRequest
    ) throws IllegalAccessException {
        String jwtId = jwtService.getMemberId();
        wishListService.delete(jwtId,wishListRequest.id());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("성공적으로 삭제되었습니다.");
    }

}
