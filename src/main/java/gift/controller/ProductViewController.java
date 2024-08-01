package gift.controller;

import gift.service.CategoryService;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/products")
public class ProductViewController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductViewController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    /**
     * 상품을 추가하는 페이지인 addForm.html 반환
     *
     * @return addForm.html
     */
    @GetMapping("/addForm")
    public String addProductForm(Model model) {
        model.addAttribute("categories", categoryService.getCategoryList());
        return "addForm";
    }

    /**
     * 상품 정보를 수정하는 페이지인 editForm.html 반환 <br> 해당 상품이 없으면 NoSuchElementException
     *
     * @param id 수정할 상품의 id
     * @return editForm.html
     */
    @GetMapping("/editForm/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        model.addAttribute("categories", categoryService.getCategoryList());
        return "editForm";
    }
}
