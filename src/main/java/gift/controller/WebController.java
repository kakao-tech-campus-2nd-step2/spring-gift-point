package gift.controller;

import gift.dto.CategoryResponseDto;
import gift.dto.ProductChangeRequestDto;
import gift.dto.ProductResponseDto;
import gift.dto.ViewProductDto;
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
public class WebController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public WebController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String itemList(Model model,
                           @PageableDefault(sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                           @RequestParam(value = "categoryId", required = false, defaultValue = "1") Long categoryId) {
        Page<ProductResponseDto> products = productService.findAll(pageable, categoryId);
        model.addAttribute("products", products);
        return "items";
    }

    @GetMapping("products/add")
    public String getAddForm(Model model, Pageable pageable) {
        Page<CategoryResponseDto> list = categoryService.getAll(pageable);
        model.addAttribute("requestDto", new ViewProductDto());
        model.addAttribute("list", list);
        return "addForm";
    }

    @GetMapping("products/edit/{id}")
    public String getEditForm(
            @PathVariable("id") Long id, Model model) {
        ProductResponseDto product = productService.findProduct(id);
        model.addAttribute("requestDto", product);
        model.addAttribute("id", id);
        return "editForm";
    }

    @PutMapping("products/edit/{id}")
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

    @DeleteMapping("products/delete/{id}")
    public String deleteProduct(
            @PathVariable("id") Long id
    ) {
        productService.deleteProduct(id);
        return "redirect:/";
    }
}
