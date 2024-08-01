package gift.DTO;

import gift.Model.Entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카테고리에 속하는 상품 목록 조회 응답 DTO")
public record ResponseProductListOfCategoryDTO(
        @Schema(description = "상품 id")
        Long id,

        @Schema(description = "상품 이름")
        String name,

        @Schema(description = "상품 가격")
        int price,

        @Schema(description = "상품 이미지url")
        String imageUrl
){
    public static ResponseProductListOfCategoryDTO of (Product product){
        return new ResponseProductListOfCategoryDTO(product.getId()
                , product.getName().getValue()
                , product.getPrice().getValue()
                , product.getImageUrl().getValue()
        );
    }
}
