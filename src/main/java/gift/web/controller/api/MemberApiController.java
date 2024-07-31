package gift.web.controller.api;

import gift.authentication.annotation.LoginMember;
import gift.service.MemberService;
import gift.service.WishProductService;
import gift.web.dto.MemberDetails;
import gift.web.dto.request.LoginRequest;
import gift.web.dto.request.member.CreateMemberRequest;
import gift.web.dto.request.wishproduct.UpdateWishProductRequest;
import gift.web.dto.response.LoginResponse;
import gift.web.dto.response.member.CreateMemberResponse;
import gift.web.dto.response.member.ReadMemberResponse;
import gift.web.dto.response.wishproduct.ReadAllWishProductsResponse;
import gift.web.dto.response.wishproduct.UpdateWishProductResponse;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
public class MemberApiController {

    private final MemberService memberService;
    private final WishProductService wishProductService;

    public MemberApiController(MemberService memberService, WishProductService wishProductService) {
        this.memberService = memberService;
        this.wishProductService = wishProductService;
    }

    @PostMapping("/register")
    public ResponseEntity<CreateMemberResponse> createMember(
        @Validated @RequestBody CreateMemberRequest request)
        throws URISyntaxException {
        CreateMemberResponse response = memberService.createMember(request);

        URI location = new URI("http://localhost:8080/api/members/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Validated @RequestBody LoginRequest request) {
        LoginResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ReadMemberResponse> readMember(@PathVariable Long memberId) {
        ReadMemberResponse response = memberService.readMember(memberId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist")
    public ResponseEntity<ReadAllWishProductsResponse> readWishProduct(@LoginMember MemberDetails memberDetails, @PageableDefault Pageable pageable) {
        ReadAllWishProductsResponse response = wishProductService.readAllWishProducts(memberDetails.getId(), pageable);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/wishlist/{wishProductId}")
    public ResponseEntity<UpdateWishProductResponse> updateWishProduct(
        @PathVariable Long wishProductId,
        @Validated @RequestBody UpdateWishProductRequest request) {
        UpdateWishProductResponse response = wishProductService.updateWishProduct(
            wishProductId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/wishlist/{wishProductId}")
    public ResponseEntity<Void> deleteWishProduct(@PathVariable Long wishProductId) {
        wishProductService.deleteWishProduct(wishProductId);
        return ResponseEntity.noContent().build();
    }
}
