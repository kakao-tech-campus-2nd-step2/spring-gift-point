package gift.controller;

import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping
    public ResponseEntity<Page<Wish>> getWishes(Pageable pageable) {
        Page<Wish> wishes = wishService.getWishes(pageable);
        return ResponseEntity.ok(wishes);
    }

    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Wish wish = wishService.addWish(token, wishRequest);
        return ResponseEntity.ok(wish);
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        wishService.removeWish(token, wishRequest.getProductId());
        return ResponseEntity.ok().build();
    }
}