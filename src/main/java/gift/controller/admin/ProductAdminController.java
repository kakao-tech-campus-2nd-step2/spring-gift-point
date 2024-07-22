package gift.controller.admin;

import gift.dto.category.CategoryResponse;
import gift.dto.option.OptionCreateRequest;
import gift.dto.option.OptionResponse;
import gift.dto.option.OptionUpdateRequest;
import gift.dto.product.ProductCreateRequest;
import gift.dto.product.ProductResponse;
import gift.dto.product.ProductUpdateRequest;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public class ProductAdminController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductAdminController(
        ProductService productService,
        CategoryService categoryService,
        OptionService optionService
    ) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    public String getAllProducts(
        Model model,
        @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        Page<ProductResponse> productPage = productService.getAllProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "products";
    }

    @GetMapping("/new")
    public String showAddProductForm(Model model) {
        List<CategoryResponse> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("product", new ProductCreateRequest("", 0, "", null));
        return "product_form";
    }

    @PostMapping
    public String addProduct(
        @Valid @ModelAttribute("product") ProductCreateRequest productCreateRequest,
        BindingResult result, Model model
    ) {
        if (result.hasErrors()) {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "product_form";
        }
        productService.addProduct(productCreateRequest);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/edit")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        ProductResponse productResponse = productService.getProductById(id);
        List<CategoryResponse> categories = categoryService.getAllCategories();
        List<OptionResponse> options = optionService.getOptionsByProductId(id);
        model.addAttribute("categories", categories);
        model.addAttribute("product", productResponse);
        model.addAttribute("options", options);
        return "product_edit";
    }

    @PutMapping("/{id}")
    public String updateProduct(
        @PathVariable("id") Long id,
        @Valid @ModelAttribute ProductUpdateRequest productUpdateRequest, BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            List<CategoryResponse> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            model.addAttribute("product", productUpdateRequest);
            model.addAttribute("org.springframework.validation.BindingResult.product", result);
            return "product_edit";
        }
        productService.updateProduct(id, productUpdateRequest);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/{id}/options/new")
    public String showAddOptionForm(@PathVariable Long id, Model model) {
        model.addAttribute("option", new OptionCreateRequest("", 0));
        model.addAttribute("productId", id);
        return "option_form";
    }

    @PostMapping("/{id}/options")
    public String addOption(
        @PathVariable Long id,
        @Valid @ModelAttribute("option") OptionCreateRequest optionCreateRequest,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("productId", id);
            return "option_form";
        }
        optionService.addOptionToProduct(id, optionCreateRequest);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @GetMapping("/{productId}/options/{optionId}/edit")
    public String showEditOptionForm(
        @PathVariable Long productId,
        @PathVariable Long optionId, Model model
    ) {
        OptionResponse optionResponse = optionService.getOptionById(productId, optionId);
        model.addAttribute("option", optionResponse);
        model.addAttribute("productId", productId);
        return "option_edit";
    }

    @PutMapping("/{productId}/options/{optionId}")
    public String updateOption(
        @PathVariable Long productId,
        @PathVariable Long optionId,
        @Valid @ModelAttribute OptionUpdateRequest optionUpdateRequest,
        BindingResult result,
        Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("option", optionUpdateRequest);
            model.addAttribute("productId", productId);
            return "option_edit";
        }
        optionService.updateOption(productId, optionId, optionUpdateRequest);
        return "redirect:/admin/products/" + productId + "/edit";
    }

    @DeleteMapping("/{productId}/options/{optionId}")
    public String deleteOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteOption(productId, optionId);
        return "redirect:/admin/products/" + productId + "/edit";
    }
}
