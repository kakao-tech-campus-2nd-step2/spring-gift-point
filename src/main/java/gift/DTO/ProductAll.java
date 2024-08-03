package gift.DTO;

import java.util.Collections;
import java.util.List;

public record ProductAll(
        ProductDTO productDTO,
        List<ProductOptionDTO> productOptionDTOList
) {

    /**
     * ProductAll 생성자
     *
     * @param productDTO       상품 정보
     * @param productOptionDTO 상품 옵션 정보
     */
    public ProductAll(ProductDTO productDTO, ProductOptionDTO productOptionDTO) {
        this(productDTO, Collections.singletonList(productOptionDTO));
    }
}
