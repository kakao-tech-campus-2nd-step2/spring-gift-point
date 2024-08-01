package gift.controller;

import gift.domain.Option;
import gift.domain.Product;
import gift.dto.ProductDTO;
import gift.service.CategoryService;
import gift.service.OptionService;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.*;

@Controller
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "상품 API 관련 엔드포인트")
public class ProductController {

    private static final String REDIRECT_URL = "redirect:/api/products";

    private final ProductService productService;
    private final CategoryService categoryService;
    private final OptionService optionService;

    public ProductController(ProductService productService, CategoryService categoryService, OptionService optionService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.optionService = optionService;
    }

    @GetMapping
    @Operation(summary = "상품 목록 조회", description = "모든 상품의 목록을 조회합니다.")
    public ResponseEntity<Page<ProductDTO>> getProducts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "id,asc") String[] sort,
                                                     @RequestParam(required = false) Long categoryId) {
        Sort.Direction direction = Sort.Direction.fromString(sort[1]);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<Product> productPage;
        if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, pageRequest);
        } else {
            productPage = productService.getAllProducts(pageRequest);
        }

        Page<ProductDTO> dtoPage = productPage.map(ProductDTO::convertToDto);

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{productId}")
    @Operation(summary = "상품 개별 조회", description = "개별적인 상품을 조회합니다.")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable("productId") Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        ProductDTO productDTO = ProductDTO.convertToDto(product);

        return ResponseEntity.ok(productDTO);
    }


    @PostMapping
    @Operation(summary = "상품 추가", description = "새로운 상품을 추가합니다.")
    public ResponseEntity<String> addProduct(@Valid @ModelAttribute Product product, @RequestParam List<String> optionNames, @RequestParam List<Integer> optionQuantities, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid product data");
        }

        productService.addProduct(product);

        for (int i = 0; i < optionNames.size(); i++) {
            Option option = new Option();
            option.setName(optionNames.get(i));
            option.setQuantity(optionQuantities.get(i));
            optionService.addOptionToProduct(product.getId(), option);

        }

        return ResponseEntity.status(HttpStatus.CREATED).body("상품 추가 성공!");
    }

    @GetMapping("/new")
    @Operation(summary = "상품 추가 폼 조회", description = "새로운 상품을 추가하기 위한 폼을 조회합니다.")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "addProduct";
    }

    @GetMapping("/{productId}/edit")
    @Operation(summary = "상품 수정 폼 조회", description = "상품을 수정하기 위한 폼을 조회합니다.")
    public String showEditProductForm(@PathVariable("productId") Long id, Model model) {
        Product product = productService.getProductById(id);

        if (product == null) {
            return REDIRECT_URL;
        }

        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllCategories());

        return "editProduct";
    }

    @PutMapping("/{productId}")
    @Operation(summary = "상품 수정", description = "기존 상품 정보를 수정합니다.")
    public ResponseEntity<String> editProduct(@PathVariable("productId") Long id, @Valid @ModelAttribute Product product, BindingResult bindingResult) {
        // 상품 정보 수정
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Invalid product data");
        }
        productService.updateProduct(id, product);

        return ResponseEntity.ok("상품 수정 성공!");
    }

    @DeleteMapping("/{productId}")
    @Operation(summary = "상품 삭제", description = "특정 상품을 삭제합니다.")
    public ResponseEntity<String> deleteProduct(@PathVariable("productId") Long id) {
        // 요청받은 id를 가진 상품을 삭제
        productService.deleteProduct(id);

        return ResponseEntity.ok("상품 삭제 성공!");
    }
    
}
