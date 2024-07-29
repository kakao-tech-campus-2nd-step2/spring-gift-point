package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 API 관련 엔드포인트")
public class ProductController {

    private static final String REDIRECT_URL = "redirect:/api/products";

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품의 목록을 조회합니다.")
    public String getProducts(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Page<Product> productPage = productService.getAllProducts(PageRequest.of(page, size));
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", page);
        return "productList";
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public String addProduct(@Valid @ModelAttribute Product product, @RequestParam List<String> optionNames, @RequestParam List<Integer> optionQuantities, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addProduct";
        }

        productService.addProduct(product);

        for (int i = 0; i < optionNames.size(); i++) {
            Option option = new Option();
            option.setName(optionNames.get(i));
            option.setQuantity(optionQuantities.get(i));
            productService.addOptionToProduct(product.getId(), option);

        }

        return REDIRECT_URL;  // 새로운 상품 추가 후 상품 조회 화면으로 리다이렉트
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 폼 조회", description = "새로운 상품을 추가하기 위한 폼을 조회합니다.")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @GetMapping("/{id}/edit")
    @Operation(summary = "상품 수정 폼 조회", description = "상품을 수정하기 위한 폼을 조회합니다.")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return REDIRECT_URL;
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "editProduct";
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    public String editProduct(@PathVariable("id") Long id, @Valid @ModelAttribute Product product, BindingResult bindingResult) {
        // 상품 정보 수정
        if (bindingResult.hasErrors()) {
            return "editProduct";
        }
        productService.updateProduct(id, product);

        return REDIRECT_URL;
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제합니다.")
    public String deleteProduct(@PathVariable("id") Long id) {
        // 요청받은 id를 가진 상품을 삭제
        productService.deleteProduct(id);

        return REDIRECT_URL;
    }

    @GetMapping("/{id}/options")
    @Operation(summary = "상품 옵션 조회", description = "특정 상품의 옵션을 조회합니다.")
    public ResponseEntity<List<Option>> getOptions(@PathVariable("id") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        List<Option> options = product.getOptions();
        return ResponseEntity.ok(options);
    }

    @PostMapping("/{id}/options")
    @Operation(summary = "상품 옵션 추가", description = "특정 상품에 옵션을 추가합니다.")
    public ResponseEntity<String> addOptionToProduct(@PathVariable("id") Long id, @RequestBody @Valid Option option) {
        try {
            productService.addOptionToProduct(id, option);
            return ResponseEntity.status(HttpStatus.CREATED).body("옵션 추가 완료!");
        } catch (DataIntegrityViolationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("동일한 상품 내에 동일한 옵션 이름이 존재합니다.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
