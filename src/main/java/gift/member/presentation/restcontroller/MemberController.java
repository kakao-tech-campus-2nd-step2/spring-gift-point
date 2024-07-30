package gift.member.presentation.restcontroller;

import gift.docs.member.MemberApiDocs;
import gift.global.authentication.annotation.MemberId;
import gift.global.authentication.dto.AuthResponse;
import gift.member.business.service.MemberService;
import gift.member.business.service.WishlistService;
import gift.member.presentation.dto.RequestMemberDto;
import gift.member.presentation.dto.RequestWishlistDto;
import gift.member.presentation.dto.ResponsePagingWishlistDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberController implements MemberApiDocs {

    private final MemberService memberService;
    private final WishlistService wishlistService;

    public MemberController(MemberService memberService, WishlistService wishlistService) {
        this.memberService = memberService;
        this.wishlistService = wishlistService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> registerMember(
        @RequestBody @Valid RequestMemberDto requestMemberDto) {
        var accessToken = memberService.registerMember(requestMemberDto.toMemberRegisterDto());
        var authResponse = new AuthResponse(requestMemberDto.email(), accessToken);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginMember(
        @RequestBody @Valid RequestMemberDto requestMemberDto) {
        var accessToken = memberService.loginMember(requestMemberDto.toMemberLoginDto());
        var authResponse = new AuthResponse(requestMemberDto.email(), accessToken);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/reissue")
    public ResponseEntity<String> reissueRefreshToken(
        @RequestHeader("Authorization") String refreshToken) {
        var accessToken = memberService.reissueAccessToken(refreshToken);
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/wishes")
    public ResponseEntity<ResponsePagingWishlistDto> getWishlistsByPage(
        @MemberId Long memberId,
        @PageableDefault(size = 20, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable,
        @RequestParam(name = "size", required = false) Integer size) {
        if (size != null) {
            if (size < 1 || size > 100) {
                throw new IllegalArgumentException("size는 1~100 사이의 값이어야 합니다.");
            }
            pageable = PageRequest.of(pageable.getPageNumber(), size, pageable.getSort());
        }
        var wishListPagingDto = wishlistService.getWishListsByPage(memberId, pageable);
        var responseWishlistPagingDto = ResponsePagingWishlistDto.from(wishListPagingDto);
        return ResponseEntity.ok(responseWishlistPagingDto);
    }

    @PostMapping("/wishes/products/{productId}")
    public ResponseEntity<Long> addWishList(@MemberId Long memberId,
        @PathVariable("productId") Long productId) {
        var wishListId = wishlistService.addWishList(memberId, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishListId);
    }

    @PutMapping("/wishes/products/{productId}")
    public ResponseEntity<Long> updateWishList(@MemberId Long memberId,
        @PathVariable("productId") Long productId,
        @RequestBody @Valid RequestWishlistDto requestWishlistDto) {
        var wishListId = wishlistService.updateWishList(memberId,
            requestWishlistDto.toWishListUpdateDto(productId));
        return ResponseEntity.ok(wishListId);
    }

    @DeleteMapping("/wishes/{wishId}")
    public ResponseEntity<Void> deleteWishList(@PathVariable("wishId") Long wishId) {
        wishlistService.deleteWishList(wishId);
        return ResponseEntity.ok().build();
    }


}
