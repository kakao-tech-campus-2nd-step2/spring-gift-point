package gift.controller.rest;

import gift.model.response.MessageResponseDTO;
import gift.model.wishlist.WishlistResponse;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wishlist 컨트롤러", description = "Wishlist API입니다.")
@RestController
@RequestMapping("/api/wishes")
public class WishlistController {


    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Operation(summary = "위시리스트 조회", description = "위시리스트의 상품을 조회합니다.")
    @GetMapping()
    public List<WishlistResponse> getWishlist(Pageable pageable, HttpSession session) {
        String email = (String) session.getAttribute("email");
        return wishlistService.getWishlistProducts(email, pageable).getContent();
    }

    @Operation(summary = "위시리스트 저장", description = "위시리스트에 상품을 저장합니다.")
    @PostMapping("/{productId}")
    public ResponseEntity<MessageResponseDTO> postWishlist(@PathVariable("productId") Long productId, HttpSession session) {
        String email = (String) session.getAttribute("email");
        wishlistService.addWishlistProduct(email, productId);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Product added to wishlist successfully"));
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트를 삭제합니다.")
    @DeleteMapping(path = "/{productId}")
    public ResponseEntity<MessageResponseDTO> deleteWishlist(@PathVariable("productId") Long id, HttpSession session) {
        String email = (String) session.getAttribute("email");
        wishlistService.deleteWishlist(email, id);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Wishlist deleted successfully"));
    }

    private MessageResponseDTO makeMessageResponse(String message) {
        return new MessageResponseDTO(message);
    }
}
