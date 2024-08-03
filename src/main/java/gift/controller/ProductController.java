package gift.controller;

import gift.argumentresolver.LoginMember;
import gift.dto.member.MemberDto;
import gift.dto.product.AddProductRequest;
import gift.dto.product.AddProductResponse;
import gift.dto.product.GetProductResponse;
import gift.dto.product.ProductDto;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<GetProductResponse>> getProducts(@LoginMember MemberDto memberDto, Pageable pageable) {
        return ResponseEntity.ok().body(productService.getProducts(memberDto, pageable));
    }

    @Operation(summary = "한 상품 조회", description = "해당 id의 상품을 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<GetProductResponse> getProduct(@LoginMember MemberDto memberDto, @PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.getProduct(memberDto, id));
    }

    @Operation(summary = "상품 추가", description = "상품을 추가합니다.")
    @PostMapping
    public ResponseEntity<AddProductResponse> addProduct(@Valid @RequestBody AddProductRequest addProductRequest) {
        return ResponseEntity.ok().body(productService.addProduct(addProductRequest.productDto(), addProductRequest.optionDtos()));
    }

    @Operation(summary = "상품 수정", description = "해당 id의 상품을 수정합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable("id") long id, @Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok().body(productService.updateProduct(id, productDto));
    }

    @Operation(summary = "상품 삭제", description = "해당 id의 상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(productService.deleteProduct(id));
    }
}
