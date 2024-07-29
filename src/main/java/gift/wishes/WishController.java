package gift.wishes;


import gift.jwt.Login;
import gift.member.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "wish", description = "장바구니 관련 API")
@Controller
@RequestMapping("/wish")
public class WishController {
    private final WishService wishService;

    public WishController(WishService wishService){
        this.wishService = wishService;
    }

    @Operation(summary = "장바구니 조회", description = "조회된 장바구니 목록 반환함")
    @GetMapping()
    public ResponseEntity<List<WishResponse>> getWishList(@Login UserDTO userDTO){
        return ResponseEntity.ok(wishService.findByMemberId(userDTO.getUserId()));
    }

    @Operation(summary = "장바구니 담기", description = "장바구니에 상품 담기")
    @PostMapping()
    public ResponseEntity<HttpStatus> createWish(@RequestBody WishRequest wishRequest, @Login UserDTO userDTO){
        wishService.createWish(userDTO.getUserId(), wishRequest.getProductId(),
            wishRequest.getQuantity());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "장바구니 상품 수량 변경", description = "조회된 장바구니의 상품 수량 변경")
    @PutMapping()
    public ResponseEntity<HttpStatus> updateQuantity(@RequestBody WishRequest wishRequest, @Login UserDTO userDTO){
        wishService.updateQuantity(userDTO.getUserId(), wishRequest.getProductId(),
            wishRequest.getQuantity());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Operation(summary = "장바구니 상품 삭제", description = "장바구니에서 상품 빼기")
    @DeleteMapping()
    public ResponseEntity<HttpStatus> deleteWish(@RequestBody Long id, @Login UserDTO userDTO){
        wishService.deleteWish(id, userDTO.getUserId());
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @Operation(summary = "장바구니 조회", description = "위시리스트 페이지로 반환함")
    @GetMapping("/page")
    public ResponseEntity<WishPageResponse> getWishListPage(@Login UserDTO userDTO, @RequestParam(value="page", defaultValue="0")int page){
        return ResponseEntity.ok(wishService.getWishPage(userDTO.getUserId(), page));
    }
}
