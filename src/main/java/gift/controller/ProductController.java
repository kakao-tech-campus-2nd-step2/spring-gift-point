package gift.controller;

import gift.dto.OneProductResponceDTO;
import gift.dto.ProductRequestDTO;
import gift.dto.ProductResponseDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/products")
@Tag(name = "ProductController", description = "상품 관리 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "상품 목록 조회", description = "모든 상품을 페이지네이션하여 조회합니다.")
    @GetMapping
    public String getProducts(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              @RequestParam(defaultValue = "name,asc") String[] sort) {
        model.addAttribute("productPage", productService.getProducts(page, size, sort));
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        return "products";
    }

    @Operation(summary = "상품 조회", description = "특정 상품의 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<OneProductResponceDTO> getProductById(@PathVariable Long productId) {
        OneProductResponceDTO product = productService.getProductById(productId);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "새 상품 폼 조회", description = "새로운 상품을 추가하는 폼을 조회합니다.")
    @GetMapping("/new")
    public String newProductForm(Model model) {
        model.addAttribute("productFormDTO", new ProductRequestDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productForm";
    }

    @Operation(summary = "상품 생성", description = "새로운 상품을 생성합니다.")
    @PostMapping
    public String createProduct(@Valid @ModelAttribute("productFormDTO") ProductRequestDTO productRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productForm";
        }

        if (productRequestDTO.getName().contains("카카오")) {
            model.addAttribute("errorMessage", "담당 MD와 협의된 경우에만 '카카오'를 포함할 수 있습니다.");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productForm";
        }

        productService.createProduct(productRequestDTO);
        return "redirect:/api/products";
    }

    @Operation(summary = "상품 수정 폼 조회", description = "기존 상품을 수정하는 폼을 조회합니다.")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        OneProductResponceDTO productResponseDTO = productService.getProductById(id);
        model.addAttribute("productFormDTO", productResponseDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "productForm";
    }

    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("productFormDTO") ProductRequestDTO productRequestDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productForm";
        }

        if (productRequestDTO.getName().contains("카카오")) {
            model.addAttribute("errorMessage", "담당 MD와 협의된 경우에만 '카카오'를 포함할 수 있습니다.");
            model.addAttribute("categories", categoryService.getAllCategories());
            return "productForm";
        }

        productService.updateProduct(id, productRequestDTO);
        return "redirect:/api/products";
    }

    @Operation(summary = "상품 삭제", description = "기존 상품을 삭제합니다.")
    @DeleteMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/products";
    }
}
