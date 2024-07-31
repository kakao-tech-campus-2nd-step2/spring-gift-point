package gift.controller.wish;

import gift.common.annotation.LoginUser;
import gift.common.auth.LoginInfo;
import gift.common.dto.PageResponse;
import gift.controller.wish.dto.WishRequest;
import gift.controller.wish.dto.WishResponse;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wish", description = "위시리스트 API")
@SecurityRequirement(name = "Authorization")
@RestController
@RequestMapping("/api/wishes")
public class WishListController {

    private final WishService wishService;

    public WishListController(WishService wishService) {
        this.wishService = wishService;
    }

    @GetMapping("")
    @Operation(summary = "전체 위시리스트 조회", description = "전체 위시리스트를 조회합니다.")
    public ResponseEntity<PageResponse<WishResponse>> getAllWishList(
        @LoginUser LoginInfo user,
        @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        PageResponse<WishResponse> responses = wishService.findAllWish(user.id(), pageable);
        return ResponseEntity.ok().body(responses);
    }

    @PostMapping("")
    @Operation(summary = "위시리스트 등록", description = "위시리스트를 등록합니다.")
    public ResponseEntity<Void> addWishProduct(@LoginUser LoginInfo user,
        @Valid @RequestBody WishRequest.Create request) {
        Long id = wishService.addWistList(user.id(), request);
        return ResponseEntity.created(URI.create("/api/wishes/" + id)).build();
    }

    @PatchMapping("/{wishId}")
    @Operation(summary = "위시리스트 수정", description = "위시리스트를 수정합니다.")
    public ResponseEntity<String> updateWishProduct(@PathVariable("wishId") Long wishId,
        @LoginUser LoginInfo user,
        @Valid @RequestBody WishRequest.Update request) {
        wishService.updateWishList(user.id(), wishId, request);
        return ResponseEntity.ok().body("위시리스트에 상품이 수정되었습니다.");
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시리스트 삭제", description = "위시리스트를 삭제합니다.")
    public ResponseEntity<Void> deleteWishProduct(@PathVariable("wishId") Long wishId,
        @LoginUser LoginInfo user) {
        wishService.deleteWishList(user.id(), wishId);
        return ResponseEntity.ok().build();
    }
}
