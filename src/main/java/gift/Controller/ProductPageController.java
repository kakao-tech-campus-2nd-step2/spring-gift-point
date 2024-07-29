package gift.Controller;

import gift.DTO.RequestProductPostDTO;
import gift.Service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.ui.Model;
import gift.Model.Entity.Product;
import gift.DTO.RequestProductDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api")
public class ProductPageController {

    private final ProductService productService;


    public ProductPageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String getProduct(@PageableDefault(size = 3, sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model) {
        Page<Product> productPage = productService.getAllProducts(pageable);
        String sortField = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().toString().equalsIgnoreCase("ASC") ? "asc" : "desc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "products";
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new RequestProductPostDTO("이름을 입력해주세요", 1, "url을 입력해주세요", 1L, "옵션 이름을 입력해주세요",1));
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(@Valid @ModelAttribute RequestProductPostDTO requestProductPostDTO) {
        productService.addProduct(requestProductPostDTO);
        return "redirect:/api/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.selectProduct(id);
        model.addAttribute("product", RequestProductDTO.of(product));
        model.addAttribute("id", id);
        return "edit-product";
    }

    @PutMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute RequestProductDTO requestProductDTO) {
        productService.editProduct(id, requestProductDTO);
        return "redirect:/api/products";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/products";
    }

}