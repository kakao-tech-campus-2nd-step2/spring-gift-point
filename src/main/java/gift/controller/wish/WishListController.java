package gift.controller.wish;

import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.dto.wish.WishRequest;
import gift.dto.wish.WishResponse;
import gift.model.user.User;
import gift.service.product.ProductService;
import gift.service.wish.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishes")
public class WishListController implements WishListSpecification {

    private final WishService wishService;
    private final ProductService productService;

    @Autowired
    public WishListController(WishService wishService, ProductService productService) {
        this.wishService = wishService;
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<String> addGiftToCart(@RequestAttribute("user") User user,
                                                @RequestBody WishRequest.Create wishRequest,
                                                @RequestParam(required = false, defaultValue = "1") int quantity) {
        wishService.addGiftToUser(user.getId(), wishRequest.productId(), quantity);
        return ResponseEntity.ok("위시리스트에 상품이 추가되었습니다.");
    }

    @PutMapping("/{giftId}")
    public ResponseEntity<String> updateGiftQuantity(@RequestAttribute("user") User user,
                                                     @PathVariable Long giftId,
                                                     @RequestParam(name = "quantity") int quantity) {
        wishService.updateWishQuantity(user.getId(), giftId, quantity);
        return ResponseEntity.ok("위시리스트에서 상품 수량이 변경되었습니다.");
    }

    @DeleteMapping("/{giftId}")
    public ResponseEntity<String> removeGiftFromCart(@RequestAttribute("user") User user,
                                                     @PathVariable Long giftId) {
        wishService.removeGiftFromUser(user.getId(), giftId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagingResponse<WishResponse.Info>> getUserGifts(@RequestAttribute("user") User user,
                                                                     @ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<WishResponse.Info> userWishes = wishService.getGiftsFromUser(user.getId(), pagingRequest.getPage(), pagingRequest.getSize());
        return ResponseEntity.ok(userWishes);
    }
}