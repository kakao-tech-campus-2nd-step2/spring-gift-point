package gift.controller;

import gift.domain.Category;
import gift.domain.Category.CategoryResponse;
import gift.domain.Product.ProductRequest;
import gift.domain.Product.ProductResponse;
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
@RequestMapping("/admin/products")
@Tag(name = "Admin Product", description = "관리자 화면 상품 API")
public class AdminController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, CategoryService categoryService) {
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
        Page<ProductResponse> productsPage = productService.getAllProducts(pageable);

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
        List<CategoryResponse> responses = categoryService.getAllCategories();
        model.addAttribute("categories", responses);
        return "form";
    }

    //상품 추가 데이터 응답
    @PostMapping
    @Operation(summary = "상품 추가", description = "상품 추가 폼에서 입력한 상품을 추가합니다.")
    public String create(@Valid @ModelAttribute ProductRequest request) {
        productService.addProduct(request);
        return "redirect:/admin/products";
    }

    //상품 단일 조회 기능
    @GetMapping("/{id}")
    @Operation(summary = "단일 상품 조회", description = "단일 상품을 출력합니다.")
    public String showOneProduct(@PathVariable("id") Long id, Model model) {
        List<ProductResponse> responses = new ArrayList<>();
        responses.add(productService.getProductById(id));
        model.addAttribute("products", responses);
        return "products_list";
    }

    //상품 검색 기능
    @GetMapping("/search")
    @Operation(summary = "상품 이름 검색", description = "검색어를 통해 해당 검색어를 포함한 이름의 상품들을 출력합니다.")
    public String searchProduct(@Valid @ModelAttribute ProductRequest request, Model model) {
        List<ProductResponse> responses = productService.searchProduct(request.name());
        model.addAttribute("products", responses);
        return "products_list";
    }

    //상품 삭제 기능
    @DeleteMapping("/{id}")
    @Operation(summary = "상품 제거", description = "해당 상품 제거")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }

    //상품 수정 폼 페이지
    @GetMapping("/update/{id}")
    @Operation(summary = "상품 수정 폼", description = "상품 수정 폼 화면을 띄웁니다.")
    public String updateProductForm(@PathVariable("id") Long id, Model model) {
        ProductResponse response = productService.getProductById(id);
        model.addAttribute("product", response);

        List<CategoryResponse> responses = categoryService.getAllCategories();
        model.addAttribute("categories", responses);
        return "form";
    }

    //상품 수정 기능
    @PutMapping("/{id}")
    @Operation(summary = "상품 수정", description = "상품 수정 폼에서 입력한 상품을 수정합니다.")
    public String updateProduct(@PathVariable("id") Long id,
        @Valid @ModelAttribute ProductRequest request) {
        productService.updateProduct(id, request);
        return "redirect:/admin/products/" + id;
    }

}
