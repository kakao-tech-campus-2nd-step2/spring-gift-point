package gift.controller;

import gift.dto.ProductDto;
import gift.dto.response.CategoryResponse;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/v2/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final OptionService optionService;


    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, OptionService optionService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.optionService = optionService;

    }

    @PostMapping("/add")
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public String addProduct(@Valid @ModelAttribute("product") ProductDto productDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("product", new ProductDto());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "add-product";
        }
        productService.addProduct(productDto);
        return "redirect:/view/v2/products";
    }

    @PostMapping("/edit/{id}")
    @Operation(summary = "상품 수정", description = "기존 상품을 수정합니다.")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductDto productDto, BindingResult result, Model model) {
        Product product = productService.getProductById(productDto.getId());
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            model.addAttribute("options", optionService.getAllOptions());
            model.addAttribute("product", new ProductDto(product));
            return "edit-product";
        }
        if (productDto.getName().contains("카카오")) {
            result.rejectValue("name", "error.product", "상품 이름에 '카카오'가 포함되어 있습니다. 담당 MD와 협의하십시오.");
            List<CategoryResponse> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "edit-product";
        }
        productService.updateProduct(id, productDto);
        return "redirect:/view/v2/products";
    }

}
