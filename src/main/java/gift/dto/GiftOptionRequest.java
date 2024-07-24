package gift.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class GiftOptionRequest {

    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-&/_]+$", message = "사용할 수 없는 특수문자입니다.")
    private String name;

    @Size(min = 1,max = 100000000)
    private Integer quantity;

    public GiftOptionRequest(String name, Integer quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public Integer getQuantity() {
        return quantity;
    }
}
