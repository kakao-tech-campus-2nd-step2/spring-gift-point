package gift.dto;

import gift.validation.ValidName;
import gift.vo.Option;
import gift.vo.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "옵션 요청 DTO")
public record OptionRequestDto(

    @Schema(description = "옵션 ID")
    Long id,

    @NotNull
    @Schema(description = "상품 ID")
    Long productId,

    @ValidName
    @NotBlank(message = "옵션명을 입력해 주세요.")
    @Schema(description = "옵션명")
    String name,

    @NotNull
    @Positive(message = "옵션 수량은 1개 이상이여야 합니다.")
    @Schema(description = "옵션 수량")
    int quantity
){
    public Option toOption (Product product) {
        return new Option(id, product, name, quantity);
    }
}