package gift.controller;

import gift.dto.KakaoTokenDto;
import gift.dto.ProductDto;
import gift.entity.Category;
import gift.entity.Product;
import gift.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 관련 API")
public class ProductController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private OptionService optionService;
    private WishlistService wishlistService;
    private final KakaoTokenService kakaoTokenService;
    private final KakaoService kakaoService;

    @Autowired
    public ProductController(CategoryService categoryService, ProductService productService, OptionService optionService, WishlistService wishlistService, KakaoService kakaoService, KakaoTokenService kakaoTokenService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.kakaoService = kakaoService;
        this.kakaoTokenService = kakaoTokenService;
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
        return "redirect:/view/products";
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
            List<Category> categories = categoryService.getAllCategories();
            model.addAttribute("categories", categories);
            return "edit-product";
        }
        productService.updateProduct(id, productDto);
        return "redirect:/view/products";
    }

}
