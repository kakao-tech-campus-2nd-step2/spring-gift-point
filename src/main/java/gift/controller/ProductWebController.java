package gift.controller;

import gift.domain.CategoryName;
import gift.dto.CategoryDTO;
import gift.dto.OptionDTO;
import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "웹 제품 관리", description = "웹에서 제품을 관리하는 API")
@Controller
@RequestMapping("/web/products")
public class ProductWebController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductWebController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Operation(summary = "제품 페이지 조회", description = "웹 페이지에서 모든 제품을 조회합니다.")
    @GetMapping
    public String getProductsPage(Model model) {
        List<ProductDTO> products = productService.findAllProducts();
        List<CategoryDTO> categories = categoryService.findAllCategories();
        model.addAttribute("products", products);
        model.addAttribute("categories", categories);
        return "products";
    }

    @Operation(summary = "제품 등록", description = "새로운 제품을 등록합니다.")
    @PostMapping(consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String postProduct(@RequestParam String name, @RequestParam BigDecimal price,
        @RequestParam String imageUrl, @RequestParam String description,
        @RequestParam CategoryName categoryName,@RequestParam(required = false) List<String> optionNames) {
        List<OptionDTO> options = new ArrayList<>();
        if (optionNames != null) {
            for (String optionName : optionNames) {
                options.add(new OptionDTO(optionName, null));
            }
        }
        CategoryDTO categoryDTO = categoryService.getCategoryByName(categoryName);
        ProductDTO productDTO = new ProductDTO.ProductDTOBuilder()
            .name(name)
            .price(price)
            .imageUrl(imageUrl)
            .description(description)
            .categoryName(categoryName)
            .options(options)
            .build();
        productService.createProduct(productDTO);
        return "redirect:/web/products";
    }

    @Operation(summary = "제품 삭제", description = "선택한 제품들을 삭제합니다.")
    @PostMapping(value = "/delete", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String deleteProduct(@RequestParam List<Long> productIds) {
        for (Long id : productIds) {
            productService.deleteProduct(id);
        }
        return "redirect:/web/products";
    }

    @Operation(summary = "제품 수정 폼 조회", description = "특정 제품을 수정하기 위한 폼을 조회합니다.")
    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        List<CategoryDTO> categories = categoryService.findAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("categories", categories);
        return "productEdit";
    }

    @Operation(summary = "제품 수정", description = "특정 제품을 수정합니다.")
    @PostMapping(value = "/edit/{id}", consumes = "application/x-www-form-urlencoded;charset=UTF-8")
    public String editProduct(@PathVariable Long id, @RequestParam String name,
        @RequestParam BigDecimal price, @RequestParam String imageUrl,
        @RequestParam CategoryName categoryName) {
        CategoryDTO categoryDTO = categoryService.getCategoryByName(categoryName);
        ProductDTO productDTO = new ProductDTO.ProductDTOBuilder()
            .id(id)
            .name(name)
            .price(price)
            .imageUrl(imageUrl)
            .categoryName(categoryName)
            .options(new ArrayList<>())
            .build();
        productService.updateProduct(id, productDTO);
        return "redirect:/web/products";
    }
}
