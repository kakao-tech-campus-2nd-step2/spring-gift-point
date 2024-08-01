package gift.controller;

import gift.dto.PaginationInfo;
import gift.dto.ProductDto;
import gift.dto.ProductResponse;
import gift.entity.Product;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import gift.service.WishlistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/view")
@Tag(name = "View API", description = "뷰 컨트롤러")
public class HomeController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;
    private final WishlistService wishlistService;

    public HomeController(ProductService productService, CategoryService categoryService, OptionService optionService, WishlistService wishlistService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
        this.wishlistService= wishlistService;
    }

    @GetMapping("/home")
    @Operation(summary = "홈 페이지", description = "홈 페이지를 반환합니다.")
    public String showHomeForm() {
        return "home";
    }

    @GetMapping("/products")
    @Operation(summary = "제품 페이지", description = "제품 페이지를 반환합니다.")
    public String showProductsPage() {
        return "products";
    }

    @GetMapping("/products/data")
    @Operation(summary = "제품 데이터", description = "페이지네이션된 제품 데이터를 반환합니다.")
    @ResponseBody
    public ProductResponse getProducts(Pageable pageable) {
        Page<Product> productPage = productService.getProducts(pageable);

        List<ProductDto> productDtoList = productPage.getContent().stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(productPage);

        ProductResponse response = new ProductResponse();
        response.setContent(productDtoList);
        response.setPagination(paginationInfo);

        return response;
    }

    @GetMapping("/products/add")
    @Operation(summary = "제품 추가 폼", description = "제품 추가 폼을 반환합니다.")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "add-product";
    }

    @GetMapping("/products/edit/{id}")
    @Operation(summary = "제품 수정 폼", description = "제품 수정 폼을 반환합니다.")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/view/products";
        }

        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("options", optionService.getAllOptions());

        model.addAttribute("product", new ProductDto(product));
        return "edit-product";
    }

    @GetMapping("/products/delete/{id}")
    @Operation(summary = "제품 삭제", description = "주어진 ID를 가진 제품을 삭제합니다.")
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/view/products";
    }

    @GetMapping("/productlist")
    @Operation(summary = "사용자 제품 리스트 페이지", description = "사용자 제품 리스트 페이지를 반환합니다.")
    public String showProductListsPage() {
        return "user-products";
    }

    @GetMapping("/wishlist/data")
    @Operation(summary = "위시리스트 데이터", description = "사용자의 이메일을 기준으로 페이지네이션된 위시리스트 데이터를 반환합니다.")
    public ResponseEntity<ProductResponse> getWishlistItems(
            @RequestParam("email") String email,
            Pageable pageable) {
        Page<Product> productPage = wishlistService.getWishlistByEmail(email, pageable);
        List<ProductDto> productDtoList = productPage.getContent().stream()
                .map(ProductDto::new)
                .collect(Collectors.toList());

        PaginationInfo paginationInfo = new PaginationInfo(productPage);


        ProductResponse response = new ProductResponse();
        response.setContent(productDtoList);
        response.setPagination(paginationInfo);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/wishlist")
    @Operation(summary = "위시리스트 폼", description = "위시리스트 폼을 반환합니다.")
    public String wishlistForm() {
        return "wishlist";
    }
}
