package gift.controller;

import gift.dto.productDto.ProductDto;
import gift.dto.productDto.ProductResponseDto;
import gift.model.product.Product;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import gift.service.ProductService;


@RequestMapping("/api/products")
@Controller
@Validated
@Tag(name = "Product Management", description = "Product Management API")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "새로운 상품 등록", description = "상품을 등록할 때 사용하는 API")
    public ResponseEntity<ProductResponseDto> addNewProduct(@Valid @RequestBody ProductDto productDto) {
        ProductResponseDto productResponseDto = productService.addNewProduct(productDto);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품을 업데이트", description = "등록된 상품을 업데이트 할 때 사용하는 API")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDto productDto) {
        ProductResponseDto productResponseDto = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "특정 상품 조회", description = "특정 상품을 조회할 때 사용하는 API")
    public ResponseEntity<ProductResponseDto> getProductById(@Valid @PathVariable Long productId) {
        ProductResponseDto productResponseDto = productService.getProductById(productId);
        return ResponseEntity.ok().body(productResponseDto);
    }

    @GetMapping
    @Operation(summary = "등록된 전체 상품 리스트 조회", description = "전체 상품 목록을 조회할 때 사용하는 API")
    public String getProductList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, 20));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "productManage";
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "등록된 상품을 삭제", description = "등록된 상품을 삭제할 때 사용하는 API")
    public String deleteProduct(@PathVariable Long productId){
        productService.DeleteProduct(productId);
        return "productManage";
    }

    @GetMapping("/edit/{productId}")
    @Operation(summary = "상품 수정 페이지로 이동", description = "상품 수정 페이지로 이동할 때 사용하는 API")
    public String moveToEditProduct(@PathVariable Long productId, Model model) {
        Product product = productService.selectProduct(productId);
        model.addAttribute("product", product);
        return "editProduct";
    }
}