package gift.controller;

import gift.dto.WishDto;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "WishlistController", description = "위시 리스트 관련 API")
@RestController
@RequestMapping("/api/wishes")
public class WishlistController {

  private final WishlistService wishlistService;

  @Autowired
  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
  @PostMapping
  public ResponseEntity<WishDto> addWish(
          @RequestHeader("Authorization") String authorization,
          @RequestBody WishDto wishDto) {
    WishDto createdWish = wishlistService.addWish(wishDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdWish);
  }

  @Operation(summary = "위시 리스트 상품 삭제", description = "회원의 위시 리스트에서 상품을 삭제한다.")
  @DeleteMapping("/{wishId}")
  public ResponseEntity<Void> deleteWish(
          @RequestHeader("Authorization") String authorization,
          @PathVariable Long wishId) {
    wishlistService.deleteWish(wishId);
    return ResponseEntity.status(HttpStatus.OK).build();
  }

  @Operation(summary = "위시 리스트 상품 조회", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
  @GetMapping
  public ResponseEntity<Page<WishDto>> getWishes(
          @RequestHeader("Authorization") String authorization,
          @RequestParam(defaultValue = "0") int page) {
    Page<WishDto> wishes = wishlistService.getWishes(PageRequest.of(page, 10));
    return ResponseEntity.ok(wishes);
  }
}