package gift.Controller;

import gift.Model.ProductDto;
import gift.Service.CategoryService;
import gift.Service.ProductService;

import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Tag(name = "Product", description = "상품 관련 api")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/api/products")
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    public String getAllProductsByRoot(Model model,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> paging = productService.getAllProductsByPage(pageable);
        model.addAttribute("paging", paging);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paging.getTotalPages());
        return "products";
    }

    @GetMapping("/products")
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회합니다.")
    public String getAllProductsByUser(Model model,
                                       @RequestParam(value = "page", defaultValue = "0") int page,
                                       @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> paging = productService.getAllProductsByPage(pageable);
        model.addAttribute("paging", paging);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", paging.getTotalPages());
        return "user_products";
    }

    @RequestMapping(value = "/api/products/create", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "상품 추가", description = "상품 추가 화면을 보여주고 상품을 추가합니다.")
    public String createProduct(@Valid @ModelAttribute ProductDto productDto, HttpServletRequest request, Model model) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("product", new ProductDto());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product_form";
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            model.addAttribute("product", productDto);
            productService.saveProduct(productDto);
            return "redirect:/api/products";
        }
        return "error";
    }

    @RequestMapping(value = "/api/products/update/{id}", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "상품 수정", description = "상품 수정 화면을 보여주고 상품을 수정합니다.")
    public String updateProductById(@PathVariable Long id, @Valid @ModelAttribute ProductDto productDtoDetails, HttpServletRequest request, Model model) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            Optional<ProductDto> optionalProduct = productService.getProductById(id);
            model.addAttribute("product", optionalProduct.get());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "product_form";
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            productService.updateProduct(productDtoDetails);
            return "redirect:/api/products";
        }
        return "error";
    }

    @PostMapping("/api/products/delete/{id}")
    @Operation(summary = "상품 삭제", description = "상품을 삭제합니다.")
    public String deleteProduct(@PathVariable Long id, Model model) {
        Optional<ProductDto> optionalProduct = productService.getProductById(id);
        model.addAttribute("product", optionalProduct.get());
        productService.deleteProduct(id);
        return "redirect:/api/products";  // 제품 목록 페이지로 리디렉션
    }

}
