package gift.controller;

import gift.model.OptionDTO;
import gift.model.ProductDTO;
import gift.model.ProductPageDTO;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
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

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<?> getAllProduct(@RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) {
        ProductPageDTO productsPage = productService.getAllProduct(page, size);
        return ResponseEntity.ok().body(productsPage.products());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<?> getAllOptions(@PathVariable long productId, Pageable pageable) {
        List<OptionDTO> optionDTOList = productService.getOptionsByProductId(productId, pageable);
        return ResponseEntity.ok(optionDTOList);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<?> createOption(@PathVariable long productId,
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO createdOption = optionService.createOption(productId, optionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@Valid @RequestBody ProductDTO updatedDTO) {
        ProductDTO updatedProduct = productService.updateProduct(updatedDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<OptionDTO> updateOption(@PathVariable long productId,
        @PathVariable long optionId,
        @Valid @RequestBody OptionDTO optionDTO) {
        OptionDTO updateOption = optionService.updateOption(productId, optionId, optionDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updateOption);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<?> deleteOption(@PathVariable long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
