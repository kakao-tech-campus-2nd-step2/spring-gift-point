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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "카테고리에 속하는 상품 목록 조회", description = "상품 목록을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ResponseProductListOfCategoryDTO.class)))
            })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인하거나 카테고리가 존재하지 않습니다",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @GetMapping("/products")
    public ResponseEntity<Page<ResponseProductListOfCategoryDTO>> getProducts(@PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
                                                                              @RequestParam("categoryId") Long categoryId) {
        Page<ResponseProductListOfCategoryDTO> page = productService.getAllProducts(pageable, categoryId);
        return ResponseEntity.ok(page);
    }

    @Operation(summary = "단일 상품 조회", description = "단일 상품을 조회합니다")
    @ApiResponse(responseCode = "200", description = "조회 완료",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ResponseProductDTO.class))
            })
    @ApiResponse(responseCode = "400", description = "잘못된 요청입니다. 입력값을 확인하거나 상품 id가 존재하지 않는 id입니다",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @ApiResponse(responseCode = "500", description = "서버 내부 에러 발생",
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = String.class))
            })
    @GetMapping("/products/{product-id}")
    public ResponseEntity<ResponseProductDTO> getProduct(@PathVariable("product-id") Long productId){
        ResponseProductDTO response = productService.getProduct(productId);
        return ResponseEntity.ok(response);
    }
}