package gift.controller;

import gift.domain.Product;
import gift.dto.CategoryDto;
import gift.dto.ProductDto;
import gift.repositories.ProductRepository;
import gift.services.CategoryService;
import gift.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Tag(name = "PageController", description = "Page API")
public class PageController {

    private final ProductService productService;
    private final CategoryService categoryService;
    @Autowired
    ProductRepository productRepository;

    public PageController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping("/") // 주소 매핑
    @Operation(summary = "메인 페이지", description = "모든 제품을 리스트로 보여주는 메인 페이지")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "페이지 로드 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public String indexPageGet(Model model,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Direction.DESC) Pageable pageable) {
        Page<ProductDto> products = productService.getAllProducts(pageable);
        int totalPages = products.getTotalPages();

        // 총 페이지 수가 0일 때 1로 설정
        if (totalPages == 0) {
            totalPages = 1;
        }
        int nowPage = products.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, totalPages);

        model.addAttribute("products", products);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "index";
    }

    @GetMapping("/add") // 주소 매핑
    @Operation(summary = "제품 추가 페이지", description = "새로운 제품을 추가하는 페이지")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 추가 페이지 로드 성공"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public String addPageGet(Model model) {
        Product product = new Product(null, "", 0, "", null);
        List<CategoryDto> categories = categoryService.getAllCategories();
        model.addAttribute("product", product);
        model.addAttribute("category", categories);
        return "add";
    }

    @GetMapping("/update") // 주소 매핑
    @Operation(summary = "제품 수정 페이지", description = "기존 제품 정보를 수정하는 페이지")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "제품 수정 페이지 로드 성공"),
        @ApiResponse(responseCode = "404", description = "제품을 찾을 수 없음"),
        @ApiResponse(responseCode = "500", description = "서버 내부 오류 발생")
    })
    public String updatePageGet(@RequestParam Long id, Model model) {
        Optional<Product> product = productRepository.findById(id);
        List<CategoryDto> categories = categoryService.getAllCategories();

        if (product.isEmpty()) {
            product = Optional.of(new Product(null, "", 0, "", null));
        }
        model.addAttribute("product", product);
        model.addAttribute("category", categories);

        return "update";
    }
}
