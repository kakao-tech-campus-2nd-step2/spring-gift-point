package gift.controller;


import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.dto.ProductUpdateRequest;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.ProductFacadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product(상품)", description = "Product관련 API입니다.")
public class ProductController {

    ProductFacadeService productService;

    //생성자 주입

    public ProductController(ProductFacadeService productService) {
        this.productService = productService;
    }

//    @Operation(summary = "전체 Product 목록 조회", description = "저장된 모든 상품의 정보를 가져옵니다.")
//    @GetMapping
//    public List<Product> getProduct() {
//        return productService.getAllProducts();
//    }

    @GetMapping("/{productId}")
    @Operation(summary = "ID로 Product 조회", description = "Product의 Id로 상품의 정보를 가져옵니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "400", description = "Bad Request!"),
        @ApiResponse(responseCode = "403", description = "Forbidden! Already Exist"),
    })
    public Product getProductById(
        @Parameter(name = "productId", description = "조회할 상품의 id", example = "1")
        @PathVariable("productId") long productId) {
        return productService.getProductById(productId);
    }


    //Product Pagination
    @GetMapping
    @Operation(summary = "Product를 Page로 조회", description = "여러개의 Product를 페이지네이션 하여 가져옵니다. 페이지당 Product의 기본 설정 개수는 5개입니다.")
    public ResponseEntity<Page<Product>> getProductPage(
        @Parameter(name = "page", description = "페이지 번호", example = "1")
        @RequestParam int page,
        @Parameter(name = "size", description = "페이지 크기", example = "20")
        @RequestParam int size,
        @Parameter(name = "sort", description = "정렬 기준", example = "id,desc")
        @RequestParam String[] sort
    ) {
        Page<Product> products = productService.getProductPage(page,size,sort);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //product 추가
    @PostMapping
    @Operation(summary = "Product 추가", description = "새로운 Product를 추가합니다. Product는 최소 1개의 Option을 가지고 있어야 합니다.")
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        Category category = productService.findCategoryById(productDTO.getCategoryId());
        Product product = productDTO.toEntity(category);
        List<Option> options = new ArrayList<>();
        for (OptionDTO optionDTO : productDTO.getOptions()) {
            options.add(optionDTO.toEntity(product));
        }
        productService.addProduct(product, options);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);

    }


    //product 수정
    @PutMapping("/{productId}")
    @Operation(summary = "Product 수정", description = "id에 해당하는 Product를 새로운 정보로 수정합니다.")
    public ResponseEntity<String> editProduct(
        @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") Long productId,
        @RequestBody ProductUpdateRequest productUpdateRequest) {
        Category category = productService.findCategoryById(productUpdateRequest.getCategoryId());
        Product product = productUpdateRequest.toEntity(category);
        productService.updateProduct(product, productId);

        return new ResponseEntity<>("상품 수정 성공", HttpStatus.OK);

    }

    //product 삭제
    @DeleteMapping("/{productId}")
    @Operation(summary = "Product 삭제", description = "id에 해당하는 Product를 삭제합니다.")
    public ResponseEntity<String> deleteProduct(
        @Parameter(name = "productId", description = "Product Id", example = "1") @PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
        return new ResponseEntity<>("상품 삭제 성공", HttpStatus.NO_CONTENT);
    }


}
