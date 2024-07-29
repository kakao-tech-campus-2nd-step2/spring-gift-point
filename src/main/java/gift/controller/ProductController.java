package gift.controller;

import org.hibernate.validator.cfg.defs.pl.REGONDef;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gift.dto.ProductDto;
import gift.dto.request.ProductCreateRequest;
import gift.dto.response.ProductPageResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@RestController
@Tag(name = "product", description = "Product API")
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService){
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 조회", description = "파라미터로 받은 상품 페이지를 조회합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    public ResponseEntity<List<ProductDto>> getProducts() {
        List<ProductDto> products = productService.findAll();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping("/new")
    @Operation(summary = "상품 추가", description = "파라미터로 받은 상품을 추가합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 상품")
    })
    public ResponseEntity<Void> addProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest, BindingResult bindingResult) {
        productService.addProduct(productCreateRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    @Operation(summary = "상품 수정", description = "파라미터로 받은 상품을 수정합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품 혹은 카테고리")
    })
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto, BindingResult bindingResult) {

        productService.updateProduct(productDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "파라미터로 받은 상품을 삭제합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}