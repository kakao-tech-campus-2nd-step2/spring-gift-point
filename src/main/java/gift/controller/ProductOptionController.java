package gift.controller;

import gift.DTO.ProductOptionDTO;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품 옵션 API", description = "상품 옵션 생성, 조회, 삭제를 수행하는 API입니다. 수정은 구현되지 않았습니다.")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;

    /**
     * 주어진 상품 ID에 해당하는 모든 상품 옵션을 조회합니다.
     *
     * @param id 조회할 상품의 ID
     * @return 조회된 상품 옵션 목록
     */
    @Operation(summary = "상품 옵션 조회", description = "주어진 상품 ID에 해당하는 모든 상품 옵션을 조회합니다.")
    @GetMapping("/{id}/options")
    public List<ProductOptionDTO> getProductOption(@PathVariable Long id) {
        return productOptionService.getProductOptions(id);
    }

    /**
     * 주어진 상품 ID에 새로운 상품 옵션을 생성
     *
     * @param id               상품의 ID
     * @param productOptionDTO 생성할 상품 옵션 정보
     * @return 생성된 상품 옵션 정보
     */
    @Operation(summary = "상품 옵션 생성", description = "주어진 상품 ID에 새로운 상품 옵션을 생성합니다.")
    @PostMapping("/{id}/options")
    public ProductOptionDTO createProductOption(@PathVariable Long id,
                                                @Valid @RequestBody ProductOptionDTO productOptionDTO) {
        return productOptionService.addProductOption(id, productOptionDTO);
    }

    /**
     * 주어진 상품 ID와 옵션 ID에 해당하는 상품 옵션을 삭제
     *
     * @param id       상품의 ID
     * @param optionId 삭제할 상품 옵션의 ID
     */
    @Operation(summary = "상품 옵션 삭제", description = "주어진 상품 ID와 옵션 ID에 해당하는 상품 옵션을 삭제합니다.")
    @DeleteMapping("/{id}/options/{optionId}")
    public void deleteProductOption(@PathVariable Long id, @PathVariable Long optionId) {
        productOptionService.deleteProductOption(optionId);
    }
}
