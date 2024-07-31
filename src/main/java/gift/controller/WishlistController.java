package gift.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.WishlistRequest;
import gift.dto.WishlistResponse;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "위시리스트 관리", description = "위시리스트 관련 API")
@RestController
@RequestMapping("/wishlist")
public class WishlistController {
	
	private final WishlistService wishlistService;
	
	public WishlistController(WishlistService wishlistService) {
		this.wishlistService = wishlistService;
	}

	@Operation(summary = "위시리스트 조회", description = "사용자의 위시리스트를 페이지네이션으로 조회합니다.")
	@ApiResponse(responseCode = "200", description = "위시리스트 조회 성공")
	@GetMapping
	public ResponseEntity<Page<WishlistResponse>> getWishlist(@RequestHeader("Authorization") String token,
			@PageableDefault(sort = "product.name") Pageable pageable) {
		Page<WishlistResponse> wishlist = wishlistService.getWishlist(token, pageable);
		if (wishlist.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.OK).body(wishlist);
	}

	@Operation(summary = "위시리스트 추가", description = "위시리스트에 상품을 추가합니다.")
	@ApiResponse(responseCode = "201", description = "위시리스트 추가 성공")
	@PostMapping
	public ResponseEntity<Void> addWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.addWishlist(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.CREATED).build();
	}

	@Operation(summary = "위시리스트 삭제", description = "위시리스트에서 상품을 제거합니다.")
	@ApiResponse(responseCode = "200", description = "위시리스트 삭제 성공")
	@DeleteMapping
	public ResponseEntity<Void> removeWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.removeWishlist(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@Operation(summary = "위시리스트 업데이트", description = "위시리스트의 상품 수량을 업데이트합니다.")
	@ApiResponse(responseCode = "200", description = "위시리스트 업데이트 성공")
	@PutMapping
	public ResponseEntity<Void> updateWishlist(@RequestHeader("Authorization") String token,
			@RequestBody WishlistRequest request, BindingResult bindingResult) {
		wishlistService.updateWishlistQuantity(token, request, bindingResult);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}