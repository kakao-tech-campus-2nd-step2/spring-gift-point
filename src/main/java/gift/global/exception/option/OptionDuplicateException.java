package gift.global.exception.option;

public class OptionDuplicateException extends RuntimeException {

    public OptionDuplicateException(String optionName) {
        super(optionName + " 이름을 가진 옵션이 이미 존재합니다.");
    }
}
