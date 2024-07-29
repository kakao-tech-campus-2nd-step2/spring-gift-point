package gift.product;

import gift.Exception.ErrorResponse;
import gift.option.Option;
import gift.option.OptionRequest;
import gift.option.OptionResponse;
import gift.option.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Tag(name = "product", description = "상품 관련 API")
@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductService productService;
    private final OptionService optionService;

    public ProductController(ProductRepository productRepository, ProductService productService,
        OptionService optionService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping()
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("newProduct", new Product()); // 새 상품 객체
        model.addAttribute("product", new Product()); // 편집을 위한 빈 객체*/
        return "admin"; // Thymeleaf 템플릿 이름
    }

    @Operation(summary = "상품 생성", description = "생성한 상품 반환함")
    @PostMapping("/post")
    public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody ProductRequest newProduct) {
        Product product = productService.createProduct(newProduct.toEntity());
        Option option = optionService.addOption(newProduct.getOptionRequest(), product);
        productService.addOption(product.getId(), option);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(product));
    }

    @Operation(summary = "상품 수정", description = "수정한 상품 반환함")
    @PutMapping("/update")
    public ResponseEntity<ProductResponse> updateProduct(
        @Valid @RequestBody ProductRequest changeProduct) {
        return ResponseEntity.ok(
            new ProductResponse(productService.updateProduct(changeProduct.toEntity())));
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("delete");
    }

    @Operation(summary = "옵션 조회", description = "상품별 옵션 목록 조회")
    @GetMapping("{id}/options")
    public ResponseEntity<List<Option>> getOptions(@PathVariable("id") Long productId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(optionService.findAllByProductId(productId));
    }

    @Operation(summary = "옵션 생성", description = "상품별 옵션 생성")
    @PostMapping("{id}/options")
    public ResponseEntity<List<OptionResponse>> addOption(
        @RequestBody @Valid OptionRequest optionRequest,
        @PathVariable Long id) {
        Product product = productService.findById(id);
        Option option = optionService.addOption(optionRequest, product);

        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addOption(id, option));
    }

    @Operation(summary = "옵션 수정", description = "상품별 옵션 수정")
    @PutMapping("{id}/options")
    public ResponseEntity<String> updateOption(@RequestBody @Valid OptionRequest optionRequest,
        @PathVariable Long id) {
        optionService.updateOption(optionRequest, id);

        return ResponseEntity.status(HttpStatus.CREATED).body("update");
    }

    @Operation(summary = "옵션 삭제", description = "상품에 옵션을 삭제함")
    @DeleteMapping("{id}/options")
    public ResponseEntity<List<Option>> deleteOption(@PathVariable("id") Long productId,
        @RequestParam Long optionId) {
        Option option = optionService.getOption(optionId);
        productService.deleteOption(productId, option);

        return ResponseEntity.status(HttpStatus.OK)
            .body(productService.deleteOption(productId, option));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(new ErrorResponse(ex.getMessage()));
    }

}