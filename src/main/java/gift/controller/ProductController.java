package gift.controller;

import gift.dto.ProductDTO;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 상품 추가,수정,삭제,조회를 위한 api end-point
 * <p>
 * $/api/products
 */
@RequestMapping("/api")
@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> getList() {
        return ResponseEntity.ok(productService.readAll());
    }

    @PostMapping("/products")
    public ResponseEntity<Void> add(@RequestBody @Valid ProductDTO dto) {
        productService.create(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid ProductDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("id를 입력해주세요");
        }
        productService.update(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/products/{page}")
    public ResponseEntity<List<ProductDTO>> getPage(@PathVariable int page) {
        return ResponseEntity.ok(productService.readProduct(page, 10));
    }

}