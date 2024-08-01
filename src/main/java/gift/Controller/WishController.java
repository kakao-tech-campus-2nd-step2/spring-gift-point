package gift.Controller;

import gift.DTO.MemberDto;
import gift.DTO.WishListDto;
import gift.LoginUser;
import gift.Service.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @PageableDefault(size = 5) Pageable pageable) {
    Page<WishListDto> wishList = wishListService.getWishList(pageable);

    return ResponseEntity.ok(wishList);
  }

  @PostMapping
  public ResponseEntity<WishListDto> addProductToWishList(@RequestBody WishListDto wishListDto,
    @LoginUser MemberDto memberDto) {

    WishListDto addedWishProduct = wishListService.addProductToWishList(wishListDto);

    // 생성된 리소스의 URI를 빌드
    var location = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(addedWishProduct.getId())
      .toUri();

    return ResponseEntity.created(location).body(addedWishProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProductToWishList(@PathVariable Long id) {
    wishListService.deleteProductToWishList(id);
    return ResponseEntity.noContent().build();

  }

}
