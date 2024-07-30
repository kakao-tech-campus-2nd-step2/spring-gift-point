package gift.controller;

import gift.domain.Product;
import gift.domain.Product.ProductDetail;
import gift.domain.Product.ProductSimple;
import gift.service.ProductService;
import gift.util.page.PageMapper;
import gift.util.page.PageResult;
import gift.util.page.SingleResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "상품관련 서비스")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 리스트 조회(간략)", description = "상품의 id와 이름을 반환합니다.")
    @GetMapping
    public PageResult<ProductSimple> getSimpleProductList(@Valid Product.getList param) {
        return PageMapper.toPageResult(productService.getSimpleProductList(param));
    }

    @Operation(summary = "상품 리스트 조회(상세)",
        description = "상품의 id,이름,가격,이미지,생성일,수정일, 해당 상품을 위시리스트에 포함함 유저id 리스트, 속해있는 카테고리id를 반환합니다.")
    @GetMapping("/detail")
    public PageResult<ProductDetail> getProductList(@Valid Product.getList param) {
        return PageMapper.toPageResult(productService.getProductList(param));
    }

    @Operation(summary = "상품 단일 조회",
        description = "상품의 id,이름,가격,이미지,생성일,수정일, 해당 상품을 위시리스트에 포함함 유저id 리스트, 속해있는 카테고리id를 반환합니다.")
    @GetMapping("/{id}")
    public SingleResult<ProductDetail> getProduct(@PathVariable long id) {
        return new SingleResult<>(productService.getProduct(id));
    }

    @Operation(summary = "상품 생성", description = "상품을 생성합니다.")
    @PostMapping
    public SingleResult<Long> createProduct(@Valid @RequestBody Product.CreateProduct create) {
        SingleResult singleResult = new SingleResult<>(productService.createProduct(create));
        singleResult.setErrorCode(HttpStatus.CREATED.value());
        return singleResult;
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @PutMapping("/{id}")
    public SingleResult<Long> updateProduct(@Valid @RequestBody Product.UpdateProduct update,
        @PathVariable long id) {

        SingleResult singleResult = new SingleResult<>(productService.updateProduct(update, id));
        singleResult.setErrorCode(HttpStatus.ACCEPTED.value());
        return singleResult;
    }

    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    @DeleteMapping("/{id}")
    public SingleResult<Long> deleteProduct(@PathVariable long id) {
        return new SingleResult<>(productService.deleteProduct(id));

    }
}
