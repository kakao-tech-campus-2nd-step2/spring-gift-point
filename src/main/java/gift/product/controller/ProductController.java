package gift.product.controller;

import gift.category.service.CategoryService;
import gift.dto.ApiResponse;
import gift.exception.NonIntegerPriceException;
import gift.exception.OptionNotFoundException;
import gift.model.HttpResult;
import gift.option.service.OptionService;
import gift.product.dto.ProductRequest;
import gift.product.dto.ProductResponse;
import gift.product.model.Product;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;
    private CategoryService categoryService;
    private OptionService optionService;

    public ProductController(ProductService productService, CategoryService categoryService,
        OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    public ResponseEntity<ProductResponse> getPagination(
        @RequestParam(value = "page", defaultValue = "0") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sort", defaultValue = "name,ASC") String sort,
        @RequestParam(value = "categoryId", defaultValue = "1") Long categoryId, Model model) {
        String[] sortParams = sort.split(",");
        String property = sortParams[0];
        Direction direction = sortParams.length > 1 ? Direction.valueOf(sortParams[1]) : Direction.DESC;

        Page<Product> productList = productService.getProductList(page, size, property, direction);
        model.addAttribute("productList", productList.getContent());
        model.addAttribute("totalPages", productList.getTotalPages());

        return ResponseEntity.ok(
            new ProductResponse(HttpResult.OK, "상품 조회 성공", HttpStatus.OK, productList));
    }


    @GetMapping("/page/{pageNumber}")
    public String getMyProductsPage(@PathVariable("pageNumber") int pageNumber, Model model) {
        var productList = productService.getProductPages(pageNumber);
        model.addAttribute("productList", productList.getContent());
        return "getproducts";
    }

    @GetMapping("/products/all")
    public ResponseEntity<ApiResponse> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        Map<String, Object> response = new HashMap<>();
        response.put("message", "All products retrieved successfully.");
        response.put("products", products);
        var productsRetreiveSucess = new ApiResponse(HttpResult.OK, "상품 전체 조회 성공", HttpStatus.OK);
        return new ResponseEntity<>(productsRetreiveSucess, productsRetreiveSucess.getHttpStatus());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable(name = "productId") Long id,
        Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("productDto", product);
        return ResponseEntity.ok(
            new ProductResponse(HttpResult.OK, "상품 조회 성공", HttpStatus.OK, product));
    }

    @GetMapping("/product/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDto", new ProductRequest());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addproductform";
    }

    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(
        @Valid @RequestBody ProductRequest productRequest)
        throws NonIntegerPriceException, OptionNotFoundException {
        Product product = productService.createProduct(productRequest);
        return ResponseEntity.ok(
            new ProductResponse(HttpResult.OK, "상품 추가 성공", HttpStatus.OK, product));
    }

    @GetMapping(value = "/product/update/{id}")
    public String showUPdateProductForm(@PathVariable("id") Long id, Model model) {
        var product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "updateproductform";
    }

    @PutMapping(value = "/product/update")
    public String updateProduct(@Valid @ModelAttribute(name = "product") Product product) {
//        var updatedProduct = productService.updateProduct(product);
//        return "redirect:/";
        return null;
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> patchProduct(@PathVariable Long id,
        @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        if (productService.patchProduct(id, updates) != null) {
            Product updatedProduct = productService.getProductById(id);
            response.put("message", "Product patched successfully.");
            response.put("product", updatedProduct);
            return ResponseEntity.ok(response);
        }
        response.put("message", "Failed to patch product.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @PatchMapping
    public ResponseEntity<Map<String, Object>> patchProducts(
        @RequestBody List<Map<String, Object>> updatesList) {
        List<Optional<Product>> updatedProducts = productService.patchProducts(updatesList);
        Map<String, Object> response = new HashMap<>();
        int originalCount = updatesList.size();
        int updateCount = updatedProducts.size();

        response.put("updatedProducts", updatedProducts);

        if (updateCount == originalCount) {
            response.put("message", "All products patched successfully.");
            return ResponseEntity.ok(response);
        }

        if (updateCount > 0) {
            response.put("message", "Some products patched successfully.");
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }

        response.put("message", "No products patched.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/product/delete/{id}")
    public String showDeleteProductForm(@PathVariable("id") Long id, Model model) {
        if (!productService.deleteProduct(id)) {
            model.addAttribute("errorMessage", "잘못됨");
        }
        return "redirect:/";
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(
        @PathVariable(value = "productId") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ProductResponse(HttpResult.OK, "상품 삭제 성공", HttpStatus.OK,
            null));
//        if (success) {
//            return ResponseEntity.ok(new ProductResponse(HttpResult.OK, "상품 삭제 성공", HttpStatus.OK,
//                null));
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//            new ProductResponse(HttpResult.ERROR, "상품 삭제 실패", HttpStatus.NOT_FOUND,
//                null));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
        @PathVariable(value = "productId") Long productId,
        @RequestBody ProductRequest productRequest) {
        var updatedProduct = productService.updateProduct(productId, productRequest);
        return ResponseEntity.ok(new ProductResponse(HttpResult.OK, "상품 수정 성공", HttpStatus.OK,
            updatedProduct));
    }

}
