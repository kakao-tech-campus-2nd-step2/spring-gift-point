package gift.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record OptionDTO (@NotBlank(message = "옵션 이름은 비어 있을 수 없습니다.")
                        @Size(max=50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
                        @Pattern(regexp = "[ㄱ-힣\\w\\s()\\[\\]\\+\\-\\&\\/]*", message = "허용되지 않는 특수 문자가 포함되어 있습니다")
                        String name,
                        @Size(min = 1, max = 100000000)
                        int quantity){}
