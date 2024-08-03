package gift.doamin.wishlist.controller;

import gift.doamin.user.dto.UserDto;
import gift.doamin.wishlist.dto.WishRequest;
import gift.doamin.wishlist.dto.WishResponse;
import gift.doamin.wishlist.service.WishListService;
import gift.global.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "위시리스트")
@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @Operation(summary = "위시리스트 등록", description = "새로운 위시리스트 항목을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WishResponse addWish(@Valid @RequestBody WishRequest wishRequest,
        @LoginUser UserDto user) {
        return wishListService.create(user.getId(), wishRequest);
    }

    @Operation(summary = "위시리스트 조회", description = "로그인된 사용자의 위시리스트를 조회합니다.")
    @GetMapping
    public Page<WishResponse> getWishList(@LoginUser UserDto user,
        @ParameterObject Pageable pageable) {
        return wishListService.getPage(user.getId(), pageable);
    }

    @Operation(summary = "위시리스트 항목 수정", description = "위시리스트의 한 항목의 수량을 수정합니다.")
    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public WishResponse updateWish(@Valid @RequestBody WishRequest wishRequest,
        @LoginUser UserDto user) {
        return wishListService.update(user.getId(), wishRequest);
    }

    @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트의 한 항목을 삭제합니다.")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWish(@Valid @RequestBody WishRequest wishRequest, Principal principal) {
        Long userId = Long.parseLong(principal.getName());
        wishListService.update(userId, wishRequest);
    }
}
