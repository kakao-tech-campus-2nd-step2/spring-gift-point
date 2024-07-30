package gift.global.exception.product;

public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(Long id) {
        super("id 가 " + id + " 인 상품을 찾을 수 없습니다.");
    }
}
