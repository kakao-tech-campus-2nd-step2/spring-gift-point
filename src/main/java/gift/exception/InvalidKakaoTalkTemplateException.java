package gift.exception;

public class InvalidKakaoTalkTemplateException extends RuntimeException {

    public InvalidKakaoTalkTemplateException() {
        super("메시지 형식이 맞지 않습니다.");
    }
}
