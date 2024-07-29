package gift.exception.customException;

public class KakaoMemberNotFoundException extends RuntimeException {
    public KakaoMemberNotFoundException(String message){
        super(message);
    }
}
