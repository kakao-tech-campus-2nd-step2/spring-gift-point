package gift.wishList.controller.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.UserDetails;
import gift.dto.request.WishReq;
import gift.dto.response.WishProductResponse;
import gift.wishList.service.WishProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "user-wishList", description = "유저 위시리스트 관련 API")
@RestController
public class UserWishListController {

	private final WishProductService wishProductService;

	public UserWishListController(WishProductService wishProductService) {
		this.wishProductService = wishProductService;
	}

	// 위시리스트에 상품 추가
	@Operation(summary = "위시리스트 추가", description = "위시리스트에 상품 추가")
	@PostMapping("/api/wishes")
	public ResponseEntity<Long> addWishList(@RequestAttribute("userDetails") UserDetails userDetails,
		@RequestBody WishReq wishReq) {
		Long wishId = wishProductService.save(userDetails.userId(), wishReq.productId());
		return ResponseEntity.status(201).body(wishId);
	}

	//로그인한 유저의 위시리스트 조회
	@Operation(summary = "위시리스트 조회", description = "로그인한 유저의 위시리스트 조회")
	@GetMapping("/api/wishes")
	public ResponseEntity<Page<WishProductResponse>> getWishList(
		@RequestAttribute("userDetails") UserDetails userDetails,
		@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
		) {
		Page<WishProductResponse> wishList = wishProductService.getByUserId(userDetails.userId(),pageable);
		return ResponseEntity.ok(wishList);
	}

	//위시리스트 삭제
	@Operation(summary = "위시리스트 삭제", description = "위시리스트 삭제")
	@DeleteMapping("/api/wishes/{wishId}")
	public ResponseEntity<Long> deleteWishList(
		@PathVariable Long wishId) {
		wishProductService.deleteWishProduct(wishId);
		return ResponseEntity.status(204).build();
	}
}
