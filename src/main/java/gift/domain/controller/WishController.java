package gift.domain.controller;

import gift.domain.annotation.ValidMember;
import gift.domain.controller.apiResponse.WishAddApiResponse;
import gift.domain.controller.apiResponse.WishListApiResponse;
import gift.domain.controller.apiResponse.WishUpdateApiResponse;
import gift.domain.dto.request.WishDeleteRequest;
import gift.domain.dto.request.WishRequest;
import gift.domain.entity.Member;
import gift.domain.service.WishService;
import gift.global.apiResponse.SuccessApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<WishListApiResponse> getWishlist(@ValidMember Member member) {
        return SuccessApiResponse.ok(new WishListApiResponse(HttpStatus.OK, wishService.getWishlist(member)));
    }

    @PostMapping
    public ResponseEntity<WishAddApiResponse> addWish(@ValidMember Member member, @Valid @RequestBody WishRequest wishRequest) {
        return SuccessApiResponse.ok(new WishAddApiResponse(HttpStatus.OK, wishService.addWishlist(member,
            wishRequest)));
    }

    @PutMapping
    public ResponseEntity<WishUpdateApiResponse> updateWish(@ValidMember Member member, @Valid @RequestBody WishRequest wishRequest) {
        return SuccessApiResponse.ok(new WishUpdateApiResponse(HttpStatus.OK, wishService.updateWishlist(member,
            wishRequest)));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteWish(@ValidMember Member member, @RequestBody WishDeleteRequest wishDeleteRequest) {
        wishService.deleteWishlist(member, wishDeleteRequest);
        return SuccessApiResponse.noContent();
    }
}
