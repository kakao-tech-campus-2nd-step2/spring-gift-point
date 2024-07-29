package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.WishPageResponseDto;
import gift.dto.WishRequestDto;
import gift.dto.WishResponseDto;
import gift.entity.User;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wishes", description = "위시리스트 관련 API")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService) {
        this.wishService = wishService;
    }

    @PostMapping
    @Operation(summary = "위시리스트 추가", description = "위시리스트에 새로운 상품을 추가합니다.")
    public ResponseEntity<WishResponseDto> addWish(@LoginMember User loginUser, @RequestBody WishRequestDto wishRequestDto) {
        WishResponseDto createdWish = wishService.addWish(loginUser.getId(), wishRequestDto);
        return new ResponseEntity<>(createdWish, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "사용자의 모든 위시리스트 항목을 조회합니다.")
    public ResponseEntity<WishPageResponseDto> getWishesByUserId(@LoginMember User loginUser, Pageable pageable) {
        WishPageResponseDto wishList = wishService.getWishesByUserId(loginUser.getId(), pageable);
        return new ResponseEntity<>(wishList, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트에서 특정 상품을 삭제합니다.")
    public ResponseEntity<Void> deleteWish(@PathVariable Long id) {
        wishService.deleteWish(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
