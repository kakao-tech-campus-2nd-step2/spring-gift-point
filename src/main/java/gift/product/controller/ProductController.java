package gift.product.controller;

import gift.global.exception.DomainValidationException;
import gift.global.response.ErrorResponseDto;
import gift.global.response.ResultCode;
import gift.global.response.ResultResponseDto;
import gift.global.response.SimpleResultResponseDto;
import gift.global.utils.ResponseHelper;
import gift.product.domain.Product;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseListDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 차후에 삭제 예정
    @GetMapping("/all")
    public ResponseEntity<ResultResponseDto<List<Product>>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseHelper.createResponse(ResultCode.GET_ALL_PRODUCTS_SUCCESS, products);
    }

    @GetMapping("")
    public ResponseEntity<ResultResponseDto<ProductResponseListDto>> getProductsByPage(@RequestParam(name = "page") int page) {
        ProductResponseListDto products = productService.getProductsByPage(page);
        return ResponseHelper.createResponse(ResultCode.GET_ALL_PRODUCTS_SUCCESS, products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResultResponseDto<Product>> getProductById(@PathVariable(name = "id") Long id) {
        Product product = productService.getProductById(id);
        return ResponseHelper.createResponse(ResultCode.GET_PRODUCT_BY_ID_SUCCESS, product);
    }

    @PostMapping("")
    public ResponseEntity<SimpleResultResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        productService.createProduct(productRequestDto.toProductServiceDto());
        return ResponseHelper.createSimpleResponse(ResultCode.CREATE_PRODUCT_SUCCESS);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> updateProduct(@PathVariable(name = "id") Long id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        productService.updateProduct(productRequestDto.toProductServiceDto(id));
        return ResponseHelper.createSimpleResponse(ResultCode.UPDATE_PRODUCT_SUCCESS);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<SimpleResultResponseDto> deleteProduct(@PathVariable(name = "id") Long id) {
        productService.deleteProduct(id);
        return ResponseHelper.createSimpleResponse(ResultCode.DELETE_PRODUCT_SUCCESS);
    }

    // GlobalException Handler 에서 처리할 경우,
    // RequestBody에서 발생한 에러가 HttpMessageNotReadableException 로 Wrapping 이 되는 문제가 발생한다
    // 때문에, 해당 에러로 Wrapping 되기 전 Controller 에서 Domain Error 를 처리해주었다
    @ExceptionHandler(DomainValidationException.class)
    public ResponseEntity<ErrorResponseDto> handleOptionValidException(DomainValidationException e) {
        System.out.println(e);
        return ResponseHelper.createErrorResponse(e.getErrorCode());
    }
}