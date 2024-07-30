package gift.controller.product;

import gift.dto.product.ProductRequest;
import gift.dto.product.ProductResponse;
import gift.dto.paging.PagingRequest;
import gift.dto.paging.PagingResponse;
import gift.service.product.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController implements ProductSpecification {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<String> addGift(@Valid @RequestBody ProductRequest.Create giftRequest) {
        productService.addGift(giftRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("상품이 생성되었습니다.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse.Info> getGift(@PathVariable Long id) {
        ProductResponse.Info gift = productService.getGift(id);
        return ResponseEntity.ok(gift);
    }

    @GetMapping
    public ResponseEntity<PagingResponse<ProductResponse.Info>> getAllGift(@ModelAttribute PagingRequest pagingRequest) {
        PagingResponse<ProductResponse.Info> response = productService.getAllGifts(pagingRequest.getPage(), pagingRequest.getSize());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGift(@PathVariable Long id, @RequestBody ProductRequest.Update giftRequest) {
        productService.updateGift(giftRequest, id);
        return ResponseEntity.ok("상품 수정이 완료되었습니다.");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGift(@PathVariable Long id) {
        productService.deleteGift(id);
        return ResponseEntity.noContent().build();
    }
}