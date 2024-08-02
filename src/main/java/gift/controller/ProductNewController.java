package gift.controller;

import gift.dto.response.ProductNewResponse;
import gift.entity.Product;
import gift.service.ProductService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "[협업] PRODUCT API", description = "[협업] 상품 컨트롤러")
public class ProductNewController {

    private final ProductService productService;
    private final WishlistService wishlistService;

    public ProductNewController(ProductService productService, WishlistService wishlistService) {
        this.productService = productService;
        this.wishlistService= wishlistService;
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductNewResponse> getProduct(@PathVariable Long id, @RequestAttribute("userId") Long userId){
        Product product = productService.getProductById(id);
        boolean isWish = wishlistService.isProductInWishlist(userId, id);
        ProductNewResponse response = new ProductNewResponse(product, isWish);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "제품 데이터", description = "페이지네이션된 제품 데이터를 반환합니다.")
    @ResponseBody
    public ResponseEntity<List<ProductNewResponse>> getProducts(@PageableDefault(size = 5, sort = "id", direction = Direction.DESC) Pageable pageable, @RequestAttribute("userId") Long userId) {
        Page<Product> productPage = productService.getProducts(pageable);
        List<ProductNewResponse> productResponsesList = productPage.getContent().stream()
                .map(product -> {
                    boolean isWish = wishlistService.isProductInWishlist(userId, product.getId());
                    return new ProductNewResponse(product, isWish);
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(productResponsesList);
    }





}
