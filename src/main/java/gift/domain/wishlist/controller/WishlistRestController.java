package gift.domain.wishlist.controller;

import gift.config.LoginUser;
import gift.domain.user.entity.User;
import gift.domain.wishlist.dto.WishItemRequestDto;
import gift.domain.wishlist.dto.WishItemResponseDto;
import gift.domain.wishlist.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist", description = "위시리스트 API")
public class WishlistRestController {

    private final WishlistService wishlistService;

    public WishlistRestController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    @Operation(summary = "위시리스트 항목 추가", description = "위시리스트에 담을 항목을 생성합니다.")
    public ResponseEntity<WishItemResponseDto> create(
        @Parameter(description = "위시리시트 항목 요청 정보", required = true)
        @RequestBody WishItemRequestDto wishItemRequestDto,
        @Parameter(hidden = true)
        @LoginUser User user
    ) {
        WishItemResponseDto wishItemResponseDto = wishlistService.create(wishItemRequestDto, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishItemResponseDto);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 조회합니다.")
    public ResponseEntity<Page<WishItemResponseDto>> readAll(
        @Parameter(description = "페이징 정보", in = ParameterIn.QUERY)
        Pageable pageable,
        @Parameter(hidden = true)
        @LoginUser User user
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(wishlistService.readAll(pageable, user));
    }

    @DeleteMapping("/{wishItemId}")
    @Operation(summary = "위시리스트 항목 삭제")
    public ResponseEntity<Void> delete(
        @Parameter(description = "위시리스트 항목 ID", in = ParameterIn.PATH, required = true)
        @PathVariable("wishItemId") long wishItemId,
        @Parameter(hidden = true)
        @LoginUser User user
    ) {
        wishlistService.delete(wishItemId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    @Operation(summary = "위시리스트 전체 삭제", description = "회원의 위시리스트를 전부 비웁니다.")
    public ResponseEntity<Void> deleteAllByUser(
        @Parameter(hidden = true)
        @LoginUser User user
    ) {
        wishlistService.deleteAllByUserId(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
