package gift.controller;

import gift.model.Option;
import gift.model.OptionDTO;
import gift.model.ProductDTO;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/product")
public class AdminController {

    private final ProductService productService;
    private final OptionService optionService;

    public AdminController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping("/list")
    public String productList(Model model) {
        List<ProductDTO> products = productService.getAllProductByList();
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("{productId}/option/list")
    public String optionList(Model model,
        @PathVariable long productId, Pageable pageable) {
        List<OptionDTO> options = productService.getOptionsByProductId(productId, pageable);
        model.addAttribute("productId", productId);
        model.addAttribute("options", options);
        return "option-list";
    }

    @GetMapping("/add")
    public ModelAndView showAddPage() {
        ModelAndView modelAndView = new ModelAndView("product-add");
        modelAndView.addObject("product", new ProductDTO(0L, "", 0L, "", 0L));
        return modelAndView;
    }

    @GetMapping("/{productId}/option/add")
    public String showAddOptionPage(@PathVariable long productId, Model model) {
        model.addAttribute("productId", productId);
        model.addAttribute("option", new OptionDTO(0L, "", 0L));
        return "option-add";
    }

    @GetMapping("/edit/{id}")
    public ModelAndView showEditPage(@PathVariable Long id) {
        ModelAndView modelAndView = new ModelAndView("product-edit");
        ProductDTO productDTO = productService.getProductById(id);
        modelAndView.addObject("product", productDTO);
        return modelAndView;
    }

    @GetMapping("{productId}/option/edit/{optionId}")
    public ModelAndView showOptionEditPage(@PathVariable Long productId, @PathVariable long optionId) {
        ModelAndView modelAndView = new ModelAndView("option-edit");
        OptionDTO optionDTO = optionService.getOption(optionId);
        modelAndView.addObject("option", optionDTO);
        modelAndView.addObject("productId", productId);
        modelAndView.addObject("optionId", optionId);
        return modelAndView;
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        productService.createProduct(productDTO);
        return "redirect:/admin/product/list";
    }

    @PostMapping("/{productId}/option/add")
    public String addOption(@Valid @ModelAttribute OptionDTO optionDTO,
        @PathVariable long productId) {
        optionService.createOption(productId, optionDTO);
        return "redirect:/admin/product/" + productId + "/option/list";
    }

    @PutMapping("/edit/{id}")
    public String updateProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        productService.updateProduct(productDTO);
        return "redirect:/admin/product/list";
    }

    @PutMapping("/{productId}/option/edit")
    public String updateOption(@Valid @ModelAttribute OptionDTO optionDTO,
        @PathVariable long productId) {
        optionService.updateOption(productId, optionDTO.id(), optionDTO);
        return "redirect:/admin/product/" + productId + "/option/list";
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.deleteProduct(id));
    }

    @DeleteMapping("/option/delete/{optionId}")
    public ResponseEntity<String> deleteOption(@PathVariable Long optionId) {
        optionService.deleteOption(optionId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleModelValidationExceptions(MethodArgumentNotValidException ex, Model model) {
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            model.addAttribute(fieldName + "Error", errorMessage);
        });
        return "error";
    }
}
