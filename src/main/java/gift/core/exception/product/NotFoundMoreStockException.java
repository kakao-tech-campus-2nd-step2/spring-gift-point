package gift.core.exception.product;

public class NotFoundMoreStockException extends RuntimeException{
	public NotFoundMoreStockException() {
		super("수량이 부족합니다.");
	}
}
