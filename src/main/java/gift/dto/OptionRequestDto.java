package gift.dto;

import gift.validation.ValidName;
import gift.vo.Option;
import gift.vo.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OptionRequestDto(
    Long id,

    @NotNull
    Long productId,

    @ValidName
    @NotBlank(message = "옵션명을 입력해 주세요.")
    String name,

    @NotNull
    @Positive(message = "옵션 수량은 1개 이상이여야 합니다.")
    int quantity
){
    public Option toOption (Product product) {
        return new Option(id, product, name, quantity);
    }
}