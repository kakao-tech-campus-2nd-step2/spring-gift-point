/**
 * ProductController Class
 * 사용 : Product에 대한 CRUD 처리를 수행하고, 해당 결과를 보여줄 View를 가져온다
 * 기능 : 상품 목록 불러오기, 상품 추가, 삭제, 수정
 */
package gift.controller;

import gift.DTO.Product.ProductRequest;
import gift.DTO.Product.ProductResponse;
import gift.DTO.User.UserResponse;
import gift.security.AuthenticateMember;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    /*
     * 상품 전체 조회
     */
    @GetMapping("/api/products")
    public ResponseEntity<Page<ProductResponse>> readAllProduct(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort") List<String> sort,
            @RequestParam(value = "categoryId", defaultValue = "1") Long categoryId
            ) {
        Page<ProductResponse> products = productService.readAllProduct(page, size, sort.getFirst(), sort.getLast(), categoryId);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    /*
     * 상품 조회
     */
    @GetMapping("/api/products/{productId}")
    public ResponseEntity<ProductResponse> readProduct(@PathVariable("productId") Long productId) {
        ProductResponse productResponse = productService.readOneProduct(productId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
    /*
     * 상품 추가
     */
    @PostMapping("/api/products")
    public ResponseEntity<Void> createProduct(
            @Valid @RequestBody ProductRequest product,
            @AuthenticateMember UserResponse user
    ){
        productService.save(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /*
     * 상품 수정
     */
    @PutMapping("/api/products/{productId}")
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") Long id,
            @Valid @RequestBody ProductRequest product,
            @AuthenticateMember UserResponse user
    ){
        productService.updateProduct(product, id);
        return new ResponseEntity<>((HttpStatus.OK));
    }
    /*
     * 상품 삭제
     */
    @DeleteMapping("/api/products/{productId}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable("productId") Long id,
            @AuthenticateMember UserResponse user
    ) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}