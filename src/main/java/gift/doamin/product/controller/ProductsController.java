package gift.doamin.product.controller;

import gift.doamin.product.dto.ProductCreateRequest;
import gift.doamin.product.dto.ProductResponse;
import gift.doamin.product.dto.ProductUpdateRequest;
import gift.doamin.product.service.ProductService;
import gift.doamin.user.dto.UserDto;
import gift.global.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequestMapping("/api/products")
public class ProductsController {

    private final ProductService productService;

    public ProductsController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 추가", description = "새로운 상품을 등록합니다.")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse addNewProduct(
        @Valid @RequestBody ProductCreateRequest productCreateRequest, @LoginUser UserDto user) {
        return productService.create(user.getId(), productCreateRequest);
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다. 한 번에 5개씩 페이지별로 조회할 수 있습니다.")
    @Parameters(value = {
        @Parameter(name = "categoryId", description = "조회할 상품의 카테고리 id. -1 입력시 모든 카테고리 조회", schema = @Schema(defaultValue = "-1", type = "integer($int64)"))})
    @GetMapping
    public Page<ProductResponse> getProducts(
        @ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "-1") Long categoryId) {
        return productService.getPage(pageable, categoryId);
    }

    @Operation(summary = "단일 상품 조회", description = "선택한 상품 1개의 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ProductResponse getOneProduct(@PathVariable Long productId) {
        return productService.readOne(productId);
    }

    @Operation(summary = "상품 정보 수정", description = "선택한 상품의 정보를 수정합니다.")
    @PutMapping("/{productId}")
    public ProductResponse updateProduct(@PathVariable Long productId,
        @Valid @RequestBody ProductUpdateRequest productUpdateRequest) {

        return productService.update(productId, productUpdateRequest);
    }

    @Operation(summary = "상품 삭제", description = "선택한 상품을 삭제합니다")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {

        productService.delete(id);
    }
}
