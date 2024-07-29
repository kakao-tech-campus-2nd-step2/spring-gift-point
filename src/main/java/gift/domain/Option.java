package gift.domain;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;


public class Option {

    private Long id;

    @NotBlank(message = "옵션 이름을 입력해 주세요.")
    @Size(min = 1, max = 50, message = "1~50자 사이로 적어주세요")
    @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다")
    private String name;

    @Min(value = 1, message = "0초과 1억미만의 범위로 입력 가능합니다.")
    @Max(value = 99999999, message = "0초과 1억미만의 범위로 입력 가능합니다.")
    private Long quantity;

    public Option() {

    }

    public Option(String name, Long quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public Option(Long id, String name, Long quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
