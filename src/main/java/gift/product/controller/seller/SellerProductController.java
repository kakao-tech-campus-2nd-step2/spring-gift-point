package gift.product.controller.seller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.OptionRequest;
import gift.dto.request.ProductCreateRequest;
import gift.product.service.OptionService;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/seller/v1/product")
public class SellerProductController {
	private final ProductService productService;
	private final OptionService optionService;

	// 상품 등록
	@PostMapping("")
	@ResponseBody
	public ResponseEntity<Long> registerProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
		Long id = productService.saveProduct(productCreateRequest);
		optionService.addOptions(id, productCreateRequest.options());
		return ResponseEntity.ok(id);
	}

	// 상품 수정 메서드
	@PutMapping("/{id}")
	@ResponseBody
	public ResponseEntity<Long> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductCreateRequest productCreateRequest) {
		return ResponseEntity.ok(productService.updateProduct(id, productCreateRequest));
	}

	// 상품 삭제 메서드
	@DeleteMapping("/{id}")
	@ResponseBody
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		productService.deleteProduct(id);
		return ResponseEntity.ok().build();
	}

	// 상품 옵션 추가 메서드
	@PostMapping("/{id}/option")
	@ResponseBody
	public ResponseEntity<Long> addOption(@PathVariable Long id, @RequestBody @Valid List<OptionRequest> optionRequests) {
		Long optionId = optionService.addOptions(id, optionRequests);
		return ResponseEntity.ok(optionId);
	}

	// 상품 옵션 삭제 메서드
	@PostMapping("/{id}/option/{optionId}")
	@ResponseBody
	public ResponseEntity<?> deleteOption(@PathVariable Long id, @PathVariable Long optionId) {
		optionService.deleteOption(optionId);
		return ResponseEntity.ok().build();
	}

}
