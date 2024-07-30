package gift.controller;

import gift.dto.CategoryDTO;
import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {
    private final ProductService productService;
    private final CategoryService categoryService;

    public IndexController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/products";
    }

    @GetMapping("/postform")
    public String postform(Model model){
        List<String> categoryList = getCateogoryList();
        model.addAttribute("categoryList", categoryList);
        return "postform";
    }

    @PostMapping("/editform/{id}")
    public String editform(@PathVariable Long id, Model model){
        ProductDTO product = productService.getProductDTOById(id);
        model.addAttribute("product", product);
        List<String> categoryList = getCateogoryList();
        model.addAttribute("categoryList", categoryList);
        return "editform";
    }

    private List<String> getCateogoryList(){
        List<CategoryDTO> categories = categoryService.getAllCategories();
        List<String> GetCategoryList = categories.stream()
                .map(CategoryDTO::getName)
                .toList();
        List<String> categoryList = new ArrayList<>();
        categoryList.addAll(GetCategoryList);
        categoryList.remove("NONE");
        return categoryList;
    }
}
