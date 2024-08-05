package gift.global.exception.wish;

public class WishNotFoundException extends RuntimeException {

    public WishNotFoundException(Long id) {
        super("id 가 " + id + " 인 장바구니 정보를 찾을 수 없습니다.");
    }
}
