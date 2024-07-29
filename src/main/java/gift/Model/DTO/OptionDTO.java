package gift.Model.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionDTO(Long id,
                        @Size(max=50, message = "글자의 길이는 50을 넘을 수 없습니다.")
                        @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "허용되지 않은 특수 기호((),[],+,-,&,/,_ 이외)가 있습니다.")
                        String name,
                        @Min(value = 1, message = "수량은 최소 1개 이상이여야 합니다.")
                        @Max(value = 100000000, message = "수량은 100000000개 미만이여야 합니다.")
                        Long quantity) {
}
