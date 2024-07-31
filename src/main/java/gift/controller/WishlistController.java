package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.ErrorResponse;
import gift.dto.WishRequest;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.service.MemberService;
import gift.service.ProductService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wishes")
@Tag(name = "Wishlist(위시리스트)", description = "사용자의 Wishlist관련 API입니다.")

public class WishlistController {

    WishlistService wishlistService;
    MemberService memberService;
    ProductService productService;

    public WishlistController(WishlistService wishlistService, MemberService memberService,
        ProductService productService) {
        this.wishlistService = wishlistService;
        this.memberService = memberService;
        this.productService = productService;
    }

    @GetMapping()
    @Operation(summary = "Wishlist 조회", description = "전달받은 email을 통해 사용자의 Wishlist를 가져옵니다")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "위시리스트 조회 성공", content = @Content(array = @ArraySchema(schema = @Schema(implementation = PagedModel.class)))),
        @ApiResponse(responseCode = "401", description = "유효한 토큰 필요", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})

    public ResponseEntity<Page<Wish>> getWishlist(@LoginUser String email,
        @ParameterObject @PageableDefault(page=0, size=10, sort="id") Pageable pageable ){
        Page<Wish> wishlist = wishlistService.getWishPage(pageable,email);
        return new ResponseEntity<>(wishlist, HttpStatus.OK);
    }

    @Operation(summary = "Wishlist 에 상품 추가", description = "로그인한 사용자의 Wishlist에 상품을 추가합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "위시리스트에 상품 추가 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "401", description = "유효한 토큰 필요", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "409", description = "이미 위시리스트에 존재하는 상품 ", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    @PostMapping
    public ResponseEntity<String> addWishlist(@RequestBody WishRequest wishRequest,
        @LoginUser String email) {
        Member member = memberService.findByEmail(email);
        Product product = productService.getProductById(wishRequest.getProductId());
        Wish wish = new Wish(member, product);
        wishlistService.addWishlist(wish, email);
        return new ResponseEntity<>("위시리스트 상품 추가 완료", HttpStatus.OK);
    }

    @Operation(summary = "Wishlist 의 상품 제거", description = "로그인한 사용자의 Wishlist에서 상품을 제거합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "위시리스트 상품 삭제 성공"),
        @ApiResponse(responseCode = "400", description = "입력 데이터 잘못됨.", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "401", description = "유효한 토큰 필요", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))),
        @ApiResponse(responseCode = "404", description = "이미 위시리스트에 존재하지 않는 상품", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class))))})
    @DeleteMapping()
    public ResponseEntity<String> deleteWishlist(@LoginUser String email,
        @RequestBody WishRequest wishRequest) {
        wishlistService.deleteWishlist(wishRequest.getProductId(), email);
        return new ResponseEntity<>("위시리스트 상품 삭제 완료", HttpStatus.NO_CONTENT);
    }
}
