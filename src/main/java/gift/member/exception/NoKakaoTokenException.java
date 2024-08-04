package gift.member.exception;

public class NoKakaoTokenException extends RuntimeException {

    public NoKakaoTokenException() {
        super("해당 회원은 카카오 토큰이 없습니다.");
    }
}
