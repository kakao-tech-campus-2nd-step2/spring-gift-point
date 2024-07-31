package gift.product.controller.admin;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.response.ProductResponse;
import gift.product.service.OptionService;
import gift.product.service.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminProuctController {
	private final ProductService productService;
	private final OptionService optionService;

	@GetMapping("/admin/product")
	public ResponseEntity<List<ProductResponse>> viewProducts(Model model) {
		List<ProductResponse> products = productService.getAllProducts();
		return ResponseEntity.ok(products);
	}
}
