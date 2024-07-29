package gift.controller;

import gift.dto.PageRequestDTO;
import gift.dto.InputProductDTO;
import gift.dto.ProductDTO;
import gift.dto.UpdateProductDTO;
import gift.service.ProductService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@Validated
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String getAllProducts(@RequestParam(defaultValue = "0") @Min(0) @Max(10000) int page,
                                 @RequestParam(defaultValue = "id") String sortBy,
                                 @RequestParam(defaultValue = "asc") String sortOrder, Model model) {
        PageRequestDTO pageRequestDTO = new PageRequestDTO(page, sortBy, sortOrder);
        Page<ProductDTO> productPage = productService.getAllProducts(pageRequestDTO);

        model.addAttribute("productList", productPage.getContent());

        int previousPage = productService.getPreviousPage(productPage);
        model.addAttribute("previousPage", previousPage);

        int nextPage = productService.getNextPage(productPage);
        model.addAttribute("nextPage", nextPage);

        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortOrder", sortOrder);

        return "index";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ProductDTO getProductById(@PathVariable Long id) {
        return productService.getProductDTOById(id);
    }

    @PostMapping
    public String postProduct(@ModelAttribute InputProductDTO inputProductDTO, Model model) {
        try {
            productService.saveProduct(inputProductDTO);
            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute UpdateProductDTO updateProductDTO, Model model) {
        try{
            productService.updateProduct(id, updateProductDTO);
            return "redirect:/products";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }
}