package gift.controller.adminController;

import gift.dto.*;
import gift.service.CategoryService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminProductController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/products")
    public String itemList(Model model,
                           @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                           @RequestParam(value = "categoryId", required = false, defaultValue = "1") Long categoryId) {
        Page<ProductResponseDto> products = productService.findAll(pageable, categoryId);
        model.addAttribute("products", products);
        return "items";
    }

    @GetMapping("/products/add")
    public String getAddForm(Model model, Pageable pageable) {
        Page<CategoryResponseDto> list = categoryService.getAll(pageable);
        model.addAttribute("requestDto", new ViewProductDto());
        model.addAttribute("list", list);
        return "addForm";
    }

    @PostMapping("products/add")
    public String add(@Valid @ModelAttribute("requestDto") ViewProductDto requestDto,
                      BindingResult result, Model model, Pageable pageable) {
        if (result.hasErrors()) {
            Page<CategoryResponseDto> list = categoryService.getAll(pageable);
            model.addAttribute("list", list);
            return "addForm";
        }
        ProductRequestDto request = new ProductRequestDto(requestDto.getName(), requestDto.getImageUrl(), requestDto.getPrice(), requestDto.getCategory(), requestDto.getOptions());
        productService.addProduct(request);
        return "redirect:/";
    }

    @GetMapping("/products/edit/{id}")
    public String getEditForm(
            @PathVariable("id") Long id, Model model) {
        ProductResponseDto product = productService.findProduct(id);
        model.addAttribute("requestDto", product);
        model.addAttribute("id", id);
        return "editForm";
    }

    @PutMapping("/products/edit/{id}")
    public String editProduct(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("requestDto") ProductChangeRequestDto requestDto,
            BindingResult result) {
        if (result.hasErrors()) {
            return "editForm";
        }
        productService.editProduct(id, requestDto);
        return "redirect:/";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
