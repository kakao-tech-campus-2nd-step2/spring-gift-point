package gift.domain.Option;

import jakarta.persistence.Embeddable;

@Embeddable
public class OptionName {
    private String optionName;

    public OptionName(String optionName){
        validOptionName(optionName);
        this.optionName = optionName;
    }

    public OptionName() {

    }

    public String getoptionName() {
        return optionName;
    }

    public void validOptionName(String optionName){
        String PATTERN = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]*$";
        if(!optionName.matches(PATTERN)){
            throw new IllegalArgumentException(" ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다.");
        }
    }
}
