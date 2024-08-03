package gift.Controller;

import gift.Model.DTO.ProductDTO;
import gift.Service.ProductService;
import gift.Valid.NameValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "상품 API", description = "상품과 관련된 API")
@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    private final ProductService productService;
    private NameValidator nameValidator;

    public ProductRestController(ProductService productService){
        this.productService = productService;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.addValidators(nameValidator);
    }

    @PostMapping()
    @Operation(summary = "상품 추가", description = "이메일과 상품을 받아 product 데이터베이스에 추가한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="productDTO", description = "상품으로, product 데이터베이스에 추가할 데이터이다.")
    @ResponseBody
    public ResponseEntity<?> addProduct(@RequestAttribute("Email") String email, @RequestBody ProductDTO productDTO, BindingResult bindingResult){
        if(!bindingResult.hasErrors()) {
            productService.create(email, productDTO);
            return ResponseEntity.ok("성공");
        }
        return ResponseEntity.badRequest().body("실패");
    }

    @Operation(summary = "상품 삭제", description = "이메일과 상품을 받아 product 데이터베이스에서 제거한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="id", description = "상품의 id로, product 데이터베이스에 삭제할 데이터이다.")
    @DeleteMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<?> deleteProduct(@RequestAttribute("Email") String email, @PathVariable Long productId){
        productService.delete(email, productId);
        return ResponseEntity.ok("성공");
    }

    @Operation(summary = "상품 수정", description = "이메일과 상품을 받아 product 데이터베이스에서 수정한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="id", description = "상품의 id로, product 데이터베이스에 업데이트할 데이터의 id이다.")
    @Parameter(name="productDTO", description = "상품으로, product 데이터베이스에 업데이트할 데이터이다.")
    @PutMapping("/{productId}")
    @ResponseBody
    public ResponseEntity<?> updateProduct(@RequestAttribute("Email") String email, @PathVariable Long productId, @RequestBody ProductDTO productDTO, BindingResult bindingResult){
        if(!bindingResult.hasErrors()) {
            productService.update(email, productId, productDTO);
            return ResponseEntity.ok("성공");
        }

        return ResponseEntity.badRequest().body("실패");
    }

    @Operation(summary = "상품 조회", description = "이메일을 받아 product 데이터베이스에 있는 데이터를 조회한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @GetMapping()
    public Page<ProductDTO> viewAllProductsByPage(@RequestParam(value = "page", defaultValue = "0") int page,
                                                  @RequestParam(value = "size", defaultValue = "10") int size,
                                                  @RequestParam(value = "sort", defaultValue = "10") String sort){
        return productService.getPage(page, size, sort);
    }

    @Operation(summary = "특정 상품 조회", description = "이메일과 상품을 받아 product 데이터베이스에서 특정 상품을 조회한다.")
    @Parameter(name="email", description = "사용자의 이메일로, intercepter에서 header에 있는 인증 토큰을 디코딩하여 email에 추가한다.")
    @Parameter(name="id", description = "상품의 id로, product 데이터베이스에서 조회할 데이터의 id이다.")
    @GetMapping("/{productId}")
    public ProductDTO viewProduct(@PathVariable Long productId){
        return productService.getById(productId);
    }
}
