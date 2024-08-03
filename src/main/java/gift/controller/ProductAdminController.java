package gift.controller;

import gift.dto.ProductDTO;
import gift.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {

    private final ProductService productService;

    public ProductAdminController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * 모든 제품 목록 조회.
     *
     * @param model 모델
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return 제품 목록 페이지
     */
    @GetMapping
    public String listProducts(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
        Page<ProductDTO> productsPage = productService.getAllProducts(page, size);
        model.addAttribute("productsPage", productsPage);
        return "productList";
    }

    /**
     * 제품 추가 폼 표시.
     *
     * @param model 모델
     * @return 제품 폼 페이지
     */
    @GetMapping("/add")
    public String addProductForm(Model model) {
        model.addAttribute("product", new ProductDTO());
        return "productForm";
    }

    /**
     * 새로운 제품 추가.
     *
     * @param productDTO 제품 DTO
     * @return 리디렉션 URL
     */
    @PostMapping("/add")
    public String addProduct(@ModelAttribute ProductDTO productDTO) {
        productService.saveProduct(productDTO);
        return "redirect:/admin/products";
    }

    /**
     * 제품 수정 폼 표시.
     *
     * @param id 제품 ID
     * @param model 모델
     * @return 제품 폼 페이지
     */
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        ProductDTO productDTO = productService.getProductById(id);
        model.addAttribute("product", productDTO);
        return "productForm";
    }

    /**
     * 기존 제품 수정.
     *
     * @param id 제품 ID
     * @param productDTO 제품 DTO
     * @return 리디렉션 URL
     */
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute ProductDTO productDTO) {
        productService.updateProduct(id, productDTO);
        return "redirect:/admin/products";
    }

    /**
     * 제품 삭제.
     *
     * @param id 제품 ID
     * @return 리디렉션 URL
     */
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}