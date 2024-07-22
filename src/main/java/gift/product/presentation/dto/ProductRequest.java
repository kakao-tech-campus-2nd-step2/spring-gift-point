package gift.product.presentation.dto;

import gift.product.business.dto.ProductIn;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.hibernate.validator.constraints.URL;

public class ProductRequest {
    private static final String nameRegex = "^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$";
    private static final String nameMessage = "오직 문자, 공백 그리고 특수문자 (),[],+,&,-,/,_만 허용됩니다.";
    private static final String kakaoRegex = "(?!.*카카오).*";
    private static final String kakaoMessage = "카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.";

    public record Create(
        @NotBlank
        @Size(max = 15)
        @Pattern(
            regexp = nameRegex,
            message = nameMessage
        )
        @Pattern(
            regexp = kakaoRegex,
            message = kakaoMessage
        )
        String name,
        @NotNull @Min(0) Integer price,
        @Size(max = 255) String description,
        @URL String imageUrl,
        @NotNull Long categoryId,
        @Size(min = 1) List<OptionRequest.Create> options
    ) {
        public ProductIn.Create toProductInCreate() {
            return new ProductIn.Create(
                name,
                description,
                price,
                imageUrl,
                categoryId,
                options.stream()
                    .map(OptionRequest.Create::toOptionInCreate)
                    .toList()
                );
        }
    }

    public record Update(
        @NotBlank
        @Size(max = 15)
        @Pattern(
            regexp = nameRegex,
            message = nameMessage
        )
        @Pattern(
            regexp = kakaoRegex,
            message = kakaoMessage
        )
        String name,
        @NotNull @Min(0) Integer price,
        @Size(max = 255) String description,
        @URL String imageUrl,
        @NotNull Long categoryId
    ) {
        public ProductIn.Update toProductInUpdate() {
            return new ProductIn.Update(name, price, description, imageUrl, categoryId);
        }
    }

    public record Ids(
        @NotEmpty
        @Size(min = 1, max = 10)
        List<Long> productIds
    ) {
    }

}
