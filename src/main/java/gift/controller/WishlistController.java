package gift.controller;

import gift.constants.SuccessMessage;
import gift.dto.WishlistResponse;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
public class WishlistController {

    private final MemberService memberService;

    public WishlistController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public Page<WishlistResponse> getWishlist(@PageableDefault(size = 5) Pageable pageable,
        HttpServletRequest httpServletRequest) {
        String email = (String) httpServletRequest.getAttribute("email");

        return memberService.getAllWishlist(email, pageable);
    }

    @PostMapping
    public ResponseEntity<String> addWishlist(@RequestBody Map<String, Long> productId,
        HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        memberService.addWishlist(email, productId.get("productId"));

        return ResponseEntity.ok(SuccessMessage.ADD_WISHLIST_SUCCESS_MSG);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<String> deleteWishlist(@PathVariable("wishId") Long wishId,
        HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        memberService.deleteWishlist(email, wishId);

        return ResponseEntity.ok(SuccessMessage.DELETE_WISHLIST_SUCCESS_MSG);
    }
}
