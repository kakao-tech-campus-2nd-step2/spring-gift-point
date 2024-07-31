package gift.wishList.controller.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.response.WishProductResponse;
import gift.wishList.service.WishProductService;

@RestController("api/user/v1/wishlist")
public class UserWishListController {

	private final WishProductService wishProductService;

	public UserWishListController(WishProductService wishProductService) {
		this.wishProductService = wishProductService;
	}

	// 위시리스트에 상품 추가
	@PostMapping("")
	public ResponseEntity<Long> addWishList(@RequestAttribute("UserId") Long userId, @RequestParam Long productId) {
		return ResponseEntity.ok(wishProductService.save(productId, userId).getId());
	}

	//로그인한 유저의 위시리스트 조회
	@GetMapping("")
	public ResponseEntity<List<WishProductResponse>> getWishList(@RequestAttribute("UserId") Long userId) {
		List<WishProductResponse> wishList = wishProductService.getByUserId(userId);
		return ResponseEntity.ok(wishList);
	}

	//로그인한 유저의 위시리스트를 페이징 해서 조회
	@GetMapping("/page")
	public ResponseEntity<Page<WishProductResponse>> getWishListWithPage(
		@RequestAttribute("UserId") Long userId,
		@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(wishProductService.getByUserId(userId,pageable));
	}
}
