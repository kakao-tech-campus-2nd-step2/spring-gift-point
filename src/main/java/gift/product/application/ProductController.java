package gift.product.application;

import gift.auth.interceptor.Authorized;
import gift.member.domain.Role;
import gift.product.application.dto.request.ProductCreateRequest;
import gift.product.application.dto.request.ProductRequest;
import gift.product.application.dto.response.ProductPageResponse;
import gift.product.application.dto.response.ProductResponse;
import gift.product.service.ProductService;
import gift.product.service.facade.ProductFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Product", description = "Product관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final ProductFacade productFacade;

    @Autowired
    public ProductController(ProductService productService, ProductFacade productFacade) {
        this.productService = productService;
        this.productFacade = productFacade;
    }

    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "상품 등록 성공"),
    })
    @Authorized(Role.USER)
    @PostMapping
    public ResponseEntity<Void> saveProduct(@RequestBody @Valid ProductCreateRequest productCreateRequest) {
        var createdProductId = productFacade.saveProduct(
                productCreateRequest.getProductCommand(),
                productCreateRequest.getProductOptionCommands()
        );

        return ResponseEntity.created(URI.create("/api/products/" + createdProductId))
                .build();
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @Authorized(Role.USER)
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void modifyProduct(@PathVariable("id") Long id, @RequestBody @Valid ProductRequest modifyProduct) {
        productService.modifyProduct(id, modifyProduct.toProductCommand());
    }

    @Operation(summary = "상품 상세 조회", description = "상품 상세 정보를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 상세 조회 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @Authorized(Role.USER)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductDetails(@PathVariable("id") Long id) {
        var productInfo = productService.getProductDetails(id);

        var response = ProductResponse.from(productInfo);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공"),
    })
    @Authorized(Role.USER)
    @GetMapping()
    public ResponseEntity<ProductPageResponse> getProductList(@PageableDefault Pageable pageable) {
        var productInfo = productService.getProducts(pageable);

        var response = ProductPageResponse.from(productInfo);
        return ResponseEntity.ok()
                .body(response);
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상품 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "상품을 찾을 수 없음")
    })
    @Authorized(Role.USER)
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
    }
}
