package gift.web.controller;

import gift.service.product.ProductService;
import gift.web.dto.MemberDto;
import gift.web.dto.product.ProductPutRequestDto;
import gift.web.dto.product.ProductRequestDto;
import gift.web.dto.product.ProductResponseDto;
import gift.web.jwt.AuthUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController //컨트롤러를 JSON을 반환하는 컨트롤러로 만들어줌
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(@PageableDefault(sort = "name") Pageable pageable, @RequestParam(required = false) Long categoryId) {
        return new ResponseEntity<>(productService.getProducts(categoryId, pageable), HttpStatus.OK);
    }

    // products/{상품번호}의 GetMapping
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto productResponseDto= productService.getProductById(id);
        return new ResponseEntity<>(productResponseDto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@AuthUser MemberDto memberDto, @RequestBody @Valid ProductRequestDto productRequestDto) {
        return new ResponseEntity<>(productService.createProduct(productRequestDto), HttpStatus.CREATED);
    }

    // PUT 구현, 멱등성 보장이 중요한 것이지, 굳이 없는 경우 생성할 필요 없음 (상황에 맞게 사용)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@AuthUser MemberDto memberDto, @PathVariable Long id, @RequestBody @Valid ProductPutRequestDto productputRequestDto) {
        return ResponseEntity.ok(productService.updateProduct(id, productputRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@AuthUser MemberDto memberDto, @PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
