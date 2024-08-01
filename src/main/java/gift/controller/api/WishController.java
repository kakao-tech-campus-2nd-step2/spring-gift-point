package gift.controller.api;

import gift.dto.request.WishRequest;
import gift.dto.response.WishProductResponse;
import gift.interceptor.MemberId;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("api/wishes")
    public ResponseEntity<Void> addProductToWish(@MemberId Long memberId, @Valid @RequestBody WishRequest request) {
        wishService.addProductToWish(memberId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("api/wishes")
    public ResponseEntity<Page<WishProductResponse>> getWishProducts(@MemberId Long memberId, @PageableDefault(sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<WishProductResponse> wishProductResponses = wishService.getWishProductResponses(memberId, pageable);
        return ResponseEntity.ok(wishProductResponses);
    }

    @PutMapping("api/wishes")
    public ResponseEntity<Void> updateWishProductQuantity(@MemberId Long memberId, @Valid @RequestBody WishRequest request) {
        wishService.updateWishProductQuantity(memberId, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("api/wishes/{wishId}")
    public ResponseEntity<Void> deleteWishProduct(@MemberId Long memberId, @PathVariable("wishId") Long wishId) {
        wishService.findAndDeleteProductInWish(memberId, wishId);
        return ResponseEntity.ok().build();
    }
}
