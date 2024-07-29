package gift.product.controller.product;

import gift.product.dto.product.ClientProductDto;
import gift.product.model.Product;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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

@Tag(name = "product", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getProductAll() {
        List<Product> productAll = productService.getProductAll();
        return ResponseEntity.ok(productAll);
    }

    @GetMapping("/paged")
    public ResponseEntity<Page<Product>> getProductAll(
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "5") int size,
        @RequestParam(name = "sort", defaultValue = "id") String sort,
        @RequestParam(name = "direction", defaultValue = "asc") String direction) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), sort);
        Page<Product> products = productService.getProductAll(pageable);
        return ResponseEntity.ok(products);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable(name = "id") Long id) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping("/insert")
    public ResponseEntity<Product> insertProduct(@Valid @RequestBody ClientProductDto productDto) {
        Product responseProduct = productService.insertProduct(productDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(responseProduct);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable(name = "id") Long id,
        @Valid @RequestBody ClientProductDto productDto) {
        Product product = productService.updateProduct(id, productDto);
        return ResponseEntity.ok(product);
    }

    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 리소스", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.ok().build();
    }
}
