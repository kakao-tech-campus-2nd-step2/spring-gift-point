package gift.web.controller.api;

import gift.authentication.annotation.LoginMember;
import gift.service.OrderService;
import gift.service.ProductOptionService;
import gift.service.ProductService;
import gift.service.WishProductService;
import gift.web.dto.MemberDetails;
import gift.web.dto.request.order.CreateOrderRequest;
import gift.web.dto.request.product.CreateProductRequest;
import gift.web.dto.request.product.UpdateProductRequest;
import gift.web.dto.request.productoption.CreateProductOptionRequest;
import gift.web.dto.request.productoption.UpdateProductOptionRequest;
import gift.web.dto.request.wishproduct.CreateWishProductRequest;
import gift.web.dto.response.order.OrderResponse;
import gift.web.dto.response.product.CreateProductResponse;
import gift.web.dto.response.product.ReadAllProductsResponse;
import gift.web.dto.response.product.ReadProductResponse;
import gift.web.dto.response.product.UpdateProductResponse;
import gift.web.dto.response.productoption.CreateProductOptionResponse;
import gift.web.dto.response.productoption.ReadAllProductOptionsResponse;
import gift.web.dto.response.productoption.UpdateProductOptionResponse;
import gift.web.dto.response.wishproduct.CreateWishProductResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;
    private final WishProductService wishProductService;
    private final ProductOptionService productOptionService;
    private final OrderService orderService;

    public ProductApiController(ProductService productService, WishProductService wishProductService, ProductOptionService productOptionService,
        OrderService orderService) {
        this.productService = productService;
        this.wishProductService = wishProductService;
        this.productOptionService = productOptionService;
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<ReadAllProductsResponse> readAllProducts(@PageableDefault Pageable pageable) {
        ReadAllProductsResponse response = productService.readAllProducts(pageable);
        return ResponseEntity.ok(response);
    }

    @GetMapping(params = "categoryId")
    public ResponseEntity<ReadAllProductsResponse> readProductsByCategoryId(@PageableDefault Pageable pageable, @RequestParam Long categoryId) {
        ReadAllProductsResponse response = productService.readProductsByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CreateProductResponse> createProduct(
        @Validated @RequestBody CreateProductRequest request) throws URISyntaxException {
        CreateProductResponse response = productService.createProduct(request);

        URI location = new URI("http://localhost:8080/api/products/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ReadProductResponse> readProduct(@PathVariable Long productId) {
        ReadProductResponse response;
        response = productService.readProductById(productId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<UpdateProductResponse> updateProduct(@PathVariable Long productId, @Validated @RequestBody UpdateProductRequest request) {
        UpdateProductResponse response;
        try {
            response = productService.updateProduct(productId, request);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/wish")
    public ResponseEntity<CreateWishProductResponse> createWishProduct(@Validated @RequestBody CreateWishProductRequest request, @LoginMember MemberDetails memberDetails) {
        CreateWishProductResponse response = wishProductService.createWishProduct(memberDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{productId}/options")
    public ResponseEntity<ReadAllProductOptionsResponse> readOptions(@PathVariable Long productId) {
        ReadAllProductOptionsResponse response = productOptionService.readAllOptions(productId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{productId}/options")
    public ResponseEntity<CreateProductOptionResponse> createOption(@PathVariable Long productId, @Validated @RequestBody CreateProductOptionRequest request) {
        CreateProductOptionResponse response = productOptionService.createOption(productId, request);

        URI location = URI.create("http://localhost:8080/api/products/" + productId + "/options/" + response.getId());
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/{productId}/order")
    public ResponseEntity<OrderResponse> orderProduct(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
        @PathVariable Long productId,
        @RequestBody @Validated CreateOrderRequest request,
        @LoginMember MemberDetails memberDetails
    ) {
        OrderResponse response = orderService.createOrder(accessToken, productId, memberDetails.getId(), request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{productId}/options/{optionId}")
    public ResponseEntity<UpdateProductOptionResponse> updateOption(@PathVariable Long productId, @PathVariable Long optionId, @Validated @RequestBody UpdateProductOptionRequest request) {
        UpdateProductOptionResponse response = productOptionService.updateOption(optionId, productId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long optionId) {
        productOptionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }
}
