package gift.dto;

import jakarta.validation.constraints.NotNull;

public class OptionNameDTO {

    @NotNull(message = "이름을 입력해주세요.")
    private String name;

    public OptionNameDTO() {}

    public OptionNameDTO(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}