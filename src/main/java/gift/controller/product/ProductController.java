package gift.controller.product;

import gift.application.product.ProductFacade;
import gift.application.product.dto.ProductModel.Info;
import gift.controller.product.dto.ProductRequest;
import gift.controller.product.dto.ProductResponse;
import gift.global.auth.Authorization;
import gift.global.dto.PageResponse;
import gift.model.member.Role;
import gift.model.product.SearchType;
import gift.application.product.service.OptionService;
import gift.application.product.service.ProductService;
import gift.application.product.dto.OptionModel;
import gift.application.product.dto.ProductModel;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.util.Pair;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    private final ProductService productService;
    private final OptionService optionService;
    private final ProductFacade productFacade;

    public ProductController(ProductService productService, OptionService optionService,
        ProductFacade productFacade) {
        this.productService = productService;
        this.optionService = optionService;
        this.productFacade = productFacade;
    }

    @Operation(summary = "상품 조회", description = "상품 조회 api")
    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductResponse.ProductSummary> getProduct(
        @PathVariable("id") Long id
    ) {
        Info productModel = productService.getProduct(id);
        ProductResponse.ProductSummary response = ProductResponse.ProductSummary.from(productModel);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 등록", description = "상품 등록 api")
    @Authorization(role = Role.ADMIN)
    @PostMapping("/api/products")
    public ResponseEntity<ProductResponse.Info> createProduct(
        @RequestBody @Valid ProductRequest.Register request
    ) {
        Pair<Info, List<OptionModel.Info>> models = productFacade.createProduct(
            request.toProductCommand(),
            request.toOptionCommand());
        ProductResponse.Info response = ProductResponse.Info.from(models.getFirst(),
            models.getSecond());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 수정", description = "상품 수정 api")
    @Authorization(role = Role.ADMIN)
    @PutMapping("/api/products/{id}")
    public ResponseEntity<ProductResponse.Info> updateProduct(
        @PathVariable("id") Long id,
        @RequestBody @Valid ProductRequest.Update request
    ) {
        Pair<Info, List<OptionModel.Info>> models = productFacade.updateProduct(id,
            request.toProductCommand());
        ProductResponse.Info response = ProductResponse.Info.from(models.getFirst(),
            models.getSecond());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "상품 삭제", description = "상품 삭제 api")
    @Authorization(role = Role.ADMIN)
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<String> deleteProduct(
        @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully.");
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록 조회 api")
    @GetMapping("/api/products")
    public ResponseEntity<PageResponse<ProductResponse.ProductSummary>> getProductsPaging(
        @RequestParam(name = "SearchType", required = false, defaultValue = "ALL") SearchType searchType,
        @RequestParam(name = "SearchValue", required = false, defaultValue = "") String searchValue,
        @RequestParam(name = "categoryId", required = false) Long categoryId,
        @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProductModel.Info> page = productService.getProductsPaging(searchType,
            searchValue,
            categoryId,
            pageable);
        var response = PageResponse.from(page, ProductResponse.ProductSummary::from);
        return ResponseEntity.ok(response);
    }

}
