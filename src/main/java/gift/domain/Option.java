package gift.domain;


import gift.entity.OptionEntity;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class Option {

    public record OptionRequest(
        @NotBlank(message = "옵션 이름을 입력해 주세요.")
        @Size(min = 1, max = 50, message = "1~50자 사이로 적어주세요")
        @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다")
        String name,
        @Min(1)
        @Max(99999999)
        Long quantity
    ) {

    }

    public record OptionResponse(
        Long id,
        String name,
        Long quantity,
        Long productId
    ) {
        public static OptionResponse from(OptionEntity optionEntity) {
            return new OptionResponse(
                optionEntity.getId(),
                optionEntity.getName(),
                optionEntity.getQuantity(),
                optionEntity.getProductEntity().getId()
            );
        }
    }
}
