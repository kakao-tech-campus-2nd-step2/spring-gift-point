package gift.controller;

import gift.dto.ProductDto;
import gift.exception.ProductNotFoundException;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Tag(name="관리자 화면 API", description = "프론트 연결 때는 쓰이지 않음")
@RequestMapping("/api/admin")
public class AdminController {
    private final ProductService productService ;

    public AdminController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 화면", description = "list.html 을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품 목록을 가져왔습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class)))
    @GetMapping("/products/list")
    public String listProducts(Model model) {
        List<ProductDto> products = productService.findAll();
        model.addAttribute("products", products);
        return "list"; // list.html 파일 보여주기
    }

    @Operation(summary = "상품 상세 정보 화면", description = "view.html 을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품 정보를 가져왔습니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Product Name\", \"description\": \"Product Description\", \"price\": 1000}")))
    @GetMapping("/products/view/{productId}")
    public String viewProduct(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
        model.addAttribute("product", product);
        return "view"; // view.html 파일 보여주기
    }

    @Operation(summary = "상품 추가 화면", description = "add.html 을 보여줍니다.")
    @GetMapping("/products/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductDto());
        return "add"; // add.html 파일 보여주기
    }

    @Operation(summary = "상품 수정 화면", description = "edit.html 을 보여줍니다.")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품 정보를 수정할 수 있는 화면을 표시합니다.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ProductDto.class),
                    examples = @ExampleObject(value = "{\"id\": 1, \"name\": \"Updated Product Name\", \"description\": \"Updated Product Description\", \"price\": 1200}")))
    @GetMapping("/products/edit/{productId}")
    public String showEditProductForm(@PathVariable("id") Long id, Model model) {
        ProductDto product = productService.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("상품을 찾을 수 없습니다. ID: " + id));
        model.addAttribute("product", product);
        return "edit"; // edit.html 파일 보여주기
    }

    @Operation(summary = "상품 삭제", description = "해당 상품 삭제 후 상품 목록 화면으로 이동 ")
    @ApiResponse(responseCode = "200", description = "성공적으로 상품을 삭제했습니다.")
    @DeleteMapping("/products/{productId}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/admin/products/list";
    }
}