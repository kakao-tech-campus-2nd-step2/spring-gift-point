package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.request.OptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    public ResponseEntity<List<OptionResponse>> getOptions(@PathVariable Long productId) {
        List<OptionResponse> options = optionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }

    @PostMapping("/{productId}/options")
    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    public ResponseEntity<Option> addOption(@PathVariable Long productId, @ModelAttribute @Valid OptionRequest optionRequest) {
        Option createdOption = optionService.addOptionToProduct(productId, optionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOption);
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    @Operation(summary = "상품 옵션 삭제", description = "기존 상품 옵션을 삭제한다.")
    public ResponseEntity<Void> deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
    public String getProducts(Model model, Pageable pageable) {
        Page<Product> products = productService.getProducts(pageable);
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회한다.")
    public String getProduct(@PathVariable Long id, Model model) {
        model.addAttribute("products", productService.findOne(id));
        return "product-list";
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 화면", description = "상품 추가화면으로 이동한다.")
    public String newProductForm(Model model) {
        model.addAttribute("product", new ProductRequest());
        model.addAttribute("categories", categoryService.getCategories());
        return "product-add-form";
    }

    @PostMapping
    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    public String addProduct(@Valid @ModelAttribute ProductRequest productRequest, @Valid @ModelAttribute OptionRequest optionRequest) {
        productService.register(productRequest, optionRequest);
        return "redirect:/api/products";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "상품 수정 화면", description = "상품 수정 화면으로 이동한다.")
    public String editProductForm(@PathVariable long id, Model model) {
        Product product = productService.findOne(id);
        ProductResponse productResponse = ProductResponse.EntityToResponse(product);
        model.addAttribute("productResponse", productResponse);
        model.addAttribute("categories", categoryService.getCategories());
        return "product-edit-form";
    }

    @PostMapping("/edit/{id}")
    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute ProductRequest productRequest) {
        productService.update(id, productRequest);
        return "redirect:/api/products";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제한다.")
    public String deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/api/products";
    }

}
