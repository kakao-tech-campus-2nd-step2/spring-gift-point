package gift.wish.controller;

import gift.wish.dto.WishRequestDto;
import gift.wish.dto.WishResponseDto;
import gift.wish.dto.WishSortField;
import gift.security.LoginMember;
import gift.wish.service.WishService;
import gift.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

  private final WishService wishService;

  @Autowired
  public WishController(WishService wishService) {
    this.wishService = wishService;
  }

  @GetMapping
  @Operation(summary = "위시 리스트 상품 조회")
  public ResponseEntity<Page<WishResponseDto>> getAllWishes(
      @LoginMember User user,
      @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
      @RequestParam(defaultValue = "CREATED_AT") @Parameter(description = "정렬 필드", example = "id") WishSortField sort,
      @RequestParam(defaultValue = "desc") @Parameter(description = "정렬 방향", example = "asc") Sort.Direction direction) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort.getFieldName()));
    Page<WishResponseDto> wishes = wishService.getWishesByUserEmail(user.getEmail(), pageable);
    return ResponseEntity.ok(wishes);
  }

  @PostMapping
  @Operation(summary = "위시 리스트 상품 추가")
  public ResponseEntity<WishResponseDto> addWish(
      @RequestBody @Parameter(description = "위시리스트 데이터", required = true) WishRequestDto wishRequestDto,
      @LoginMember User user) {

    WishResponseDto wishResponseDto = wishService.addWish(user.getId(), wishRequestDto);
    return new ResponseEntity<>(wishResponseDto, HttpStatus.CREATED);
  }

  @DeleteMapping("/{wishId}")
  @Operation(summary = "위시 리스트 상품 삭제")
  public ResponseEntity<WishResponseDto> removeWish(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long wishId,
      @LoginMember User user) {

    WishResponseDto wishResponseDto = wishService.removeWish(user.getId(), wishId);
    return ResponseEntity.ok(wishResponseDto);
  }
}

