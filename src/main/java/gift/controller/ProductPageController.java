package gift.controller;

import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductPageController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductPageController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "상품 목록 페이지 보기")
    @GetMapping
    public String viewHomePage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String sort,
            @RequestParam(required = false) Long categoryId,
            Model model) {

        String[] sortParams = sort.split(",");
        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(sortParams.length > 1 ? sortParams[1] : "asc").orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(sortDirection, sortParams[0])));

        Page<ProductDTO> productPage = productService.getAllProducts(pageable, categoryId);
        model.addAttribute("productPage", productPage);
        model.addAttribute("selectedCategoryId", categoryId);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "index";
    }


    private List<Sort.Order> getSortOrders(String[] sort) {
        return Arrays.stream(sort)
                .map(this::createSortOrder)
                .collect(Collectors.toList());
    }

    private Sort.Order createSortOrder(String sortOrder) {
        String[] parts = sortOrder.split(",");
        if (parts.length < 2) {
            return new Sort.Order(Sort.Direction.ASC, parts[0]);
        }
        return new Sort.Order(Sort.Direction.fromString(parts[1]), parts[0]);
    }


    @Operation(summary = "새 상품 생성 폼")
    @GetMapping("/new")
    public String createProductForm(Model model) {
        model.addAttribute("product", new ProductDTO(null, "", 0, "", null, ""));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @Operation(summary = "새 상품 생성")
    @PostMapping("/save")
    public String createProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "addProduct";
        }
        productService.createProduct(productDTO);
        return "redirect:/products";
    }

    @Operation(summary = "기존 상품 수정 폼")
    @GetMapping("/update/{id}")
    public String updateProductForm(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.getProductById(id);
        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "editProduct";
    }

    @Operation(summary = "기존 상품 수정")
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAllCategories());
            return "editProduct";
        }
        productService.updateProduct(id, productDTO);
        return "redirect:/products";
    }

    @Operation(summary = "상품 삭제")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return "redirect:/products";
    }
}
