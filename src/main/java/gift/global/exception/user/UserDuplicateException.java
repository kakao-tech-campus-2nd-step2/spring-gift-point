package gift.global.exception.user;

public class UserDuplicateException extends RuntimeException {

    public UserDuplicateException(String email) {
        super(email + " 이메일을 가진 유저가 이미 존재합니다.");
    }

}
