package gift.controller;


import gift.model.option.OptionRequest;
import gift.model.product.Product;
import gift.model.product.ProductRequest;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 모든 상품 조회
    @GetMapping
    public String getAllProducts(Model model,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "id,asc") String[] sort) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));
        model.addAttribute("products", productService.getAllProducts(pageable));
        model.addAttribute("page", page);
        model.addAttribute("pagesize", size);
        model.addAttribute("page", page);
        model.addAttribute("sort", sort);
        return "products";
    }

    // 상품 추가하는 창
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "product-form";
    }

    // 상품 추가 후 홈으로 이동
    @PostMapping("/add")
    public String addProduct(@ModelAttribute @Valid ProductRequest product, @ModelAttribute
        OptionRequest option) {
        productService.createProduct(product, option);
        return "redirect:/products";
    }

    // 등록된 상품 수정하는 창
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        return "product-form";
    }

    // 등록된 상품을 수정하는 기능
    @PutMapping("/edit/{id}")
    public String editProduct(@PathVariable("id") Long id, @ModelAttribute @Valid ProductRequest product) {
        productService.updateProduct(id, product);
        return "redirect:/products";
    }

    // 등록된 상품을 삭제하는 기능
    @DeleteMapping ("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

}
