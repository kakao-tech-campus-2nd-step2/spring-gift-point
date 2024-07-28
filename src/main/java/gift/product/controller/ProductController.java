package gift.product.controller;

import gift.common.util.CommonResponse;
import gift.product.domain.Product;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
@Validated
@Tag(name = "Product", description = "상품 API")
// crud를 진행하고 다시 api/products로 보내는 역할
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 1. product create
    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    @PostMapping
    public ResponseEntity<?> createProduct(ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productService.createProduct(product, productRequest.getCategoryId());

        //return "redirect:/show/products";
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 생성 성공", true));
    }

    // 2. product update
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") Long id, ProductRequest productRequest) {
        Product product = new Product(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl());
        productService.updateProduct(id, product, productRequest.getCategoryId());

        //return "redirect:/show/products";
        return ResponseEntity.ok(new CommonResponse<>(null, "상품 수정 성공", true));
    }

    // 3. product delete
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        //return "redirect:/show/products";

        return ResponseEntity.ok(new CommonResponse<>(null, "상품 삭제 성공", true));
    }

    // 4. product read
    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long id) {
        ProductResponse productResponse = productService.getProductDTOById(id);

        return ResponseEntity.ok(new CommonResponse<>(productResponse, "상품 조회 성공", true));
    }

    // product pagination
    @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<?> getProductsByPage(@RequestParam int page,
                                               @RequestParam int size,
                                               @RequestParam(defaultValue = "price,desc") String sort,
                                               @RequestParam(required = false) Long categoryId) {
        // sort 파라미터를 ',' 기준으로 분리
        String[] sortParams = sort.split(",");
        String sortBy = sortParams[0];
        String direction = sortParams.length > 1 ? sortParams[1] : "desc";

        // 페이지네이션 처리
        Page<ProductResponse> productPage = productService.getProductsByPage(page, size, sortBy, direction, categoryId);

        return ResponseEntity.ok(new CommonResponse<>(productPage, "제품 페이지를 받아오는데 성공하였습니다.", true));
    }
}
