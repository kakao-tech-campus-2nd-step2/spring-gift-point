package gift.dto;

import gift.domain.Option;
import gift.domain.Wish;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionDto {
    private Long id;

    @Size(min = 1, max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$",
        message = "허용 가능한 특수문자는 ( ), [ ], +, -, &, /, _ 입니다.")
    private String name;

    @Min(1)
    @Max(99999999)
    private int quantity;

    public OptionDto() {
    }

    public OptionDto(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public OptionDto(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public static OptionDto convertToDto(Option option) {
        return new OptionDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        );
    }
}
