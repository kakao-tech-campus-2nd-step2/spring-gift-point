package gift.product.controller;

import static gift.product.exception.GlobalExceptionHandler.UNKNOWN_VALIDATION_ERROR;

import gift.product.dto.ProductDTO;
import gift.product.model.Product;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

@Tag(name = "Product", description = "API 컨트롤러")
@RestController
@RequestMapping("/api/products")
public class ApiProductController {

    private final ProductService productService;

    @Autowired
    public ApiProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
        summary = "상품 목록",
        description = "등록된 상품 목록을 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "상품 목록 조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "사용자 요청에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @GetMapping("/list")
    public Page<Product> showProductList(Pageable pageable) {
        System.out.println("[ProductController] showProductList()");
        return productService.getAllProducts(pageable);
    }

    @Operation(
        summary = "상품 등록",
        description = "상품을 등록합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "상품 등록 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Product.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "등록하고자 하는 상품의 정보를 제대로 입력하지 않은 경우"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "상품 명에 \"카카오\"를 포함하여 상품의 등록을 시도한 경우(담당 MD와 협의한 경우에만 사용이 가능)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 카테고리로 상품의 등록을 시도한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "상품 등록 정보에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
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

    @Operation(
        summary = "상품 수정",
        description = "기등록된 상품을 수정합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "상품 수정 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Product.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "등록하고자 하는 상품의 정보를 제대로 입력하지 않은 경우"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "403",
            description = "상품 명에 \"카카오\"를 포함하여 상품의 수정을 시도한 경우(담당 MD와 협의한 경우에만 사용이 가능)"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 카테고리로 수정을 시도한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "상품 수정 정보에는 문제가 없으나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
        @PathVariable Long id,
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
            .body(productService.updateProduct(id, productDTO));
    }

    @Operation(
        summary = "상품 삭제",
        description = "상품을 삭제합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "상품 삭제 성공"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "존재하지 않는 상품의 삭제를 시도한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "상품 삭제 요청은 올바르나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        System.out.println("[ProductController] deleteProduct()");
        productService.deleteProduct(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @Operation(
        summary = "상품 키워드 조회",
        description = "상품 명에 특정 키워드가 포함된 상품 목록을 조회합니다."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "상품 명에 특정 키워드가 포함된 상품 목록 조회 성공",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = Page.class)
            )
        ),
        @ApiResponse(
            responseCode = "401",
            description = "인증과 관련된 문제(인증 헤더 누락 또는 토큰 인증 실패)가 발생한 경우"
        ),
        @ApiResponse(
            responseCode = "500",
            description = "사용자의 요청은 올바르나, 서버가 이를 올바르게 처리하지 못한 경우"
        )
    })
    @GetMapping("/search")
    public Page<Product> searchProduct(
        @RequestParam("keyword") String keyword,
        Pageable pageable) {
        System.out.println("[ProductController] searchProduct()");
        return productService.searchProducts(keyword, pageable);
    }
}
