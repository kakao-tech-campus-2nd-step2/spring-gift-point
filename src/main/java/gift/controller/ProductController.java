package gift.controller;

import gift.domain.Category;
import gift.domain.Product;
import gift.error.NotFoundException;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/products")
@Tag(name = "Product", description = "상품 API")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    //상품 전체 조회 페이지
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "전체 상품 조회", description = "상품 리스트를 페이지네이션 방식으로 출력합니다.")
    public String showProductList(
        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
        Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productsPage = productService.getAllProducts(pageable);

        model.addAttribute("products", productsPage.getContent());
        model.addAttribute("currentPage", productsPage.getNumber());
        model.addAttribute("totalPages", productsPage.getTotalPages());
        model.addAttribute("totalItems", productsPage.getTotalElements());
        model.addAttribute("pageSize", productsPage.getSize());

        return "products_list";
    }

    //상품 추가 폼 페이지
    @GetMapping("/new")
    @Operation(summary = "상품 추가 폼", description = "상품 추가 폼 화면을 띄웁니다.")
    public String createProductForm(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "form";
    }

    //상품 추가 데이터 응답
    @PostMapping
    @Operation(summary = "상품 추가", description = "상품 추가 폼에서 입력한 상품을 추가합니다.")
    public String create(@Valid @ModelAttribute Product formProduct) {
        productService.addProduct(formProduct);
        return "redirect:/products";
    }

    //상품 단일 조회 기능
    @GetMapping("/{id}")
    @Operation(summary = "단일 상품 조회", description = "단일 상품을 출력합니다.")
    public String showOneProduct(@PathVariable("id") Long id, Model model) {
        List<Product> products = new ArrayList<>();
        products.add(productService.getProductById(id));
        model.addAttribute("products", products);
        return "products_list";
    }

    //상품 검색 기능
    @GetMapping("/search")
    @Operation(summary = "상품 이름 검색", description = "검색어를 통해 해당 검색어를 포함한 이름의 상품들을 출력합니다.")
    public String searchProduct(@Valid @ModelAttribute Product formProduct, Model model) {
        List<Product> products = productService.searchProduct(formProduct.getName());
        model.addAttribute("products", products);
        return "products_list";
    }

    //상품 삭제 기능
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 제거", description = "해당 상품 제거")
    public String deleteProduct(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        productService.deleteProduct(id);
        return "redirect:/products";
    }

    //상품 수정 폼 페이지
    @GetMapping("/update/{id}")
    @Operation(summary = "상품 수정 폼", description = "상품 수정 폼 화면을 띄웁니다.")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "form";
    }

    //상품 수정 기능
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품 수정 폼에서 입력한 상품을 수정합니다.")
    public String updateProduct(@PathVariable("id") Long id,
        @Valid @ModelAttribute Product updateProduct) {
        productService.updateProduct(id, updateProduct);
        return "redirect:/products/" + id;
    }

}
