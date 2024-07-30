package gift.controller;

import gift.auth.LoginUser;
import gift.domain.User;
import gift.dto.common.apiResponse.ApiResponseBody.SuccessBody;
import gift.dto.common.apiResponse.ApiResponseGenerator;
import gift.dto.requestdto.ProductCreateRequestDTO;
import gift.dto.requestdto.ProductRequestDTO;
import gift.dto.responsedto.ProductResponseDTO;
import gift.service.AuthService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api")
@Tag(name = "상품 api", description = "상품 api입니다")
public class ProductController {
    private final ProductService productService;
    private final AuthService authService;

    public ProductController(ProductService productService, AuthService authService) {
        this.productService = productService;
        this.authService = authService;
    }

    @GetMapping("/products")
    @Operation(summary = "상품 전체 조회 api", description = "상품 전체 조회 api입니다")
    @ApiResponse(responseCode = "200", description = "상품 전체 조회 성공")
    public ResponseEntity<SuccessBody<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> productResponseDTOList = productService.getAllProducts();
        return ApiResponseGenerator.success(HttpStatus.OK, "모든 상품을 조회했습니다.",
            productResponseDTOList);
    }

    @GetMapping("/products/page")
    @Operation(summary = "상품 조회 페이지 api", description = "상품 조회 페이지 api입니다")
    @ApiResponse(responseCode = "200", description = "상품 페이지 전체 조회 성공")
    public ResponseEntity<SuccessBody<List<ProductResponseDTO>>> getAllProductPages(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "8") int size,
        @RequestParam(value = "criteria", defaultValue = "id") String criteria) {
        List<ProductResponseDTO> productResponseDTOList = productService.getAllProducts(page, size, criteria);
        return ApiResponseGenerator.success(HttpStatus.OK, "모든 상품을 조회했습니다.",
            productResponseDTOList);
    }

    @GetMapping("/product/{id}")
    @Operation(summary = "상품 단일 조회 api", description = "상품 단일 조회 api입니다")
    @ApiResponse(responseCode = "200", description = "상품 단일 조회 성공")
    public ResponseEntity<SuccessBody<ProductResponseDTO>> getOneProduct(
        @PathVariable("id") Long productId) {
        ProductResponseDTO productResponseDTO = productService.getOneProduct(productId);
        return ApiResponseGenerator.success(HttpStatus.OK, "id : " + productId + " 상품을 조회했습니다.",
            productResponseDTO);
    }

    @PostMapping("/product")
    @Operation(summary = "상품 추가 api", description = "상품 추가 api입니다")
    @ApiResponse(responseCode = "201", description = "상품 추가 성공")
    public ResponseEntity<SuccessBody<Long>> addProduct(
        @Valid @RequestBody ProductCreateRequestDTO productCreateRequestDTO,
        @LoginUser User user) {
        authService.authorizeAdminUser(user);
        Long productId = productService.addProduct(productCreateRequestDTO);

        return ApiResponseGenerator.success(HttpStatus.CREATED, "상품이 등록되었습니다.", productId);
    }

    @PutMapping("/product/{id}")
    @Operation(summary = "상품 수정 api", description = "상품 수정 api입니다")
    @ApiResponse(responseCode = "200", description = "상품 수정 성공")
    public ResponseEntity<SuccessBody<Long>> updateProduct(
        @PathVariable("id") Long productId,
        @Valid @RequestBody ProductRequestDTO productRequestDTO,
        @LoginUser User user) {
        authService.authorizeAdminUser(user);
        Long updatedProductId = productService.updateProduct(productId, productRequestDTO);
        return ApiResponseGenerator.success(HttpStatus.OK, "상품이 수정되었습니다.", updatedProductId);
    }

    @DeleteMapping("/product/{id}")
    @Operation(summary = "상품 삭제 api", description = "상품 삭제 api입니다")
    @ApiResponse(responseCode = "200", description = "상품 삭제 성공")
    public ResponseEntity<SuccessBody<Long>> deleteProduct(
        @PathVariable("id") Long productId,
        @LoginUser User user) {
        authService.authorizeAdminUser(user);
        Long deletedProductId = productService.deleteProduct(productId);
        return ApiResponseGenerator.success(HttpStatus.OK, "상품이 삭제되었습니다.", deletedProductId);
    }
}
