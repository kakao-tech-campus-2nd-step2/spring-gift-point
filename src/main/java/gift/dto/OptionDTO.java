package gift.dto;

import jakarta.validation.constraints.*;

public class OptionDTO {
    private Long id;

    @NotBlank(message = "옵션 이름은 필수 입력 사항입니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.")
    @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.")
    private String name;

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억개 미만이어야 합니다.")
    private int quantity;

    public OptionDTO() {
    }

    public OptionDTO(Long id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public OptionDTO(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank(message = "옵션 이름은 필수 입력 사항입니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.") @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "옵션 이름은 필수 입력 사항입니다.") @Pattern(regexp = "^[a-zA-Z0-9가-힣 ()\\[\\]+\\-&/_]*$", message = "옵션 이름에 허용되지 않는 문자가 포함되어 있습니다.") @Size(max = 50, message = "옵션 이름은 최대 50자까지 입력할 수 있습니다.") String name) {
        this.name = name;
    }

    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 1억개 미만이어야 합니다.")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(@Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.") @Max(value = 99999999, message = "옵션 수량은 1억개 미만이어야 합니다.") int quantity) {
        this.quantity = quantity;
    }
}
