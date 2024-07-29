package gift.controller;

import gift.domain.Category;
import gift.dto.request.OptionRequest;
import gift.dto.request.ProductOptionRequest;
import gift.dto.request.ProductRequest;
import gift.dto.response.CategoryResponse;
import gift.dto.response.OptionResponse;
import gift.dto.response.ProductResponse;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products/list";
    }

    @GetMapping("/new")
    public String showAddProductForm(Model model){
        List<CategoryResponse> categories = categoryService.findAll(); // 모든 카테고리를 가져오는 서비스 메서드
        model.addAttribute("categories", categories);
        model.addAttribute("productOptionDto", new ProductOptionRequest(new ProductRequest(null, null, 0, null, null), new OptionRequest(null, 0)));
        return "products/add";
    }

    @PostMapping
    public String addProduct(@Valid @ModelAttribute ProductOptionRequest productOptionDto) {
        productService.save(productOptionDto.getProductRequest(), productOptionDto.getOptionRequest());
        return "redirect:/products";
    }

    @GetMapping("/edit/{productId}")
    public String showEditProductForm(@PathVariable Long productId, Model model){
        List<CategoryResponse> categories = categoryService.findAll(); // 모든 카테고리를 가져오는 서비스 메서드
        model.addAttribute("categories", categories);

        ProductResponse productDto = productService.findById(productId);
        model.addAttribute("productDto", new ProductRequest(productId, productDto.name(), productDto.price(), productDto.imageUrl(), productDto.categoryResponse().id()));
        return "products/edit";
    }

    @PostMapping("/update/{productId}")
    public String updateProduct(@PathVariable Long productId, @Valid @ModelAttribute ProductRequest productDto){
        productService.updateById(productId, productDto);
        return "redirect:/products";
    }

    @GetMapping("/delete/{productId}")
    public String deleteProduct(@PathVariable Long productId) {
        productService.deleteById(productId);
        return "redirect:/products";
    }

    @GetMapping("/{productId}/options")
    public String listProductOptions(@PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("options", optionService.findByProductId(productId));
        return "options/list";
    }

    @GetMapping("/{productId}/options/new")
    public String showAddProductOptionForm(@PathVariable Long productId, Model model){
        model.addAttribute("productId", productId);
        model.addAttribute("optionDto", new OptionRequest(null,0));
        return "options/add";
    }

    @PostMapping("/{productId}/options")
    public String addProductOption (@PathVariable Long productId, @Valid @ModelAttribute OptionRequest optionDto) {
        optionService.save(productId, optionDto);
        return "redirect:/products/"+productId+"/options";
    }


    @GetMapping("/{productId}/options/edit/{optionId}")
    public String showEditProductOptionForm(@PathVariable Long productId, @PathVariable Long optionId, Model model){
        model.addAttribute("productId", productId);

        OptionResponse optionDto = optionService.findById(optionId);
        model.addAttribute("optionDto", new OptionRequest(optionDto.name(), optionDto.quantity()));
        return "options/edit";
    }

    @PostMapping("/{productId}/options/update/{optionId}")
    public String updateProductOption(@PathVariable Long productId, @PathVariable Long optionId, @Valid @ModelAttribute OptionRequest optionDto){
        optionService.updateById(optionId, optionDto);
        return "redirect:/products/"+productId+"/options";
    }

    @GetMapping("/{productId}/options/delete/{optionId}")
    public String deleteProductOption(@PathVariable Long productId, @PathVariable Long optionId) {
        optionService.deleteById(optionId);
        return "redirect:/products/"+productId+"/options";
    }
}
