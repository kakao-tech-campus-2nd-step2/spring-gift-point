package gift.controller;

import gift.dto.CategoryRequestDto;
import gift.dto.ProductRequestDto;
import gift.dto.OptionRequestDto;
import gift.dto.ProductUpdateRequestDto;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class AdminController {
    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public AdminController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    // 전체 상품목록
    @GetMapping
    public String viewProducts(Model model) {
        model.addAttribute("products", productService.findAll());
        return "products-list";
    }

    // 상품 추가폼 끌어오기
    @GetMapping("/add")
    public String addProductsForm(Model model) {
        Product product = new Product();
        product.addOption(new Option()); // 기본 옵션 추가
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        return "addProducts-form";
    }

    // 상품추가 Post
    @PostMapping("/add")
    public String addProduct(Model model, @Valid @ModelAttribute Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "addProducts-form";
        }

        List<OptionRequestDto> optionRequestDtos = product.getOptions().stream()
            .map(option -> new OptionRequestDto(option.getName(), option.getQuantity()))
            .collect(Collectors.toList());

        ProductRequestDto productRequestDto = new ProductRequestDto(
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId(),
            optionRequestDtos
        );

        productService.addProduct(productRequestDto);
        return "redirect:/products";
    }

    // 상품업데이트
    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Optional<Product> product = productService.findById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("categories", categoryService.findAll());
            return "updateProducts-form";
        }
        return "redirect:/products";
    }

    @PutMapping("/update/{id}")
    public String updateProduct(@PathVariable("id") Long id, Model model, @Valid @ModelAttribute Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            return "updateProducts-form";
        }


        ProductUpdateRequestDto productUpdateRequestDto = new ProductUpdateRequestDto(
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getId()
        );

        productService.updateProduct(id, productUpdateRequestDto);
        return "redirect:/products";
    }

    // 상품 삭제
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    // 카테고리 추가 폼
    @GetMapping("/categories/add")
    public String addCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        return "addCategory-form";
    }

    // 카테고리 추가 Post
    @PostMapping("/categories/add")
    public String addCategory(Model model, @Valid @ModelAttribute Category category, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addCategory-form";
        }
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto(category.getName(),category.getColor(),category.getImageUrl(),category.getDescription());
        categoryService.save(category);
        return "redirect:/products";
    }

    // 옵션 추가 폼
    @GetMapping("/{productId}/options/add")
    public String addOptionForm(@PathVariable Long productId, Model model) {
        model.addAttribute("option", new Option());
        model.addAttribute("productId", productId);
        return "addOption-form";
    }

    // 옵션 추가 Post
    @PostMapping("/{productId}/options/add")
    public String addOption(@PathVariable Long productId, Model model, @Valid @ModelAttribute Option option, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addOption-form";
        }
        option.setProduct(productService.findById(productId).orElseThrow());
        optionService.save(option);
        return "redirect:/products";
    }

    // 옵션 수정 폼
    @GetMapping("/{productId}/options/update/{optionId}")
    public String updateOptionForm(@PathVariable Long productId, @PathVariable Long optionId, Model model) {
        Option option = optionService.findById(optionId).orElseThrow();
        model.addAttribute("option", option);
        model.addAttribute("productId", productId);
        return "updateOption-form";
    }

    // 옵션 수정 Post
    @PutMapping("/{productId}/options/update/{optionId}")
    public String updateOption(@PathVariable Long productId, @PathVariable Long optionId, Model model, @Valid @ModelAttribute Option option, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "updateOption-form";
        }
        optionService.save(option);
        return "redirect:/products";
    }

    // 옵션 삭제
    @GetMapping("/{productId}/options/delete/{optionId}")
    public String deleteOption(@PathVariable Long productId, @PathVariable Long optionId, Model model) {
        optionService.deleteById(optionId);
        return "redirect:/products";
    }
}
