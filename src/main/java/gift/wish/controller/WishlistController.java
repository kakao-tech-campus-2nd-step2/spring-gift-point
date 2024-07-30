package gift.wish.controller;

import gift.wish.domain.WishlistRequest;
import gift.wish.domain.WishlistResponse;
import gift.wish.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wish")
@Tag(name = "위시 리스트 API")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "사용자 위시리스트 상품조회")
    public ResponseEntity<Page<WishlistResponse>> getUserWishlist(@PathVariable("id") Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<WishlistResponse> wishlist = wishlistService.getWishlistResponseByUserId(userId, pageable);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }
    @GetMapping
    @Operation(summary = "위시리스트 상품조회")
    public ResponseEntity<Page<WishlistResponse>> getWishlist(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "createdDate,desc") String sort) {
        String[] sortParams = sort.split(",");
        Sort.Direction direction = Sort.Direction.fromString(sortParams[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortParams[0]));

        Page<WishlistResponse> wishlist = wishlistService.getWishlistResponse(pageable);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }
    @PostMapping
    @Operation(summary = "위시리스트 상품 추가")
    public ResponseEntity<WishlistResponse> addWish(@RequestBody WishlistRequest wishlistRequest){
        try{
            return new ResponseEntity<>(wishlistService.addWish(wishlistRequest), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 상품 삭제")
    public ResponseEntity<Void> deleteWish(@PathVariable("wishId") Long wishId){
        wishlistService.deleteById(wishId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
