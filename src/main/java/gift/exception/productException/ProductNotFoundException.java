package gift.exception.productException;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(Long productId) {
        super("상품 ID  : " + productId +" 는 찾을 수 없습니다.");
    }
}



