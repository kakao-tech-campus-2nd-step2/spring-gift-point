package gift.controller;

import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.DTO.Wish.WishProductRequest;
import gift.DTO.Wish.WishProductResponse;
import gift.security.AuthenticateMember;
import gift.service.ProductService;
import gift.service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Void> createWishList(
            @PathVariable("productId") Long id, @AuthenticateMember UserResponse userRes
    ){
        ProductResponse productRes = productService.readOneProduct(id);

        WishProductRequest wishProduct = new WishProductRequest(userRes, productRes);
        wishListService.addWishList(wishProduct);

        return new ResponseEntity<>(HttpStatus.OK);
    }
    /*
     * 위시 리스트 조회
     */
    @GetMapping("api/wishes")
    public ResponseEntity<Page<WishProductResponse>> readWishList(
            @AuthenticateMember UserResponse user,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "field", defaultValue = "id") String field
    ){
        if(sort.equals("asc")){
            Page<WishProductResponse> wishList = wishListService.findWishListASC(user.getId(), page, size, field);
            return new ResponseEntity<>(wishList, HttpStatus.OK);
        }
        Page<WishProductResponse> wishList = wishListService.findWishListDESC(user.getId(), page, size, field);
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
