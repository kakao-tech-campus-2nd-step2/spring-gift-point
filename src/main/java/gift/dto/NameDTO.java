package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
public class NameDTO {

    @NotNull(message = "이름을 입력해주세요.")
    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    public NameDTO() {}

    public NameDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}