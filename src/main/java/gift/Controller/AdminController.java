package gift.Controller;

import gift.Model.DTO.ProductDTO;

import java.util.List;

import gift.Service.ProductService;
import gift.Valid.NameValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminController {
    private NameValidator nameValidator;
    private final ProductService productService;
    private String email = "admin";

    public AdminController(ProductService productService){
        this.productService = productService;
    }

    @Autowired
    public void setNameValidator(NameValidator nameValidator){this.nameValidator = nameValidator;}

    @InitBinder("product")
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(nameValidator);
    }

    @GetMapping
    public String getAllProducts(@RequestParam(value = "page", defaultValue = "0") int page, Model model){
        Page<ProductDTO> products = productService.getPage(email, page);
        model.addAttribute("products", products);
        return "products";
    }

    @GetMapping("/add")
    public String addProductForm(Model model){
        List<String> categories = productService.getCategoriesName();
        model.addAttribute("product", new ProductDTO(0L, "" ,"a",0,"b"));
        model.addAttribute("categories", categories);
        return "add";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()) {
            return "add";
        }
        productService.create(email, productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/edit/{id}")
    public String updateProductForm(@PathVariable("id") Long id, Model model){
        ProductDTO productDTO = productService.getById(email, id);
        List<String> categories = productService.getCategoriesName();

        model.addAttribute("product", productDTO);
        model.addAttribute("categories", categories);
        return "edit";
    }

    @PostMapping("edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @ModelAttribute("product") @Valid ProductDTO productDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()) {
            return "edit";
        }
        productService.update(email, id, productDTO);
        return "redirect:/admin/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id){
        productService.delete(email, id);
        return "redirect:/admin/products";
    }
}
