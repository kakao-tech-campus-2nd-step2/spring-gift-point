package gift.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;

@Embeddable
public class Name {

    @NotNull(message = "이름을 입력해주세요.")
    @Length(min = 1, max = 15, message = "1자 ~ 15자까지 가능합니다.")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "카카오가 포함된 문구는 현재 사용 할 수 없습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9가-힣\\(\\)\\[\\]\\+\\-\\&\\/\\_ ]*$", message = "사용불가한 특수 문자가 포함되어 있습니다.")
    private String name;

    protected Name() {
    }

    public Name(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}