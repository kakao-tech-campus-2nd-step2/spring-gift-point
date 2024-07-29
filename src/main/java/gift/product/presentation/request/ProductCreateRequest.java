package gift.product.presentation.request;

import gift.option.presentation.request.OptionCreateRequest;
import gift.product.application.command.ProductCreateCommand;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductCreateRequest(
        @NotNull @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣()\\[\\]+\\-\\&\\/\\_\\s]*$", message = "상품 이름에 허용되지 않는 특수 문자가 포함되어 있습니다.")
        String name,
        Integer price,
        String imageUrl,
        Long categoryId,
        List<OptionCreateRequest> optionCreateRequestList
) {
    public ProductCreateCommand toCommand() {
        return new ProductCreateCommand(
                name,
                price,
                imageUrl,
                categoryId,
                optionCreateRequestList.stream()
                        .map(OptionCreateRequest::toCommand)
                        .toList()
        );
    }
}
