package gift.Controller;

import gift.DTO.*;
import gift.Service.OptionService;
import gift.Service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import gift.Model.Entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Product", description = "Product API")
@Controller
@RequestMapping("/api")
public class ProductPageController {

    private final ProductService productService;
    private final OptionService optionService;

    public ProductPageController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    /*
    //서버사이드 렌더링 코드 (6주차 과제에서는 사용을 하지 않아 지우긴 아까워 주석처리)

    @GetMapping("/products")
    public String getProducts(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                             Model model) {
        Page<Product> productPage = productService.getAllProducts(pageable);
        String sortField = pageable.getSort().iterator().next().getProperty();
        String sortDir = pageable.getSort().iterator().next().getDirection().toString().equalsIgnoreCase("ASC") ? "asc" : "desc";
        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("currentPage", pageable.getPageNumber());
        model.addAttribute("size", pageable.getPageSize());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        return "products";
    }
    */

    @Operation(summary = "카테고리에 속하는 상품 목록 조회", description = "상품 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseProductListOfCategoryDTO.class)))
            })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인하거나 카테고리가 존재하지 않습니다")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping("/products")
    public ResponseEntity<Page<ResponseProductListOfCategoryDTO>> getProducts(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                                              @RequestParam("categoryId") Long categoryId) {
        Page<ResponseProductListOfCategoryDTO> page = productService.getAllProducts(pageable, categoryId);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "단일 상품 조회", description = "단일 상품을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseProductDTO.class)))
            })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인하거나 상품 id가 존재하지 않는 id입니다")
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생")
    @GetMapping("/products/{product-id}")
    public ResponseEntity<ResponseProductDTO> getProduct(@PathVariable("product-id") Long productId){
        ResponseProductDTO response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/products/new")
    public String newProductForm(Model model) {
        model.addAttribute("product", new RequestProductPostDTO("이름을 입력해주세요", 1, "url을 입력해주세요", 1L, "옵션 이름을 입력해주세요",1));
        return "new-product";
    }

    @PostMapping("/products")
    public String newProduct(@Valid @ModelAttribute RequestProductPostDTO requestProductPostDTO) {
        productService.addProduct(requestProductPostDTO);
        return "redirect:/api/products";
    }

    @GetMapping("/products/edit/{id}")
    public String editProductForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.selectProduct(id);
        model.addAttribute("product", RequestProductDTO.of(product));
        model.addAttribute("id", id);
        return "edit-product";
    }

    @PutMapping("/products/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id, @Valid @ModelAttribute RequestProductDTO requestProductDTO) {
        productService.editProduct(id, requestProductDTO);
        return "redirect:/api/products";
    }

    @DeleteMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/products";
    }

}