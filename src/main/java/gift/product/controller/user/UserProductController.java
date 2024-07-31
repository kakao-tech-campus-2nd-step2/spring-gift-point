package gift.product.controller.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.OrderRequest;
import gift.dto.response.ProductResponse;
import gift.product.domain.Category;
import gift.product.domain.CategoryService;
import gift.product.domain.Product;
import gift.dto.SearchType;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import gift.dto.request.UserDetails;
import gift.user.service.UserService;

@RestController
public class UserProductController {

	private final UserService userService;
	private final ProductService productService;
	private final OptionService optionService;
	private final CategoryService categoryService;

	public UserProductController(UserService userService, ProductService productService, OptionService optionService,
		CategoryService categoryService) {
		this.userService = userService;
		this.productService = productService;
		this.optionService = optionService;
		this.categoryService = categoryService;
	}

	// 상품 단일 조회 메서드
	@GetMapping("/product/{id}")
	@ResponseBody
	public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
		Product product = productService.getProductById(id);
		return ResponseEntity.ok(ProductResponse.from(product));
	}

	// 모든 상품을 페이징해서 조회
	@GetMapping("/product")
	@ResponseBody
	public ResponseEntity<Page<ProductResponse>> getProductsWithPaging(
		@RequestParam(required = false) SearchType searchType,
		@RequestParam(required = false) String searchValue,
		@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
	) {
		return ResponseEntity.ok(productService.getProductsWithPaging(pageable, searchType, searchValue));
	}

	// 상품 주문
	@PostMapping("/product/{productId}/order")
	@ResponseBody
	public ResponseEntity<?> orderProduct(
		@PathVariable Long productId,
		@RequestBody OrderRequest orderRequest,
		@RequestAttribute("userDetails") UserDetails userDetails
	) {
		optionService.reduceStock(orderRequest);
		userService.sendKakaoMessage(orderRequest.message(), userDetails.userId());
		return ResponseEntity.ok().build();
	}

	//카테고리 목록 조회
	@GetMapping("/api/categories")
	public ResponseEntity<List<Category>> getCategories() {
		return ResponseEntity.ok(categoryService.getAllCategories());
	}
}
