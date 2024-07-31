package gift.doamin.wishlist.controller;

import gift.doamin.wishlist.dto.WishForm;
import gift.doamin.wishlist.dto.WishParam;
import gift.doamin.wishlist.service.WishListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위시리스트")
@RestController
@RequestMapping("/api/wishlist")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "위시리스트 등록", description = "새로운 위시리스트 항목을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addWish(@Valid @RequestBody WishForm wishForm, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        wishListService.create(userId, wishForm);
    }

    @Operation(summary = "위시리스트 조회", description = "로그인된 사용자의 위시리스트를 조회합니다. 한 번에 5개씩 페이지별로 조회할 수 있습니다.")
    @GetMapping
    public Page<WishParam> getWishList(Principal principal,
        @RequestParam(required = false, defaultValue = "0", name = "page") int pageNum) {

        Long userId = Long.parseLong(principal.getName());
        return wishListService.getPage(userId, pageNum);
    }

    @Operation(summary = "위시리스트 항목 수정", description = "위시리스트의 한 항목의 수량을 수정합니다.")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateWish(@Valid @RequestBody WishForm wishForm, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        wishListService.update(userId, wishForm);
    }

    @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트의 한 항목을 삭제합니다.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWish(@Valid @RequestBody WishForm wishForm, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        wishListService.update(userId, wishForm);
    }
}
