package gift.product.presentation.dto;

import gift.product.business.dto.OptionIn;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionRequest {
    private final static String nameSizeMessage = "옵션 이름은 공백을 포함한 50자 이하여야 합니다.";
    private final static String nameRegex = "^[a-zA-Z0-9ㄱ-ㅎ가-힣 ()\\[\\]+\\-&/_]*$";
    private final static String namePatternMessage = "오직 문자, 공백 그리고 특수문자 (),[],+,&,-,/,_만 허용됩니다.";
    private final static String quantityMinMessage = "수량은 1개 이상이어야 합니다.";
    private final static String quantityMaxMessage = "수량은 100,000,000개 이하이어야 합니다.";

    public record Create(
        @Size(max = 50, message = nameSizeMessage)
        @Pattern(
            regexp = nameRegex,
            message = namePatternMessage
        )
        String name,
        @Min(value = 1, message = quantityMinMessage)
        @Max(value = 100000000, message = quantityMaxMessage)
        Integer quantity
    ) {
        public OptionIn.Create toOptionInCreate() {
            return new OptionIn.Create(name, quantity);
        }
    }

    public record Update (
        @Min(1)
        Long id,
        @Size(max = 50, message = nameSizeMessage)
        @Pattern(
            regexp = nameRegex,
            message = namePatternMessage
        )
        String name,
        @Min(value = 1, message = quantityMinMessage)
        @Max(value = 100000000, message = quantityMaxMessage)
        Integer quantity)
    {

        public OptionIn.Update toOptionInUpdate() {
            return new OptionIn.Update(id, name, quantity);
        }
    }
}
