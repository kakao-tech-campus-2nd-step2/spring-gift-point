package gift.doamin.product.controller;

import gift.doamin.product.dto.ProductForm;
import gift.doamin.product.dto.ProductParam;
import gift.doamin.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.security.Principal;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductParam addNewProduct(@Valid @RequestBody ProductForm productForm,
        Principal principal) {
        Long userId = Long.valueOf(principal.getName());
        productForm.setUserId(userId);
        return productService.create(productForm);
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다. 한 번에 5개씩 페이지별로 조회할 수 있습니다.")
    @GetMapping
    public Page<ProductParam> getProducts(
        @RequestParam(required = false, defaultValue = "0", name = "page") int pageNum) {
        return productService.getPage(pageNum);
    }

    @Operation(summary = "단일 상품 조회", description = "선택한 상품 1개의 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ProductParam getOneProduct(@PathVariable Long id) {
        return productService.readOne(id);
    }

    @Operation(summary = "상품 정보 수정", description = "선택한 상품의 정보를 수정합니다.")
    @PutMapping("/{id}")
    public ProductParam updateProduct(@PathVariable Long id,
        @Valid @RequestBody ProductForm productForm) {

        return productService.update(id, productForm);
    }

    @Operation(summary = "상품 삭제", description = "선택한 상품을 삭제합니다")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.delete(id);
    }
}
