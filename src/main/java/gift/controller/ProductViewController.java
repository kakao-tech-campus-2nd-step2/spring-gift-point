package gift.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import gift.dto.OptionDto;
import gift.dto.ProductDto;
import gift.dto.request.ProductRequest;
import gift.dto.response.ProductPageResponse;
import gift.service.CategoryService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;

@Controller
@Tag(name = "product", description = "Product API")
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductViewController(ProductService productService, CategoryService categoryService){
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "상품 조회", description = "파라미터로 받은 상품 페이지를 조회합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 조회 성공")
    })
    public String getProducts(Model model, @RequestParam(value = "page", defaultValue = "0")int page, @RequestParam(value = "size", defaultValue = "10") int size) {
        ProductPageResponse paging = productService.getPage(page, size);
        model.addAttribute("paging", paging);
        return "admin_page";
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 화면", description = "상품 추가 화면을 띄웁니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 추가 화면 이동 성공")
    })
    public String showProductForm(Model model){
        ProductDto product = new ProductDto(0L, null, 0, null, null, List.of(new OptionDto()));
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll().getCategories());
        return "product_form";
    }

    @PostMapping("/new")
    @Operation(summary = "상품 추가", description = "파라미터로 받은 상품을 추가합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 카테고리"),
        @ApiResponse(responseCode = "409", description = "이미 존재하는 상품")
    })
    public String addProduct(@Valid @ModelAttribute ProductRequest productCreateRequest, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()){
            model.addAttribute("product", productCreateRequest);
            model.addAttribute("categories",categoryService.findAll().getCategories());
            return "product_form";
        }

        productService.addProduct(productCreateRequest);
        return "redirect:/api/products";
    }

    @GetMapping("/edit/{id}")
    @Operation(summary = "상품 수정 화면", description = "상품 수정 화면을 띄웁니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 화면 이동 성공")
    })
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id)); 
        model.addAttribute("categories", categoryService.findAll().getCategories());
        return "edit_product_form";
    }

    @PostMapping("/edit/{id}")
    @Operation(summary = "상품 수정", description = "파라미터로 받은 상품을 수정합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 수정 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품 혹은 카테고리")
    })
    public String updateProduct(@PathVariable Long id,@Valid @ModelAttribute ProductRequest productRequest, BindingResult bindingResult, Model model) {
        
        if(bindingResult.hasErrors()){
            model.addAttribute("product", productRequest);
            model.addAttribute("categories", categoryService.findAll().getCategories());
            return "product_form";
        }

        productService.updateProduct(id, productRequest);
        return "redirect:/api/products";
    }

    @PostMapping("/delete/{id}")
    @Operation(summary = "상품 삭제", description = "파라미터로 받은 상품을 삭제합니다." )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "상품 추가 성공"),
        @ApiResponse(responseCode = "404", description = "존재하지 않는 상품")
    })
    public String deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return "redirect:/api/products";
    }
}