package gift.controller;

import gift.dto.request.ProductRequestDTO;
import gift.dto.response.PagingProductResponseDTO;
import gift.dto.response.ProductResponseDTO;
import gift.service.ProductService;
import io.micrometer.common.lang.Nullable;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("")
    public ResponseEntity<PagingProductResponseDTO> getProducts(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "name,asc") String sort,
            @RequestParam(name = "categoryId", defaultValue = "1") @Nullable Long categoryId){

        String[] sortParams = sort.split(",");
        Sort.Order order = new Sort.Order(Sort.Direction.fromString(sortParams[1]), sortParams[0]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(order));

        PagingProductResponseDTO pagingProductResponseDTO = productService.getProductsByCategoryId(categoryId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(pagingProductResponseDTO);
    }


    @PostMapping("")
    public ResponseEntity<String> createProduct(@RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.addProduct(productRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Product added successfully");
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable("productId") Long productId) {
        ProductResponseDTO productResponseDTO = productService.getProduct(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(productResponseDTO);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(@PathVariable("productId") Long productId,
                                                @RequestBody @Valid ProductRequestDTO productRequestDTO) {
        productService.updateProduct(productId, productRequestDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product updated successfully");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long productId) {
        productService.removeProduct(productId);
        return ResponseEntity.status(HttpStatus.OK)
                .body("Product deleted successfully");
    }



}

