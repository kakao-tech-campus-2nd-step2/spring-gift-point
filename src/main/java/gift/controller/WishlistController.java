package gift.controller;

import gift.annotation.LoginMember;
import gift.model.Wishlist;
import gift.model.Product;
import gift.service.WishlistService;
import gift.model.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "WishlistController", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  public WishlistController(WishlistService wishlistService) {
    this.wishlistService = wishlistService;
  }

  @Operation(summary = "위시리스트 조회", description = "회원의 위시리스트를 페이지네이션하여 조회합니다.")
  @GetMapping
  public ResponseEntity<Page<Wishlist>> getWishlist(@RequestParam(defaultValue = "0") int page,
                                                    @RequestParam(defaultValue = "10") int size,
                                                    @LoginMember Member member) {
    Page<Wishlist> wishlist = wishlistService.getWishlist(member.getId(), page, size);
    return ResponseEntity.ok(wishlist);
  }

  @Operation(summary = "위시리스트 항목 추가", description = "회원의 위시리스트에 새로운 항목을 추가합니다.")
  @PostMapping
  public ResponseEntity<Wishlist> addWishlistItem(@RequestBody Map<String, Object> body, @LoginMember Member member) {
    Wishlist savedWishlist = wishlistService.addWishlistItem(member, body);
    return ResponseEntity.ok(savedWishlist);
  }

  @Operation(summary = "위시리스트 항목 삭제", description = "위시리스트에서 특정 항목을 삭제합니다.")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> removeWishlistItem(@PathVariable Long id) {
    wishlistService.removeWishlistItem(id);
    return ResponseEntity.noContent().build();
  }
}