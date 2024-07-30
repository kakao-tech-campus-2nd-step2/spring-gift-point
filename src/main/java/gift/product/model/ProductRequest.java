package gift.product.model;

import gift.option.model.OptionRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

public class ProductRequest {

    public record Create(
        @NotBlank
        @Size(min = 1, max = 15, message = "상품 이름은 공백을 포함하여 최대 15자입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용가능한 특수 문자는 (),[],+,-,&,/,_ 입니다.")
        String name,
        @NotNull
        @Min(value = 0, message = "가격은 0원 이상입니다.")
        Integer price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId,
        @NotNull
        @Size(min = 1, message = "Product 에 해당하는 Option 은 하나 이상이어야 합니다.")
        List<OptionRequest.Create> optionCreateRequests
    ) {

    }

    public record Update(
        @NotBlank
        @Size(min = 1, max = 15, message = "상품 이름은 공백을 포함하여 최대 15자입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "사용가능한 특수 문자는 (),[],+,-,&,/,_ 입니다.")
        String name,
        @NotNull
        @Min(value = 0, message = "가격은 0원 이상입니다.")
        Integer price,
        @NotBlank
        String imageUrl,
        @NotNull
        Long categoryId
    ) {

        public static Update from(ProductResponse productResponse) {
            return new Update(
                productResponse.name(),
                productResponse.price(),
                productResponse.imageUrl(),
                productResponse.categoryId()
            );
        }
    }
}
