package gift.controller;

import gift.dto.ProductDTO;
import gift.model.Product;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
@Tag(name = "상품 관리 API", description = "상품 관리를 위한 API")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 얻기", description = "모든 상품을 페이지로 조회합니다.")
    public String getProducts(Model model, @PageableDefault(size = 5) Pageable pageable) {
        Page<Product> products = productService.findAllProducts(pageable);
        model.addAttribute("products", products);
        return "product_list";
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 폼 보기", description = "상품을 추가할 수 있는 폼으로 이동합니다.")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductDTO("", "0", null, ""));
        model.addAttribute("categories", categoryService.findAllCategories());
        return "add_product_form";
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public String addProduct(@ModelAttribute @Valid ProductDTO productDTO, BindingResult result,
        Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productDTO", productDTO);
            model.addAttribute("categories", categoryService.findAllCategories());
            return "add_product_form";
        }
        productService.saveProduct(productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 수정 폼 보기", description = "상품을 수정할 수 있는 폼으로 이동합니다.")
    public String showEditProductForm(@PathVariable("id") long id, Model model) {
        Product product = productService.findProductsById(id);
        model.addAttribute("productDTO", ProductService.toDTO(product));
        model.addAttribute("productID", id);
        model.addAttribute("categories", categoryService.findAllCategories());
        return "edit_product_form";
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public String editProduct(@PathVariable("id") long id,
        @ModelAttribute @Valid ProductDTO updatedProductDTO,
        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productID", id);
            model.addAttribute("categories", categoryService.findAllCategories());
            return "edit_product_form";
        }
        productService.updateProduct(updatedProductDTO, id);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public String deleteProduct(@PathVariable("id") long id) {
        productService.deleteProductAndWishlistAndOptions(id);
        return "redirect:/admin/products";
    }

}
