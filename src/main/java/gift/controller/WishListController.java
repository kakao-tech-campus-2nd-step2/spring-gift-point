package gift.controller;

import gift.domain.member.MemberId;
import gift.domain.wishlist.WishListRequest;
import gift.domain.wishlist.WishListResponse;
import gift.service.WishListService;
import gift.util.AuthAspect;
import gift.util.AuthenticatedMember;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping
    @AuthenticatedMember
    public List<WishListResponse> readWishList(HttpServletRequest httpServletRequest,
        @RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
        @RequestParam(required = false, defaultValue = "10", value = "size") int pageSize) {
        MemberId member = (MemberId) httpServletRequest.getAttribute(
            AuthAspect.ATTRIBUTE_NAME_AUTH_MEMBER);
        return wishListService.findByMemberId(member.id(), pageNo, pageSize);
    }

    @PostMapping
    @AuthenticatedMember
    public WishListResponse addWishList(HttpServletRequest httpServletRequest, @Valid @RequestBody
    WishListRequest wishListRequest) {
        MemberId member = (MemberId) httpServletRequest.getAttribute(
            AuthAspect.ATTRIBUTE_NAME_AUTH_MEMBER);
        return new WishListResponse(wishListService.save(member.id(), wishListRequest));
    }

    @DeleteMapping
    @AuthenticatedMember
    public void deleteWishList(HttpServletRequest httpServletRequest, @Valid @RequestBody
    WishListRequest wishListRequest) {
        MemberId member = (MemberId) httpServletRequest.getAttribute(
            AuthAspect.ATTRIBUTE_NAME_AUTH_MEMBER);
        wishListService.delete(member.id(), wishListRequest);
    }
}
