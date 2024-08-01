package gift.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.request.ProductRequest;
import gift.dto.response.GetProductsResponse;
import gift.dto.response.ProductResponse;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.validation.BindingResult;

@RestController
@Tag(name = "product", description = "Product API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "전체 상품 목록을 조회합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    public ResponseEntity<GetProductsResponse> getProducts(@RequestParam("categoryId") Long categoryId, @RequestParam("sort") String sort) {
        return new ResponseEntity<>(productService.findAll(categoryId, sort), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 조회", description = "상품에 대한 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    public ResponseEntity<ProductResponse> findProduct(@PathVariable Long productId){
        return new ResponseEntity<>(productService.findById(productId), HttpStatus.OK);
    }

    @PostMapping("/new")
    @Operation(summary = "상품 추가", description = "파라미터로 받은 상품을 추가합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 상품")
    })
    public ResponseEntity<ProductResponse> addProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        return new ResponseEntity<>(productService.addProduct(productRequest), HttpStatus.CREATED);
    }

    @PutMapping("/edit/{productId}")
    @Operation(summary = "상품 수정", description = "파라미터로 받은 상품을 수정합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품 혹은 카테고리"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 상품")
    })
    public ResponseEntity<Void> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        productService.updateProduct(productId, productRequest);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "파라미터로 받은 상품을 삭제합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 삭제 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}