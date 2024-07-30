package gift.exception.customException;

import static gift.exception.exceptionMessage.ExceptionMessage.NAME_IN_KAKAO;

public class KakaoInNameException extends RuntimeException{
    public KakaoInNameException(){
        super(NAME_IN_KAKAO);
    }

    public KakaoInNameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

}
