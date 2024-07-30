package gift.controller;


import gift.dto.request.ProductRequestDTO;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AdminController {

    private final ProductService productService;

    @Autowired
    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    /*@GetMapping("/admin")
    public String mainRendering(){
        return "main";
    }

    @GetMapping("/admin/post")
    public String adminAddPage(){
        return "add";
    }

    @GetMapping("/admin/delete")
    public String adminDeletePage(){
        return "delete";
    }

    @GetMapping("/admin/put")
    public String adminUpdatePage(){
        return "update";
    }

    @GetMapping("/admin/get")
    public String adminGetPage(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "get";
    }

    @PostMapping("/admin/post/submit")
    public String submitPostProduct(@ModelAttribute @Valid ProductRequestDTO productRequestDTO, Model model) {
        productService.saveProduct(productRequestDTO);
        return adminGetPage(model);
    }

    @PostMapping("/admin/delete/submit")
    public String submitDeleteProduct(@RequestParam("productId") Long productId, Model model){
        productService.deleteProduct(productId);
        return adminGetPage(model);
    }

    @PostMapping("/admin/put/submit")
    public String submitUpdateProduct(@RequestParam("productId") Long productId,
                                      @ModelAttribute @Valid ProductRequestDTO productRequestDTO,
                                      Model model) {
        productService.updateProduct(productId, productRequestDTO);
        return adminGetPage(model);
    }*/

}
