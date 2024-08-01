package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Entity.Wishlist;
import gift.Model.MemberDto;
import gift.Model.WishlistDto;
import gift.Service.ProductService;
import gift.Service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wishlist", description = "위시 리스트 관련 api")
public class WishlistController {

    private final WishlistService wishlistService;
    private final ProductService productService;

    @Autowired
    public WishlistController(WishlistService wishlistService, ProductService productService) {
        this.wishlistService = wishlistService;
        this.productService = productService;
    }

    @GetMapping("/")
    @Operation(summary = "위시 리스트 조회", description = "위시 리스트를 조회합니다.")
    public ResponseEntity<?> getWishlist(@LoginMemberResolver MemberDto memberDto,
                                         @RequestParam(value = "page", defaultValue = "0") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishlistDto> paging = wishlistService.getWishlistByPage(memberDto, pageable);
        return ResponseEntity.ok(paging);
    }

    @PostMapping("/")
    @Operation(summary = "위시 리스트 추가", description = "위시 리스트에 상품을 추가합니다.")
    public ResponseEntity<?> addWishlistItem(@LoginMemberResolver MemberDto memberDto, @RequestBody WishlistDto wishlistDto) {
        if (memberDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }
        wishlistDto.setMemberId(memberDto.getId());
        wishlistService.addWishlistItem(wishlistDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{wishId}")
    @Operation(summary = "위시 리스트 삭제", description = "위시 리스트에서 상품을 삭제합니다.")
    public ResponseEntity<?> removeWishlistItem(@LoginMemberResolver MemberDto memberDto, @RequestBody WishlistDto wishlistDto, @PathVariable Long wishId) {
        if(memberDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Token");
        }

        if(productService.getProductById(wishlistDto.getProductId()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 상품입니다");
        }

        wishlistService.removeWishlistItem(wishId);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wishlist/clear")
    @Operation(summary = "위시 리스트 비우기", description = "위시 리스트를 비웁니다.")
    public ResponseEntity<?> clearWishlist(@LoginMemberResolver MemberDto memberDto) {
        wishlistService.clearWishlist(memberDto.getId());
        return ResponseEntity.ok().build();
    }

}
