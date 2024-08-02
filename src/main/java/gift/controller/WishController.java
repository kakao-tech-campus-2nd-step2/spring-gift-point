package gift.controller;

import gift.config.auth.LoginUser;
import gift.domain.model.dto.WishAddRequestDto;
import gift.domain.model.entity.User;
import gift.domain.model.dto.WishResponseDto;
import gift.domain.model.dto.WishUpdateRequestDto;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/wishes")
@Tag(name = "Wish", description = "위시리스트 관리 API")
public class WishController {

    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<WishResponseDto>> getWishes(@LoginUser User user,
        @RequestParam(defaultValue = "0") @Min(value = 0, message = "페이지 번호는 0 이상이어야 합니다.") int page,
        @RequestParam(defaultValue = "10") @Positive int size,
        @RequestParam(defaultValue = "createdDate,asc") String sort) {
        return ResponseEntity.ok(wishService.getWishes(user, page, size, sort));
    }

    @Operation(summary = "위시리스트에 상품 추가", description = "위시리스트에 새로운 상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<WishResponseDto> addWish(
        @Valid @RequestBody WishAddRequestDto wishAddRequestDto,
        @LoginUser User user) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(wishService.addWish(user, wishAddRequestDto));
    }

    @Operation(summary = "위시리스트 상품 수정", description = "위시리스트에 있는 상품의 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<WishResponseDto> updateWish(
        @PathVariable Long productId,
        @Valid @RequestBody WishUpdateRequestDto wishUpdateRequestDto, @LoginUser User user) {
        return ResponseEntity.ok(wishService.updateWish(productId, user, wishUpdateRequestDto));
    }

    @Operation(summary = "위시리스트에서 상품 삭제", description = "위시리스트에서 지정된 상품을 삭제합니다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteWish(@PathVariable Long productId, @LoginUser User user) {
        wishService.deleteWish(user, productId);
        return ResponseEntity.noContent().build();
    }
}
