package gift.common.exception;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String name) {
        super("상품 :" + name + "는 이미 존재하는 상품입니다." );
    }
}
