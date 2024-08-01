package gift.global.exception.user;

public class MemberNotFoundException extends RuntimeException {

    public MemberNotFoundException(Long id) {
        super("id 가 " + id + " 인 유저를 찾을 수 없습니다.");
    }
}
