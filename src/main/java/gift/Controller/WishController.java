package gift.Controller;

import gift.DTO.MemberDto;
import gift.DTO.WishListDto;
import gift.LoginUser;
import gift.Service.WishListService;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/wishes")
public class WishController {

  private final WishListService wishListService;

  public WishController(WishListService wishListService) {
    this.wishListService = wishListService;
  }

  @GetMapping
  public ResponseEntity<Page<WishListDto>> getWishList(
    @PageableDefault(size = 10, sort = "wishId", direction = Direction.DESC) Pageable pageable) {
    Page<WishListDto> wishList = wishListService.getWishList(pageable);

    return ResponseEntity.ok(wishList);
  }

  @PostMapping
  public ResponseEntity<WishListDto> addProductToWishList(@RequestBody Long productId,
    @LoginUser MemberDto memberDto) {

    wishListService.addProductToWishList(productId);
    return ResponseEntity.created(URI.create("/productId")).build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProductToWishList(@PathVariable Long id) {
    wishListService.deleteProductToWishList(id);
    return ResponseEntity.ok().build();

  }

}
