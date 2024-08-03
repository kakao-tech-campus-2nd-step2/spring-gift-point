package gift.controller;

import gift.annotation.LoginUser;
import gift.dto.product.*;
import gift.exceptions.CustomException;
import gift.model.User;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Controller
@Validated
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회",
            description = "카테고리 ID에 따라 상품 목록을 조회합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "상품 목록 조회 성공"),
                @ApiResponse(responseCode = "400", description = "잘못된 값 입력"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 categoryId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<ProductsPageResponseDTO> getProductsPage(@RequestParam Long categoryId, @PageableDefault(sort = "name") Pageable pageable) {
        ProductsPageResponseDTO content = productService.getAllProductsByCategoryId(categoryId, pageable);

        return new ResponseEntity<>(content, HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회",
            description = "상품 ID에 따라 상품 정보를 조회합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<ProductResponseDTO> getProduct(@PathVariable(name = "productId") Long productId) {
        ProductResponseDTO productResponseDTO = productService.getProduct(productId);

        return new ResponseEntity<>(productResponseDTO, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "상품 추가",
            description = "새로운 상품을 추가합니다.",
            responses = {
                @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<ProductAddResponseDTO> addProduct(@LoginUser User user, @Valid  @RequestBody ProductAddRequestDTO productAddRequestDTO) {
        validateProductName(productAddRequestDTO.name());
        ProductAddResponseDTO productAddResponseDTO = productService.createProduct(productAddRequestDTO);

        return new ResponseEntity<>(productAddResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정",
            description = "기존 상품을 수정합니다.",
            responses = {
                @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<ProductModifyResponseDTO> modifyProduct(@LoginUser User user, @PathVariable(name = "productId") Long id, @Valid @RequestBody ProductModifyRequestDTO productModifyRequestDTO) {
        validateProductName(productModifyRequestDTO.name());
        ProductModifyResponseDTO productModifyResponseDTO = productService.updateProduct(id, productModifyRequestDTO);

        return new ResponseEntity<>(productModifyResponseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제",
            description = "상품을 삭제합니다.",
            responses = {
                @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
                @ApiResponse(responseCode = "400", description = "잘못 된 값 입력"),
                @ApiResponse(responseCode = "401", description = "인증 오류"),
                @ApiResponse(responseCode = "404", description = "존재하지 않는 productId"),
                @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    public ResponseEntity<Void> DeleteProduct(@LoginUser User user, @PathVariable(name = "productId") Long id) {
        productService.deleteProduct(id);

        return ResponseEntity.noContent().build();
    }

    private void validateProductName(String name) {
        if (name.contains("카카오")) {
            throw CustomException.invalidNameException();
        }
    }
}