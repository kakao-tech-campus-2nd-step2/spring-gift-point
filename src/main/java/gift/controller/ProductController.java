package gift.controller;

import gift.dto.ProductDTO;
import gift.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 모든 제품 조회.
     *
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 제품 목록
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(@RequestParam int page, @RequestParam int size) {
        Page<ProductDTO> products = productService.getAllProducts(page, size);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    /**
     * 제품 ID로 제품 조회.
     *
     * @param id 제품 ID
     * @return 제품
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO product = productService.getProductById(id);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    /**
     * 새로운 제품 추가.
     *
     * @param productDTO 제품 DTO
     * @param bindingResult 바인딩 결과
     * @return 응답 엔티티
     */
    @PostMapping
    public ResponseEntity<Void> addProduct(@RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.saveProduct(productDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 기존 제품 수정.
     *
     * @param id 제품 ID
     * @param productDTO 제품 DTO
     * @param bindingResult 바인딩 결과
     * @return 응답 엔티티
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody @Valid ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        productService.updateProduct(id, productDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * 제품 삭제.
     *
     * @param id 제품 ID
     * @return 응답 엔티티
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}