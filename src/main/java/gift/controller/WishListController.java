package gift.controller;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.DTO.Wish.WishProductRequest;
import gift.DTO.Wish.WishProductResponse;
import gift.security.AuthenticateMember;
import gift.service.ProductService;
import gift.service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WishListController {
    private final WishListService wishListService;
    private final ProductService productService;

    public WishListController(WishListService wishListService, ProductService productService){
        this.wishListService = wishListService;
        this.productService = productService;
    }
    /*
     * 위시 리스트 추가
     */
    @PostMapping("api/wishes/{productId}")
    public ResponseEntity<String> createWishList(
            @PathVariable("productId") Long id, @AuthenticateMember UserResponse userRes
    ){
        ProductResponse productRes = productService.readOneProduct(id);

        WishProductRequest wishProduct = new WishProductRequest(userRes, productRes);
        wishListService.addWishList(wishProduct);

        String message = "위시리스트에 추가했습니다!";
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }
    /*
     * 위시 리스트 조회
     */
    @GetMapping("api/wishes")
    public ResponseEntity<Page<WishProductResponse>> readWishList(
            Pageable pageable,
            @AuthenticateMember UserResponse user
    ){
        Page<WishProductResponse> wishList = wishListService.findWishList(user.getId(), pageable);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }
    /*
     * 위시 리스트 삭제
     */
    @DeleteMapping("api/wishes/{wishId}")
    public ResponseEntity<Void> deleteWishProduct(
            @PathVariable("wishId") Long wishId,
            @AuthenticateMember UserResponse user
    ){
        wishListService.deleteWishProduct(wishId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
