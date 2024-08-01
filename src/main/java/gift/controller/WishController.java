package gift.controller;

import gift.dto.WishRequest;
import gift.entity.Wish;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name="위시리스트 API")
@RequestMapping("/api/wishes")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 조회")
    @GetMapping
    public ResponseEntity<Page<Wish>> getWishes(Pageable pageable) {
        Page<Wish> wishes = wishService.getWishes(pageable);
        return ResponseEntity.ok(wishes);
    }

    @Operation(summary = "위시리스트 추가")
    @PostMapping
    public ResponseEntity<Wish> addWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        Wish wish = wishService.addWish(token, wishRequest);
        return ResponseEntity.ok(wish);
    }

    @Operation(summary = "위시리스트 삭제")
    @DeleteMapping("/{wishId}")
    public ResponseEntity<Void> removeWish(@RequestBody WishRequest wishRequest, @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String token) {
        wishService.removeWish(token, wishRequest.getProductId());
        return ResponseEntity.ok().build();
    }
}