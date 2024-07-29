package gift.controller;

import gift.domain.Category;
import gift.domain.Product;
import gift.dto.CategoryResponseDto;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.dto.ProductRequestDto;
import gift.exception.ProductNotFoundException;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    @Autowired
    public ProductController(ProductService productService,CategoryService categoryService,OptionService optionService){
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    // 목록 페이지
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        return "products";
    }

    // id 클릭 시 상품 상세보기
    @GetMapping("/{id}")
    public String findProductById(@PathVariable Long id,Model model){
        Product product = productService.findById(id);
        model.addAttribute("productDto",new ProductRequestDto(product.getId(),product.getName(),
            product.getPrice(), product.getImageUrl(),product.getCategory().getId()));
        Category category = categoryService.findById(product.getCategory().getId());
        model.addAttribute("category",category);
        return "product";
    }

    // 수정
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productService.findById(id);
        model.addAttribute("productDto",new ProductRequestDto(product.getId(),product.getName(),
            product.getPrice(), product.getImageUrl(),product.getCategory().getId()));
        List<CategoryResponseDto> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        return "editForm";
    }

    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id,@Valid @ModelAttribute ProductRequestDto productRequestDto)
    {
        productService.updateProduct(id, productRequestDto);
        return "redirect:/products";
    }

    // 추가
    @GetMapping("/add")
    public String addForm(Model model){
        model.addAttribute("productDto",new ProductRequestDto(null,null,0,null,null));
        List<CategoryResponseDto> categories = categoryService.findAll();
        model.addAttribute("categories",categories);
        return "addForm";
    }

    @PostMapping("/add")
    public String addProduct(@Valid @ModelAttribute ProductRequestDto productRequestDto){
        productService.save(productRequestDto);
        return "redirect:/products";
    }

    // 삭제
    @GetMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id){

        productService.deleteById(id);
        return "redirect:/products";
    }
}

