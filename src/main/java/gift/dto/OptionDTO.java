package gift.dto;

import gift.domain.Option;
import gift.domain.Product;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionDTO {

    private Long id;

    @NotBlank(message = "옵션 이름은 필수 입력 항목입니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    @Pattern(regexp = "^[\\p{L}0-9 ()\\[\\]+\\-&/_]+$", message = "옵션 이름에 사용 가능한 특수문자는 ( ), [ ], +, -, &, /, _ 입니다")
    private String name;

    @NotNull(message = "수량은 필수 입력 항목입니다.")
    @Min(value = 1, message = "수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 100000000, message = "수량은 1억 개 미만이어야 합니다.")
    private int quantity;

    public OptionDTO() {}
    
    private OptionDTO(OptionDTOBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.quantity = builder.quantity;
    }

    public OptionDTO(String name) {
        this.name=name;
    }

    public OptionDTO(String optionName, Object o) {
        this.name = optionName;
    }

    public static OptionDTO from(Option option) {
        return new OptionDTOBuilder()
            .id(option.getId())
            .name(option.getName())
            .quantity(option.getQuantity())
            .build();
    }

    public Option toEntity(Product product) {
        return new Option.OptionBuilder()
            .id(this.id)
            .name(this.name)
            .quantity(this.quantity)
            .product(product)
            .build();
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

    public static class OptionDTOBuilder {
        private Long id;
        private String name;
        private int quantity;

        public OptionDTOBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public OptionDTOBuilder name(String name) {
            this.name = name;
            return this;
        }

        public OptionDTOBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public OptionDTO build() {
            return new OptionDTO(this);
        }
    }
}
