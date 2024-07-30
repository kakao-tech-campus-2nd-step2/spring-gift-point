package gift.Controller;


import gift.Model.Member;
import gift.Model.Product;

import gift.Service.WishlistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Wishlist", description = "Wishlist 관련 API")
@RestController
public class WishListController {
    private final WishlistService wishlistService;

    public WishListController(WishlistService wishlistService){
        this.wishlistService = wishlistService;
    }

    @Operation(
        summary = "모든 위시리스트 가져오기",
        description = "등록된 모든 상품을 가져오기 /api/wishlist?page=(요구하려는 페이지의 숫자, 0부터 시작)&size=(페이지에 표시할 상품 수)를 이용해 페이지 적용 가능"
    )
    @ApiResponse(
        responseCode = "200",
        description = "위시리스트 가져오기 성공"
    )
    @Parameters({
        @Parameter(name = "request", description = "메소드 실행 전 토큰을 전달 받기 위한 객체"),
        @Parameter(name = "pageable", description = "List에 담긴 Product객체를 개수에 맞춰서 page로 리턴")
    })
    @GetMapping("/api/wishlist")
    public ResponseEntity<Page<Product>> getWish(HttpServletRequest request, Pageable pageable) {
        String email = (String) request.getAttribute("email");
        wishlistService.checkUserByMemberEmail(email);
        Page<Product> page = wishlistService.getAllWishlist(email, pageable);
        return ResponseEntity.ok().body(page);
    }

    @Operation(
        summary = "위시리스트에 저장",
        description = "선택된 상품을 위시리스트에 저장"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 전달 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "위시리스트에 더할 상품 Id"),
        @Parameter(name = "request", description = "메소드 실행 전 토큰을 전달 받기 위한 객체"),
    })
    @PostMapping("/api/wishlist/add/{productId}")
    public ResponseEntity<Product> editWishForm(@PathVariable(value = "productId") Long productId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        wishlistService.checkUserByMemberEmail(email);
        Member member = wishlistService.getMemberByEmail(email);
        Product product = wishlistService.getProductById(productId);
        wishlistService.addWishlist(member.getId(), product.getId());
        return ResponseEntity.ok().body(product);
    }

    @Operation(
        summary = "위시리스트 상품 삭제하기",
        description = "등록된 위시리스트를 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "위시리스트 상품 삭제 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "위시리스트에 삭제 할 상품 Id"),
        @Parameter(name = "request", description = "메소드 실행 전 토큰을 전달 받기 위한 객체"),
    })
    @DeleteMapping("/api/wishlist/{productId}")
    public ResponseEntity<Product> deleteWish(@PathVariable(value = "productId") Long productId, HttpServletRequest request) {
        String email = (String) request.getAttribute("email");
        wishlistService.checkUserByMemberEmail(email);
        Long wishlistId = wishlistService.getWishlistId(email,productId);
        Product deleteProduct = wishlistService.deleteWishlist(email, productId, wishlistId);
        return ResponseEntity.ok().body(deleteProduct);
    }
}
