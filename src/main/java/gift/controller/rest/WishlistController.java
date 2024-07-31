package gift.controller.rest;

import gift.entity.MessageResponseDTO;
import gift.entity.Product;
import gift.entity.WishlistDTO;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Wishlist 컨트롤러", description = "Wishlist API입니다.")
@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {


    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @Operation(summary = "위시리스트 조회", description = "위시리스트의 상품을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트의 모든 상품 조회 성공",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @GetMapping()
    public List<Product> getWishlist(HttpServletRequest request,
                                     Pageable pageable) {
        String email = (String) request.getAttribute("email");
        return wishlistService.getWishlistProducts(email, pageable).getContent();
    }

    @Operation(summary = "위시리스트 저장", description = "위시리스트에 상품을 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트에 상품 저장 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @PostMapping(consumes = "application/json")
    public ResponseEntity<MessageResponseDTO> postWishlist(HttpServletRequest request, @RequestBody @Valid WishlistDTO form) {
        String email = (String) request.getAttribute("email");
        wishlistService.addWishlistProduct(email, form);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Product added to wishlist successfully"));
    }

    @Operation(summary = "위시리스트 삭제", description = "위시리스트를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class))),
            @ApiResponse(responseCode = "401", description = "인가 기능이 확인되지 않은 접근",
                    content = @Content(schema = @Schema(implementation = MessageResponseDTO.class)))
    })
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<MessageResponseDTO> deleteWishlist(@PathVariable("id") Long id, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        wishlistService.deleteWishlist(email, id);
        return ResponseEntity
                .ok()
                .body(makeMessageResponse("Wishlist deleted successfully"));
    }

    private MessageResponseDTO makeMessageResponse(String message) {
        return new MessageResponseDTO(message);
    }
}
