package gift.global.exception.option;

public class OptionNotFoundException extends RuntimeException {

    public OptionNotFoundException(Long id) {
        super("id 가 " + id + " 인 옵션을 찾을 수 없습니다.");
    }
}
