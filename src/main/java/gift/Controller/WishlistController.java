package gift.Controller;

import gift.Annotation.LoginMemberResolver;
import gift.Entity.Wishlist;
import gift.Model.MemberDto;
import gift.Model.WishlistDto;
import gift.Service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@Tag(name = "Wishlist", description = "위시 리스트 관련 api")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping("/wishlist")
    @Operation(summary = "위시 리스트 조회", description = "위시 리스트를 조회합니다.")
    public String getWishlist(@LoginMemberResolver MemberDto memberDto, Model model,
                              @RequestParam(value = "page", defaultValue = "0") int page,
                              @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<WishlistDto> paging = wishlistService.getWishlistByPage(memberDto, pageable);
        model.addAttribute("paging", paging);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paging.getTotalPages());
        return "wishlist";
    }

    @PostMapping("/wishlist/add")
    @Operation(summary = "위시 리스트 추가", description = "위시 리스트에 상품을 추가합니다.")
    public String addWishlistItem(@LoginMemberResolver MemberDto memberDto, @RequestBody WishlistDto wishlistDto) {
        if (memberDto == null) {
            return "redirect:/login";
        }
        wishlistDto.setUserId(memberDto.getId());
        wishlistService.addWishlistItem(wishlistDto);
        return "redirect:/products";
    }

    @PostMapping("/wishlist/remove")
    @Operation(summary = "위시 리스트 삭제", description = "위시 리스트에서 상품을 삭제합니다.")
    public String removeWishlistItem(@LoginMemberResolver MemberDto memberDto, @RequestBody WishlistDto wishlistDto) {
        if (memberDto == null) {
            return "redirect:/login";
        }

        Optional<Wishlist> wishlistOptional = wishlistService.getWishlist(memberDto.getId()).stream()
                .filter(wishlist -> wishlist.getProductId() == wishlistDto.getProductId())
                .findFirst();
        wishlistDto.setUserId(memberDto.getId());

        wishlistService.removeWishlistItem(wishlistDto, wishlistOptional.get());

        return "redirect:/wishlist";
    }

    @PostMapping("/wishlist/clear")
    @Operation(summary = "위시 리스트 비우기", description = "위시 리스트를 비웁니다.")
    public ResponseEntity<?> clearWishlist(@LoginMemberResolver MemberDto memberDto) {
        wishlistService.clearWishlist(memberDto.getId());
        return ResponseEntity.ok().build();
    }

}
