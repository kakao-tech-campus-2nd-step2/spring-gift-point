package gift.controller;

import gift.dto.ApiResponse;
import gift.exception.NonIntegerPriceException;
import gift.exception.ProductNotFoundException;
import gift.model.HttpResult;
import gift.model.Product;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService,
        CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }


    @GetMapping("/products")
    public String getProductList(@RequestParam(value = "page", defaultValue = "0") int page,
        Model model) {
        int pageSize = 10; // 한 페이지에 표시할 상품 개수
        Page<Product> productPage = productService.getProductList(page, pageSize);

        model.addAttribute("productList", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());  // 총 페이지 수를 모델에 추가

        return "getproducts";
    }

    @GetMapping("/{pageNumber}")
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

    @GetMapping("/product/{id}")
    public String getProductById(@PathVariable(name = "id") Long id, Model model) {
        Map<String, Object> response = new HashMap<>();
        try {
            Product product = productService.getProductById(id);
            model.addAttribute("productDto", product);
            return "getproducts";
        } catch (ProductNotFoundException ex) {
            response.put("message", ex.getMessage());
            model.addAttribute("errorMessage", response.get("message"));
            return "getproducts";
        }
    }

    @GetMapping("/product/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDto", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addproductform";
    }

    @PostMapping("/product/add")
    public String createProduct(@Valid @ModelAttribute(name = "product") ProductDto productDto,
        Model model)
        throws NonIntegerPriceException {
        try {
            productService.createProduct(productDto);
            return "redirect:/";
        } catch (NonIntegerPriceException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("product", productDto);  // 입력된 데이터 유지
            return "addproductform";  // 같은 페이지로 돌아감
        }
    }

    @GetMapping(value = "/product/update/{id}")
    public String showUPdateProductForm(@PathVariable("id") Long id, Model model) {
        var product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "updateproductform";
    }

    @PostMapping(value = "/product/update")
    public String updateProduct(@Valid @ModelAttribute(name = "product") Product product) {
        var updatedProduct = productService.updateProduct(product);
        return "redirect:/";
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        Map<String, Object> response = new HashMap<>();
        if (success) {
            response.put("message", "Product deleted successfully.");
            return ResponseEntity.noContent().build();
        }
        response.put("message", "Failed to delete product.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
