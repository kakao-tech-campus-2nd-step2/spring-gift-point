package gift.controller;

import gift.dto.DomainResponse;
import gift.model.HttpResult;
import gift.model.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "APIs related to product operations")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    @PostMapping
    public ResponseEntity<DomainResponse> addProduct(@Valid @RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), "Validation errors");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(Map.of("errors", bindingResult.getAllErrors())), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        product.setCategory(categoryService.findById(product.getCategory().getId()));
        productService.save(product);
        HttpResult httpResult = new HttpResult(HttpStatus.CREATED.value(), "Product created successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    @PutMapping("/{id}")
    public ResponseEntity<DomainResponse> updateProduct(@PathVariable Long id, @Valid @RequestBody Product updatedProduct, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), "Validation errors");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(errors), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            HttpResult httpResult = new HttpResult(HttpStatus.NOT_FOUND.value(), "Product not found");
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        existingProduct.setCategory(categoryService.findById(updatedProduct.getCategory().getId()));
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageurl(updatedProduct.getImageurl());
        existingProduct.setOptions(updatedProduct.getOptions());

        productService.update(existingProduct);
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Product updated successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<DomainResponse> getAllProducts(Pageable pageable) {
        Page<Product> products = productService.getAllProducts(pageable);
        List<Map<String, Object>> productList = new ArrayList<>();
        productList.add(Map.of("products", products));
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Products retrieved successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, productList, HttpStatus.OK));
    }

    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    @GetMapping("/{id}")
    public ResponseEntity<DomainResponse> getProductById(@PathVariable Long id) {
        Product product = productService.findById(id);
        if (product == null) {
            HttpResult httpResult = new HttpResult(HttpStatus.NOT_FOUND.value(), "Product not found");
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        productMap.put("price", product.getPrice());
        productMap.put("imageurl", product.getImageurl());
        productMap.put("category", product.getCategory().getName());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Product retrieved successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(productMap), HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<DomainResponse> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Product deleted successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }
}