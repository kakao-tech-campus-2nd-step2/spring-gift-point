package gift.Controller;

import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Product;
import gift.Service.AdminService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AdminController {

    private final AdminService adminService;
    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }

    @GetMapping("/admin/products")
    public String getProducts(Model model, Pageable pageable) {
        Page<Product> productPage = adminService.findAll(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "index";
    }

    @GetMapping("/admin/products/add")
    public String newProductForm(Model model) {
        model.addAttribute("product", new ProductDTO(0L,"",0,"",new Category(0L,"","","","",null),null));
        model.addAttribute("categories", adminService.getAllCategory());
        return "post";
    }

    @PostMapping("/admin/products")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        adminService.addProduct(productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/update/{productId}")
    public String editProductForm(@PathVariable(value = "productId") Long productId, Model model) {
        Product product = adminService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", adminService.getAllCategory());
        return "update";
    }

    @PostMapping("/admin/products/update/{productId}")
    public String updateProduct(@PathVariable(value = "productId") Long productId, @Valid @ModelAttribute ProductDTO newProductDTO) {
        adminService.updateProduct(newProductDTO);
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") Long productId) {
        adminService.deleteProduct(productId);
        return "redirect:/admin/products";
    }
}


