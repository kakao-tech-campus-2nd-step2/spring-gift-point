package gift.product.controller;

import static gift.global.dto.ApiResponseDto.SUCCESS;

import gift.global.dto.ApiResponseDto;
import gift.global.dto.PageInfoDto;
import gift.option.dto.OptionRequestDto;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ProductApiController {

    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    // 제품을 추가하는 핸들러
    @PostMapping("/admin/products")
    public ApiResponseDto<Void> createProduct(
        @RequestBody @Valid CreateProductRequestDto createProductRequestDto) {
        productService.insertProduct(createProductRequestDto);

        return SUCCESS();
    }

    // 페이지 내의 제품을 조회하는 핸들러.
    @GetMapping("/products")
    public ApiResponseDto<List<ProductResponseDto>> readUserProducts(
        @ModelAttribute PageInfoDto pageInfoDto) {
        return SUCCESS(productService.selectProducts(pageInfoDto));
    }

    // 단일 제품 조회 핸들러.
    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductResponseDto> readUserProduct(
        @PathVariable(name = "productId") long productId) {
        return ResponseEntity.ok(productService.selectProduct(productId));
    }

    // id가 i인 상품을 수정하는 핸들러
    @PutMapping("/admin/products/{productId}")
    public ApiResponseDto<Void> updateProduct(@PathVariable(name = "productId") long productId,
        @RequestBody @Valid ProductRequestDto productRequestDto,
        @RequestParam(name = "categoryId") long categoryId) {
        // service를 호출해서 제품 수정
        productService.updateProduct(productId, productRequestDto, categoryId);

        return SUCCESS();
    }

    // 제품에 새로운 옵션을 추가하는 핸들러
    // 최소 하나 이상의 옵션이 있어야 하므로 제품을 추가할 때도 기본 옵션을 받도록 하고, 해당 핸들러로 옵션을 더 추가하도록 함.
    @PutMapping("/admin/products/{productId}/options")
    public ApiResponseDto<Void> addProductOption(@PathVariable(name = "productId") long productId,
        @RequestBody OptionRequestDto optionRequestDto) {
        productService.insertOption(productId, optionRequestDto);

        return SUCCESS();
    }

    // id가 i인 상품을 삭제하는 핸들러
    @DeleteMapping("/admin/products/{productId}")
    public ApiResponseDto<Void> deleteProduct(@PathVariable(name = "productId") long productId) {
        // service를 사용해서 하나의 제품 제거
        productService.deleteProduct(productId);

        return SUCCESS();
    }
}
