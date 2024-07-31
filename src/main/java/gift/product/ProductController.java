package gift.product;

import gift.Exception.ErrorResponse;
import gift.option.Option;
import gift.option.OptionContents;
import gift.option.OptionRequest;
import gift.option.OptionResponse;
import gift.option.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/products")
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

    @Operation(summary = "상품 페이지", description = "테스트용 페이지 였음.")
    @GetMapping("productpage")
    public String listProducts(Model model) {
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("newProduct", new Product()); // 새 상품 객체
        model.addAttribute("product", new Product()); // 편집을 위한 빈 객체*/
        return "admin"; // Thymeleaf 템플릿 이름
    }

    @Operation(summary = "상품 생성", description = "생성한 상품 반환함")
    @PostMapping()
    public ResponseEntity<ProductResponse> createProduct(
        @Valid @RequestBody ProductRequest newProduct) {
        Product product = productService.createProduct(newProduct.toEntity());
        Option option = optionService.addOption(newProduct.getOptionRequest(), product);
        productService.addOption(product.getId(), option);

        return ResponseEntity.status(201).body(new ProductResponse(product));
    }

    @Operation(summary = "상품 조회", description = "특정 상품 정보 조회", parameters = {
        @Parameter(name = "productId", description = "상품 ID")
    })
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long productId) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new ProductResponse(productService.findById(productId)));
    }

    @Operation(summary = "상품 수정", description = "수정한 상품 반환함")
    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
        @Valid @RequestBody ProductRequest changeProduct, @PathVariable Long productId) {
        return ResponseEntity.ok(
            new ProductResponse(productService.updateProduct(changeProduct.toEntity(), productId)));
    }

    @Operation(summary = "상품 삭제")
    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.status(204).body("delete");
    }

    // OPTION

    @Operation(summary = "상품 목록 조회", description = "모든 상품 목록 조회")
    @GetMapping()
    public ResponseEntity<ProductPageResponse> getProductPage(
        @RequestParam(value = "page") int page,
        @RequestParam(value = "size") int size) {
        return ResponseEntity.ok(productService.getProductPage(page, size));
    }


    @Operation(summary = "옵션 목록 조회", description = "상품별 옵션 목록 조회")
    @GetMapping("{productId}/options")
    public ResponseEntity<OptionContents> getOptions(@PathVariable("productId") Long productId) {
        return ResponseEntity.ok(OptionContents.from(optionService.findAllByProductId(productId)));
    }

    @Operation(summary = "옵션 생성", description = "상품별 옵션 생성")
    @PostMapping("{productId}/options")
    public ResponseEntity<List<OptionResponse>> addOption(
        @RequestBody @Valid OptionRequest optionRequest,
        @PathVariable Long productId) {
        Product product = productService.findById(productId);
        Option option = optionService.addOption(optionRequest, product);

        return ResponseEntity.status(201)
            .body(productService.addOption(productId, option));
    }

    @Operation(summary = "옵션 수정", description = "상품별 옵션 수정")
    @PutMapping("{productId}/options/{optionId}")
    public ResponseEntity<String> updateOption(@RequestBody @Valid OptionRequest optionRequest,
        @PathVariable Long productId, @PathVariable Long optionId) {
        optionService.updateOption(optionRequest, productId, optionId);

        return ResponseEntity.ok("update");
    }

    @Operation(summary = "옵션 삭제", description = "상품에 옵션을 삭제함")
    @DeleteMapping("{productId}/options/{optionId}")
    public ResponseEntity<List<Option>> deleteOption(@PathVariable Long productId,
        @PathVariable Long optionId) {
        Option option = optionService.getOption(optionId);
        productService.deleteOption(productId, option);

        return ResponseEntity.status(204)
            .body(productService.deleteOption(productId, option));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
        IllegalArgumentException ex) {
        return ResponseEntity.status(400).body(new ErrorResponse(ex.getMessage()));
    }

}