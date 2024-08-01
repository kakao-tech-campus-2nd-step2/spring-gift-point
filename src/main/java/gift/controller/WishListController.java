package gift.controller;

import gift.domain.WishList.WishList;
import gift.domain.WishList.WishListRequest;
import gift.domain.WishList.WishListResponse;
import gift.repository.MenuRepository;
import gift.service.JwtService;
import gift.service.MemberService;
import gift.service.WishListService;
import io.jsonwebtoken.JwtException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<WishListResponse> save(
            @RequestHeader("Authorization") String token,
            @RequestBody WishListRequest wishListRequest
    ) {
        String jwtId = jwtService.getMemberId();
        WishList wishList = new WishList(
                memberService.findById(jwtId),
                menuRepository.findById(wishListRequest.productId()).get(), new Date()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListService.save(wishList));
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
    public ResponseEntity<Void> delete(
            @PathVariable("wishId") Long wishId,
            @RequestBody WishListRequest wishListRequest
    ) throws IllegalAccessException {
        String jwtId = jwtService.getMemberId();
        wishListService.delete(jwtId,wishListRequest.productId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, String>> handleException(JwtException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("TokenError", "허용되지 않는 요청입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errors);
    }

}
