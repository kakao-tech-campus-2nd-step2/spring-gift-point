package gift.controller;

import gift.dto.ProductResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.LoginMember;
import gift.model.Member;
import gift.model.Product;
import gift.service.WishService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping("/wishes")
    public ResponseEntity makeWish(@RequestBody @Valid WishRequest request, @LoginMember Member member) {
        wishService.makeWish(request, member);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/wishes")
    public ResponseEntity<Page<WishResponse>> getAllWishProductsByMember(@LoginMember Member member, @PageableDefault(page = 0, size = 5) Pageable pageable) {
        Page<WishResponse> wishesByMember = wishService.getWishesByMember(member, pageable);
        return ResponseEntity.ok().body(wishesByMember);
    }

    @DeleteMapping("/wishes/{id}")
    public ResponseEntity deleteWishProduct(@PathVariable("id") Long id, @LoginMember Member member) {
        wishService.deleteWish(id, member);
        return ResponseEntity.noContent().build();
    }
}
