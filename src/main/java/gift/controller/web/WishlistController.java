package gift.controller.web;

import gift.dto.Request.AddToWishlistRequest;
import gift.dto.Response.WishlistResponse;
import gift.dto.WishlistDTO;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/wishlist")
@Tag(name = "Wishlist API", description = "위시리스트 관련 API")
public class WishlistController {

    private final WishlistService wishlistService;

    @Autowired
    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @GetMapping
    @Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 조회합니다.")
    public String getWishlist(Principal principal, Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "productName") String sort,
        @RequestParam(defaultValue = "asc") String direction) {

        String username = principal.getName();
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<WishlistDTO> wishlistPage = wishlistService.getWishlistByUser1(username, pageable);

        model.addAttribute("wishlistPage", wishlistPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        return "wishlist";
    }

    @PostMapping("/add")
    @ResponseBody
    @Operation(summary = "위시리스트 추가", description = "상품을 위시리스트에 추가합니다.")
    public ResponseEntity<WishlistResponse> addToWishlist(@RequestBody AddToWishlistRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new WishlistResponse(false));
        }
        String username = principal.getName();
        WishlistResponse response = wishlistService.addToWishlist(username, request.getProductId(), request.getQuantity(), request.getOptions());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    @ResponseBody
    @Operation(summary = "위시리스트 수량 수정", description = "위시리스트에 있는 상품의 수량을 수정합니다.")
    public ResponseEntity<WishlistResponse> updateQuantity(@PathVariable("id") Long id, @RequestParam("quantity") int quantity, @RequestParam("optionId") Long optionId) {
        WishlistResponse response = wishlistService.updateQuantity(id, quantity, optionId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete/{id}")
    @ResponseBody
    @Operation(summary = "위시리스트 삭제", description = "위시리스트에서 상품을 삭제합니다.")
    public ResponseEntity<WishlistResponse> removeFromWishlist(@PathVariable("id") Long id) {
        WishlistResponse response = wishlistService.removeFromWishlist(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/order/{id}")
    @ResponseBody
    @Operation(summary = "위시리스트 주문", description = "위시리스트의 상품을 주문합니다.")
    public ResponseEntity<String> orderWishlist(@PathVariable("id") Long id) {
        wishlistService.orderWishlist(id);
        return ResponseEntity.ok("주문이 완료되었습니다.");
    }
}
