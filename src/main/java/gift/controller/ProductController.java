package gift.controller;


import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.ProductFacadeService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@Tag(name = "Product(상품)", description = "Product관련 API입니다.")
public class ProductController {

    ProductFacadeService productService;

    //생성자 주입

    public ProductController(ProductFacadeService productService) {
        this.productService = productService;
    }


    @Operation(summary = "전체 Product 목록 조회", description = "저장된 모든 상품의 정보를 가져옵니다.")
    @GetMapping
    public List<Product> getProduct() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "ID로 Product 조회", description = "Product의 Id로 상품의 정보를 가져옵니다.")
    public Product getProductById(@Parameter(name = "id", description = "Product Id", example = "1")
    @PathVariable("id") long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "ID로 Product 옵션 조회", description = "Product의 Id로 상품의 옵션을 가져옵니다.")
    public List<Option> getProductByIdWithOption(
        @Parameter(name = "id", description = "Product Id", example = "1") @PathVariable("id") long id) {
        return productService.getAllProductOption(id);
    }


    //Product Pagination
    @GetMapping("/page/{page}")
    @Operation(summary = "Product를 Page로 조회", description = "여러개의 Product를 페이지네이션 하여 가져옵니다. 페이지당 Product의 기본 설정 개수는 5개입니다.")
    public ResponseEntity<Page<Product>> getProductPage(
        @Parameter(name = "page", description = "가져올 Page의 번호", example = "1") @PathVariable("page") int page) {
        Page<Product> products = productService.getProductPage(page);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    //product 추가
    @PostMapping
    @Operation(summary = "Product 추가", description = "새로운 Product를 추가합니다. Product는 최소 1개의 Option을 가지고 있어야 합니다.")
    public ResponseEntity<String> addProduct(@RequestBody @Valid ProductDTO productDTO) {
        Category category = productService.findCategoryById(productDTO.getCategoryId());
        Product product = productDTO.toEntity(category);
        List<Option> options = new ArrayList<>();
        for (OptionDTO optionDTO : productDTO.getOption()) {
            options.add(optionDTO.toEntity(product));
        }
        productService.addProduct(product, options);
        return new ResponseEntity<>("OK", HttpStatus.CREATED);

    }


    //product 수정
    @PatchMapping("/{id}")
    @Operation(summary = "Product 수정", description = "id에 해당하는 Product를 새로운 정보로 수정합니다.")
    public ResponseEntity<String> editProduct(
        @Parameter(name = "id", description = "Product Id", example = "1") @PathVariable("id") Long id,
        @RequestBody ProductDTO productDTO) {
        Category category = productService.findCategoryById(productDTO.getCategoryId());
        Product product = productDTO.toEntity(category);
        productService.updateProduct(product, id);

        return new ResponseEntity<>("product edit success", HttpStatus.OK);

    }

    //product 삭제
    @DeleteMapping("/{id}")
    @Operation(summary = "Product 삭제", description = "id에 해당하는 Product를 삭제합니다.")
    public ResponseEntity<String> deleteProduct(
        @Parameter(name = "id", description = "Product Id", example = "1") @PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>("product delete success", HttpStatus.NO_CONTENT);
    }


}
