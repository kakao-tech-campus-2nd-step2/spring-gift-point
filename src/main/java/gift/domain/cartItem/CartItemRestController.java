package gift.domain.cartItem;

import gift.domain.cartItem.dto.CartItemDTO;
import gift.domain.member.dto.LoginInfo;
import gift.global.resolver.Login;
import gift.global.response.ResponseMaker;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members/cart")
@Tag(name = "CartItem", description = "CartItem API")
public class CartItemRestController {

    private final CartItemService cartItemService;

    public CartItemRestController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }


    /**
     * 장바구니에 상품 담기
     */
    @PostMapping("/{productId}")
    @Operation(summary = "장바구니에 상품 담기")
    public ResponseEntity<ResultResponseDto<Integer>> addCartItem(
        @Parameter(description = "상품 ID") @PathVariable("productId") Long productId,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        int currentCount = cartItemService.addCartItem(loginInfo.getId(), productId);

        return ResponseMaker.createResponse(HttpStatus.OK,
            "상품이 장바구니에 추가되었습니다. 총 개수: " + currentCount, currentCount);
    }

    /**
     * 장바구니 조회 - 페이징(매개변수별)
     */
    @GetMapping
    @Operation(summary = "장바구니 조회 - 페이징")
    public ResponseEntity<ResultResponseDto<List<CartItemDTO>>> getProductsInCartByUserIdAndPageAndSort(
        @Parameter(description = "페이지 번호") @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "정렬 기준") @RequestParam(value = "sort", defaultValue = "id_asc") String sort,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        int size = 10; // default
        Sort sortObj = getSortObject(sort);

        List<CartItemDTO> cartItemDTOS = cartItemService.getProductsInCartByMemberIdAndPageAndSort(
            loginInfo.getId(),
            page,
            size,
            sortObj
        );

        return ResponseMaker.createResponse(HttpStatus.OK, "장바구니 조회에 성공했습니다.", cartItemDTOS);
    }

    /**
     * 장바구니 상품 삭제
     */
    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "장바구니 상품 삭제")
    public ResponseEntity<SimpleResultResponseDto> deleteCartItem(
        @Parameter(description = "장바구니 상품(CartItem) ID") @PathVariable("cartItemId") Long cartItemId,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        cartItemService.deleteCartItem(cartItemId);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK, "장바구니에서 상품이 삭제되었습니다.");
    }

    /**
     * 장바구니 상품 수량 변경
     */
    // TODO cartItem 에 userId, productId, + 상품 정보까지 담는걸로
    // 안그러면 productId 로 다시 상품 정보를 불러와야 함..페이징할때도 cartItem 에서 바로 할 수 있으니 나을 것 같다
    @PutMapping("/{cartItemId}")
    @Operation(summary = "장바구니 상품 수량 변경")
    public ResponseEntity<SimpleResultResponseDto> updateCartItem(
        @Parameter(description = "장바구니 상품(CartItem) ID") @PathVariable("cartItemId") Long cartItemId,
        @Parameter(description = "변경할 상품 수량") @RequestParam("count") int count,
        @Parameter(description = "로그인 유저 정보") @Login LoginInfo loginInfo
    ) {
        int modifiedCount = cartItemService.updateCartItem(cartItemId, count);

        return ResponseMaker.createSimpleResponse(HttpStatus.OK,
            "해당 상품의 수량이 변경되었습니다. 총 개수: " + modifiedCount + "개");
    }

    // TODO 페이징 기준을 cartItem 에 맞춰서 수정해야함.. 간단한건 생성날짜? <- 추가 속성 필요
    private Sort getSortObject(String sort) {
        switch (sort) {
            case "price_asc":
                return Sort.by(Sort.Direction.ASC, "price");
            case "price_desc":
                return Sort.by(Sort.Direction.DESC, "price");
            case "name_asc":
                return Sort.by(Sort.Direction.ASC, "name");
            case "name_desc":
                return Sort.by(Sort.Direction.DESC, "name");
            default:
                return Sort.by(Sort.Direction.ASC, "id");
        }
    }
}
