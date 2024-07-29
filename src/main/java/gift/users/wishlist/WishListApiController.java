package gift.users.wishlist;

import gift.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "wishlist API", description = "wishlist related API")
public class WishListApiController {

    private final WishListService wishListService;

    public WishListApiController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @GetMapping("/{userId}")
    @Operation(summary = "get all user wishlist", description = "회원 아이디로 회원의 모든 위시리스트를 조회합니다.")
    public ResponseEntity<Page<WishListDTO>> getWishList(@PathVariable("userId") long userId,
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy,
        @RequestParam(value = "sortDirection", required = false, defaultValue = "asc") String sortDirection) {

        size = PageUtil.validateSize(size);
        sortBy = PageUtil.validateSortBy(sortBy, Arrays.asList("id", "productId", "num"));
        Direction direction = PageUtil.validateDirection(sortDirection);
        Page<WishListDTO> wishLists = wishListService.getWishListsByUserId(userId, page, size,
            direction, sortBy);

        return ResponseEntity.ok(wishLists);
    }

    @PostMapping("/{userId}")
    @Operation(summary = "add one wishlist", description = "회원 아이디로 회원의 위시리스트에 하나의 위시리스트를 추가합니다. required "
        + "info(productId, num, optionId)")
    public ResponseEntity<WishListDTO> addWishList(@PathVariable("userId") long userId,
        @Valid @RequestBody WishListDTO wishListDTO) {

        WishListDTO result = wishListService.addWishList(wishListDTO, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @PutMapping("/{userId}/{wishListId}")
    @Operation(summary = "update one wishlist", description = "회원 아이디와 위시리스트 아이디로 회원의 위시리스트를 수정합니다.")
    public ResponseEntity<WishListDTO> updateWishList(@PathVariable("userId") long userId,
        @PathVariable("wishListId") long wishListId,
        @Valid @RequestBody WishListDTO wishListDTO) {

        WishListDTO result = wishListService.updateWishList(userId, wishListId,
            wishListDTO);
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/{userId}/{wishListId}")
    @Operation(summary = "delete one wishlist", description = "회원 아이디와 위시리스트 아이디로 하나의 위시리스트를 삭제합니다.")
    public ResponseEntity<Void> deleteWishListByWishListId(@PathVariable("userId") long userId,
        @PathVariable("wishListId") long wishListId) {

        wishListService.deleteWishListByWishListId(userId, wishListId);
        return ResponseEntity.ok().build();
    }
}
