package gift.controller;

import gift.dto.pageDTO.PageRequestDTO;
import gift.dto.pageDTO.ProductPageResponseDTO;
import gift.dto.productDTO.ProductAddRequestDTO;
import gift.dto.productDTO.ProductGetResponseDTO;
import gift.dto.productDTO.ProductUpdateRequestDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public String getAllProducts(Model model, @Valid PageRequestDTO pageRequestDTO) {
        try {
            Pageable pageable = PageRequest.of(pageRequestDTO.page(), pageRequestDTO.size(), Sort.by(pageRequestDTO.sort()));
            ProductPageResponseDTO productPageResponseDTO = productService.getAllProducts(pageable);
            model.addAttribute("products", productPageResponseDTO);
            return "product_list";
        } catch (Exception e) {
            throw new InvalidInputValueException("잘못된 요청입니다.");
        }
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 폼 보기", description = "상품을 추가할 수 있는 폼으로 이동합니다.")
    public String showAddProductForm(Model model) {
        model.addAttribute("productDTO", new ProductAddRequestDTO("", "0", "", null, null));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add_product_form";
    }

    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public String addProduct(@ModelAttribute @Valid ProductAddRequestDTO productAddRequestDTO,
        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productDTO", productAddRequestDTO);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "add_product_form";
        }
        try {
            productService.addProduct(productAddRequestDTO);
            return "redirect:/admin/products";
        } catch (Exception e) {
            throw new InvalidInputValueException("잘못된 값이 입력되었습니다.");
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "상품 수정 폼 보기", description = "상품을 수정할 수 있는 폼으로 이동합니다.")
    public String showUpdateProductForm(@PathVariable("id") long id, Model model) {
        ProductGetResponseDTO product = productService.getProductById(id);
        model.addAttribute("productDTO", product);
        model.addAttribute("productID", id);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "edit_product_form";
    }

    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    public String updateProduct(@PathVariable("id") long id,
        @ModelAttribute @Valid ProductUpdateRequestDTO productUpdateRequestDTO,
        BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productID", id);
            model.addAttribute("categories", categoryService.getAllCategories());
            return "edit_product_form";
        }
        try {
            productService.updateProduct(productUpdateRequestDTO, id);
            return "redirect:/admin/products";
        } catch (Exception e) {
            throw new InvalidInputValueException("잘못된 값이 입력되었습니다.");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public String deleteProduct(@PathVariable("id") long id) {
        try {
            productService.deleteProductAndWishlistAndOptions(id);
            return "redirect:/admin/products";
        } catch (Exception e) {
            throw new NotFoundException("존재하지 않는 상품입니다.");
        }
    }

}