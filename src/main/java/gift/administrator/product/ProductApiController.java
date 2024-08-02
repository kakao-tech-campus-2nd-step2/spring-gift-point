package gift.administrator.product;

import gift.response.ApiResponse;
import gift.response.ApiResponse.HttpResult;
import gift.util.PageUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Arrays;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
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
@Tag(name = "product API", description = "product related API")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "get all products", description = "모든 상품을 조회합니다.")
    public ResponseEntity<ApiResponse<Page<ProductDTO>>> getAllProducts(
        @RequestParam(value = "page", required = false, defaultValue = "0") int page,
        @RequestParam(value = "size", required = false, defaultValue = "10") int size,
        @RequestParam(value = "sort", required = false, defaultValue = "id,asc") String sort,
        @RequestParam(value = "categoryId", required = false) Long categoryId) {
        size = PageUtil.validateSize(size);
        String[] sortParams = PageUtil.validateSort(sort,
            Arrays.asList("id", "name", "createdDate"));
        String sortBy = sortParams[0];
        Direction direction = PageUtil.validateDirection(sortParams[1]);
        Page<ProductDTO> productPage = productService.getAllProducts(page, size, sortBy,
            direction, categoryId);
        ApiResponse<Page<ProductDTO>> apiResponse = new ApiResponse<>(HttpResult.OK, "상품 전체 조회 성공",
            HttpStatus.OK, productPage);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "get one product", description = "상품 아이디로 하나의 상품을 조회합니다.")
    public ResponseEntity<ApiResponse<ProductDTO>> getProduct(
        @PathVariable("productId") Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "상품 조회 성공",
            HttpStatus.OK, productDTO);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    @Operation(summary = "add one product", description =
        "하나의 상품을 추가합니다. required info(name, price,"
            + " imageUrl, categoryId, option)")
    public ResponseEntity<ApiResponse<ProductDTO>> addProduct(
        @Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAddingProducts(productDTO);
        ProductDTO result = productService.addProduct(productDTO);
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "상품 추가 성공",
            HttpStatus.CREATED, result);
        return ResponseEntity.status(HttpStatus.CREATED).body(apiResponse);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "update one product", description = "상품 아이디로 하나의 상품을 수정합니다. "
        + "required info(name, price, imageUrl, categoryId, option)")
    public ResponseEntity<ApiResponse<ProductDTO>> updateProduct(
        @PathVariable("productId") Long productId,
        @Valid @RequestBody ProductDTO productDTO) {
        productService.existsByNameAndId(productDTO.getName(), productId);

        ProductDTO result = productService.updateProduct(productDTO, productId);
        ApiResponse<ProductDTO> apiResponse = new ApiResponse<>(HttpResult.OK, "상품 수정 성공",
            HttpStatus.OK, result);
        return ResponseEntity.ok().body(apiResponse);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "delete one product", description = "상품 아이디로 하나의 상품을 삭제합니다.")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
        @PathVariable("productId") Long productId) {
        productService.getProductById(productId);
        productService.deleteProduct(productId);
        ApiResponse<Void> apiResponse = new ApiResponse<>(HttpResult.OK, "상품 삭제 성공",
            HttpStatus.OK, null);
        return ResponseEntity.ok(apiResponse);
    }
}
