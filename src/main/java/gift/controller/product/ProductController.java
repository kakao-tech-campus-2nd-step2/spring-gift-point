package gift.controller.product;

import gift.dto.request.OptionRequestDto;
import gift.dto.request.ProductRequestDto;
import gift.dto.response.CategoryResponseDto;
import gift.dto.response.OptionResponseDto;
import gift.dto.response.ProductResponseDto;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping()
    public String getAll(Model model,
                         @PageableDefault(size = 5, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        List<ProductResponseDto> productDtos = productService.findProducts(pageable);
        model.addAttribute("productDtos", productDtos);
        return "manager";
    }

    @GetMapping("/{id}/options")
    public String getProductOptions(@PathVariable("id") Long productId,
                                    Model model){
        List<OptionResponseDto> optionDtos = optionService.findOptionsByProduct(productId);
        model.addAttribute("options", optionDtos);
        model.addAttribute("productId",productId);
        return "option/optionForm";
    }

    @PostMapping("/{id}/options")
    public String addProductOption(@PathVariable("id") Long productId,
                                   @ModelAttribute @Valid OptionRequestDto optionRequestDto){
        optionService.saveOption(optionRequestDto, productId);
        return "redirect:/products/{id}/options";
    }

    @PostMapping("/{product_id}/delete/{option_id}")
    public String deleteOption(@PathVariable("product_id") Long productId,
                               @PathVariable("option_id") Long optionId){
        optionService.deleteOneOption(productId, optionId);
        return "redirect:/products";
    }

    @GetMapping("/new/{id}/options")
    public String addOptionForm(@PathVariable("id") Long productId,
                                Model model){
        model.addAttribute("productId", productId);
        return "option/addOptionForm";
    }

    @GetMapping("/new")
    public String addForm(Model model){
        List<CategoryResponseDto> categories = categoryService.findAllCategories();
        model.addAttribute("categories", categories);
        return "addForm";
    }

    @PostMapping("/new")
    public String add(@ModelAttribute @Valid ProductRequestDto productDto,
                      @ModelAttribute @Valid OptionRequestDto optionRequestDto){
        productService.addProduct(productDto, optionRequestDto);
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable("id") Long id,
                           Model model){
        ProductResponseDto productDto = productService.findProductById(id);
        List<CategoryResponseDto> categories = categoryService.findAllCategories();

        model.addAttribute("productDto", productDto);
        model.addAttribute("categories", categories);

        return "editForm";
    }

    @PostMapping("/{id}/edit")
    public String update(@PathVariable("id") Long id,
                         @ModelAttribute @Valid ProductRequestDto productDto){
        productService.updateProduct(id, productDto);
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return "redirect:/products";
    }

}
