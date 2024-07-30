package gift.dto.request;


import jakarta.validation.constraints.*;

public class OptionRequest {

    @NotBlank(message = "옵션 이름을 입력하세요.")
    @Pattern(regexp = "^[\\w\\s()\\[\\]+\\-&/]+$")
    @Size(max = 50, message = "옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있습니다.")
    private String name;

    @NotNull(message = "옵션 수량을 입력하세요.")
    @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
    @Max(value = 99999999, message = "옵션 수량은 최대 1억 개 미만이어야 합니다.")
    private Integer quantity;

    public OptionRequest(String name, Integer quantity){
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity(){
        return quantity;
    }
}
