package gift.controller.web;

import gift.dto.ProductDTO;
import gift.exception.ErrorCode;
import gift.exception.InvalidProductNameException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/web/products")
@Tag(name = "Product Web API", description = "웹 상품 관련 API")
public class ProductWebController {

    private final ProductService productService;

    @Autowired
    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/list")
    @Operation(summary = "모든 상품 조회", description = "모든 상품을 조회하여 목록을 반환합니다.")
    public String getAllProducts(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "id") String sort,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false) Integer categoryId) {

        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductDTO> productPage;

        if (categoryId != null) {
            productPage = productService.getProductsByCategoryId(pageable, categoryId);
        } else {
            productPage = productService.getProducts(pageable);
        }

        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
        model.addAttribute("categoryId", categoryId);

        return "productList";
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "상품 ID로 조회", description = "지정된 상품 ID에 해당하는 상품을 조회합니다.")
    public String getProductById(@PathVariable("id") Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "productDetail";
    }

    @GetMapping("/add")
    @Operation(summary = "상품 추가 폼", description = "새로운 상품을 추가하기 위한 폼을 반환합니다.")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "addProduct";
    }

    @PostMapping("/add")
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public String addProduct(@Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addProduct";
        }
        validateProductName(productDTO.getName());
        productService.saveProduct(productDTO);
        return "redirect:/web/products/list";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "상품 수정 폼", description = "지정된 상품을 수정하기 위한 폼을 반환합니다.")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        ProductDTO product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit/{id}")
    @Operation(summary = "상품 수정", description = "지정된 상품 ID에 해당하는 상품을 수정합니다.")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute("product") ProductDTO productDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "editProduct";
        }
        validateProductName(productDTO.getName());
        productService.updateProduct(id, productDTO);
        return "redirect:/web/products/list";
    }

    @GetMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "지정된 상품 ID에 해당하는 상품을 삭제합니다.")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/web/products/list";
    }

    private void validateProductName(String name) {
        if (name.length() > 20) {
            throw new InvalidProductNameException(ErrorCode.INVALID_NAME_LENGTH);
        }
        if (!Pattern.matches("[a-zA-Z0-9가-힣()\\[\\]+\\-&/_ ]*", name)) {
            throw new InvalidProductNameException(ErrorCode.INVALID_CHARACTERS);
        }
        if (name.contains("카카오")) {
            throw new InvalidProductNameException(ErrorCode.CONTAINS_KAKAO);
        }
    }
}
