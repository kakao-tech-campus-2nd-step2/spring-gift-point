package gift.global.exception.cartItem;

public class CartItemNotFoundException extends RuntimeException {

    public CartItemNotFoundException(Long id) {
        super("id 가 " + id + " 인 장바구니 정보를 찾을 수 없습니다.");
    }
}
