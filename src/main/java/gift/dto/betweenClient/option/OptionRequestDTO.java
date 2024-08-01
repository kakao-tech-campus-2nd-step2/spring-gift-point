package gift.dto.betweenClient.option;

import gift.entity.Option;
import gift.entity.Product;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OptionRequestDTO(
        @NotBlank(message = "옵션 이름을 입력해주세요.")
        String name,

        @NotNull(message = "옵션 수량을 입력해주세요.")
        Integer quantity
) {
    public Option convertToOption(Product product){
        return new Option(product, name, quantity);
    }
}
