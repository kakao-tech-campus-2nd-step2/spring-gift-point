package gift.Controller;


import gift.DTO.ProductDTO;
import gift.Model.Category;
import gift.Model.Option;
import gift.Model.Product;
import gift.Service.ProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name = "Product", description = "Product 관련 API")
@Controller
public class ProductController {

    private final ProductService productService;
    @Autowired
    public ProductController(ProductService productService){
        this.productService = productService;
    }
    @Operation(
        summary = "모든 상품 가져오기",
        description = "등록된 모든 상품을 가져와 index.html로 전달"
    )
    @ApiResponse(
        responseCode = "200",
        description = "모든 상품 페이지 연결 성공"
    )
    @Parameters({
        @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체"),
        @Parameter(name = "pageable", description = "List에 담긴 Product객체를 개수에 맞춰서 page로 리턴")
    })
    @GetMapping("/api/products")
    public String getProducts(Model model, Pageable pageable) {
        Page<Product> productPage = productService.findAll(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("page", productPage);
        return "index";
    }

    @Operation(
        summary = "상품 더하기 페이지",
        description = "더할 상품의 정보를 입력을 받기 위해 post.html과 연결"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 더하기 페이지 연결 성공"
    )
    @Parameter(name = "model", description = "html파일로 보낼 객체를 담을 객체")
    @GetMapping("/api/products/add")
    public String newProductForm(Model model) {
        model.addAttribute("product", new ProductDTO(0L,"",0,"",new Category(0L,"","","","",null),null));
        model.addAttribute("categories", productService.getAllCategory());
        return "post";
    }

    @Operation(
        summary = "상품 더하기",
        description = "상품을 product테이블에 저장"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 더하기 성공"
    )
    @Parameter(name = "product", description = "더할 상품 객체")
    @PostMapping("/api/products")
    public String createProduct(@Valid @ModelAttribute ProductDTO productDTO) {
        productService.addProduct(productDTO);
        return "redirect:/api/products";
    }

    @Operation(
        summary = "상품 수정하기",
        description = "상품을 수정"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 수정 페이지 연결 성공"
    )
    @Parameter(name = "product", description = "수정할 상품 객체")
    @GetMapping("/api/products/update/{productId}")
    public String editProductForm(@PathVariable(value = "productId") Long productId, Model model) {
        Product product = productService.getProductById(productId);
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategory());
        return "update";
    }

    @Operation(
        summary = "상품 수정하기",
        description = "상품을 테이블에 업데이트"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 업데이트 성공"
    )
    @Parameters({
        @Parameter(name = "productId", description = "수정할 상품 Id"),
        @Parameter(name = "product", description = "수정할 상품 객체")
    })

    @PostMapping("/api/products/update/{productId}")
    public String updateProduct(@PathVariable(value = "productId") Long productId, @Valid @ModelAttribute ProductDTO newProductDTO) {
        productService.updateProduct(newProductDTO);
        return "redirect:/api/products";
    }

    @Operation(
        summary = "상품 삭제",
        description = "상품을 product테이블에서 삭제"
    )
    @ApiResponse(
        responseCode = "200",
        description = "상품 삭제 성공"
    )
    @Parameter(name = "productId", description = "삭제할 상품 객체 Id")
    @PostMapping("/api/products/delete/{productId}")
    public String deleteProduct(@PathVariable(value = "productId") Long productId) {
        productService.deleteProduct(productId);
        return "redirect:/api/products";
    }
}
