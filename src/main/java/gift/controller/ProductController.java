package gift.controller;

import gift.dto.ProductRequest;
import gift.dto.ProductResponse;
import gift.service.ProductService;
import gift.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "20") int size,
                                                              @RequestParam(required = false) Long category) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ProductResponse> productPage;

        if (category != null) {
            productPage = productService.findAllByCategoryId(category, pageRequest);
        } else {
            productPage = productService.findAll(pageRequest);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("total_page", productPage.getTotalPages());
        data.put("content", productPage.getContent());

        Map<String, Object> response = new HashMap<>();
        response.put("data", data);

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> convertToDto(ProductResponse product) {
        Map<String, Object> productDto = new HashMap<>();
        productDto.put("id", product.getId());
        productDto.put("name", product.getName());
        productDto.put("price", product.getPrice());
        productDto.put("image_url", product.getImageUrl());
        productDto.put("category_id", product.getCategoryId());
        return productDto;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable long id) {
        ProductResponse product = productService.findById(id);
        Map<String, Object> productDto = convertToDto(product);
        return ResponseEntity.ok(productDto);
    }

    @GetMapping("/new")
    public ResponseEntity<ProductRequest> addProductForm() {
        return ResponseEntity.ok(new ProductRequest("", 0, "", 1L));
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductRequest productRequest) {
        try {
            productService.save(productRequest);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        try {
            productService.update(id, productRequest);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete-batch")
    public ResponseEntity<?> deleteBatch(@RequestBody Map<String, List<Long>> request) {
        productService.deleteBatch(request.get("ids"));
        return ResponseEntity.ok("Success");
    }
}
