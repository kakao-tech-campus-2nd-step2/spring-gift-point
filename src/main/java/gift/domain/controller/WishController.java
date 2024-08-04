package gift.domain.controller;

import gift.domain.annotation.ValidMember;
import gift.domain.controller.apiResponse.WishAddApiResponse;
import gift.domain.controller.apiResponse.WishListApiResponse;
import gift.domain.dto.request.WishRequest;
import gift.domain.entity.Member;
import gift.domain.service.WishService;
import gift.global.apiResponse.SuccessApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
        return SuccessApiResponse.of(new WishAddApiResponse(HttpStatus.CREATED, wishService.addWishlist(member, wishRequest)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWish(@ValidMember Member member, @PathVariable("id") Long id) {
        wishService.deleteWishlist(member, id);
        return SuccessApiResponse.noContent();
    }
}
