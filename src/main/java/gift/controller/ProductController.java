package gift.controller;

import gift.dto.ProductDto;
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
    public ResponseEntity<Void> addNewProduct(@Valid @RequestBody ProductDto productDto) {
        productService.addNewProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품을 업데이트", description = "등록된 상품을 업데이트 할 때 사용하는 API")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto) {
        productService.updateProduct(id, productDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "상품 수정 페이지로 이동", description = "상품 수정 페이지로 이동할 때 사용하는 API")
    public String moveToEditProduct(@PathVariable Long id, Model model) {
        Product product = productService.selectProduct(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @GetMapping
    @Operation(summary = "등록된 전체 상품 리스트 조회", description = "전체 상품 목록을 조회할 때 사용하는 API")
    public String getProductList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, 20));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "productManage";
    }

    @GetMapping("/add")
    @Operation(summary = "상품 추가 페이지로 이동", description = "상품 추가 페이지로 이동할 때 사용하는 API")
    public String movtoAddProduct() {
        return "addProduct";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "등록된 상품을 삭제", description = "등록된 상품을 삭제할 때 사용하는 API")
    public String DeleteProduct(@PathVariable Long id){
        productService.DeleteProduct(id);
        return "productManage";
    }
}