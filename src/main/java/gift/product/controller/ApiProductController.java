package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.docs.ProductControllerDocs;
import gift.product.dto.ProductDTO;
import gift.product.model.Product;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
public class ApiProductController implements ProductControllerDocs {

    private final ProductService productService;

    @Autowired
    public ApiProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public Page<Product> showProductList(Pageable pageable) {
        System.out.println("[ProductController] showProductList()");
        return productService.getAllProducts(pageable);
    }

    @PostMapping
    public ResponseEntity<Product> registerProduct(
        @Valid @RequestBody ProductDTO productDTO,
        BindingResult bindingResult) {
        System.out.println("[ProductController] registerProduct()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.registerProduct(productDTO));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable Long productId,
        @Valid @RequestBody ProductDTO productDTO,
        BindingResult bindingResult) {
        System.out.println("[ProductController] updateProduct()");
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            if (fieldError != null)
                throw new ValidationException(fieldError.getDefaultMessage());
            throw new ValidationException(UNKNOWN_VALIDATION_ERROR);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(productService.updateProduct(productId, productDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        System.out.println("[ProductController] deleteProduct()");
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/search")
    public Page<Product> searchProduct(
        @RequestParam("keyword") String keyword,
        Pageable pageable) {
        System.out.println("[ProductController] searchProduct()");
        return productService.searchProducts(keyword, pageable);
    }
}
