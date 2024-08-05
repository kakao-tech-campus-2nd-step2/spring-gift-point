package gift.controller.wish;

import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.dto.wish.WishRequest;
import gift.dto.wish.WishResponse;
import gift.model.user.User;
import gift.service.wish.WishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/wishes")
public class WishListController implements WishListSpecification {

    private final WishService wishService;

    @Autowired
    public WishListController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping()
    public ResponseEntity<String> addGiftToWishList(@RequestAttribute("user") User user,
                                                    @RequestBody WishRequest.Create wishRequest,
                                                    @RequestParam(required = false, defaultValue = "1") int quantity) {
        wishService.addGiftToUser(user, wishRequest.productId(), quantity);
        return ResponseEntity.ok("위시리스트에 상품이 추가되었습니다.");
    }

    @PutMapping("/{giftId}")
    public ResponseEntity<String> updateGiftQuantity(@RequestAttribute("user") User user,
                                                     @PathVariable Long giftId,
                                                     @RequestParam(name = "quantity") int quantity) {
        wishService.updateWishQuantity(user, giftId, quantity);
        return ResponseEntity.ok("위시리스트에서 상품 수량이 변경되었습니다.");
    }

    @DeleteMapping("/{wishId}")
    public ResponseEntity<String> removeGiftFromWishList(@RequestAttribute("user") User user,
                                                         @PathVariable Long wishId) {
        wishService.removeGiftFromUser(user, wishId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<PagingResponse<WishResponse.Info>> getUserGifts(@RequestAttribute("user") User user,
                                                                          @ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<WishResponse.Info> userWishes = wishService.getGiftsFromUser(user, pagingRequest.getPage(), pagingRequest.getSize());
        return ResponseEntity.ok(userWishes);
    }
}