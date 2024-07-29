package gift.wish.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import gift.user.dto.UserDto;
import gift.wish.dto.WishDto;
import gift.security.LoginMember;
import gift.wish.service.WishService;
import gift.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishes")
@Tag(name = "Wish API", description = "위시리스트 관련 API")
public class WishController {

  private final WishService wishService;

  @Autowired
  public WishController(WishService wishService) {
    this.wishService = wishService;
  }

  @GetMapping
  @Operation(summary = "모든 위시리스트 항목 조회")
  public ResponseEntity<Page<WishDto>> getAllWishes(
      @LoginMember User user,
      @RequestParam(defaultValue = "0") @Parameter(description = "페이지 번호", example = "0") int page,
      @RequestParam(defaultValue = "10") @Parameter(description = "페이지 크기", example = "10") int size,
      @RequestParam(defaultValue = "id") @Parameter(description = "정렬 필드", example = "id") String sort,
      @RequestParam(defaultValue = "asc") @Parameter(description = "정렬 방향", example = "asc") String direction) {


    Sort.Direction sortDirection = Sort.Direction.fromString(direction);
    Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sort));
    Page<WishDto> wishes = wishService.getWishesByMemberEmail(user.getEmail(), pageable);
    return ResponseEntity.ok(wishes);
  }

  @PostMapping
  @Operation(summary = "위시리스트 추가")
  public ResponseEntity<WishDto> addWish(
      @RequestBody @Parameter(description = "위시리스트 데이터", required = true) WishDto wishDto,
      @LoginMember User member) {
    wishDto.setUser(
        new UserDto(member.getId(), member.getEmail(), member.getPassword(), member.getRole()));
    WishDto createdWish = wishService.addWish(wishDto);
    return ResponseEntity.ok(createdWish);
  }

  @DeleteMapping("/{productId}")
  @Operation(summary = "위시리스트 항목 삭제", description = "특정 상품 ID를 가진 위시리스트 항목을 삭제")
  public ResponseEntity<Void> removeWish(
      @PathVariable @Parameter(description = "상품 ID", required = true) Long productId,
      @LoginMember User member) {
    wishService.removeWish(member.getEmail(), productId);
    return ResponseEntity.ok().build();
  }
}

