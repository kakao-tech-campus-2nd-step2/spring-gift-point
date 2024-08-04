package gift.model.valueObject;

public class Quantity {
    private final long quantity;

    private static final double MAX_OPTION_NUM = 100000000;

    public Quantity(long quantity) {
        this.quantity = quantity;

        if (!isCorrectQuantityUpdate(this.quantity)) {
            throw new IllegalArgumentException("옵션은 1개 이상 1억개 미만이어야 합니다.");
        }
    }

    public long getQuantity() {
        return quantity;
    }

    private boolean isCorrectQuantityUpdate(long quantity) {
        if (quantity < 1 || quantity >= MAX_OPTION_NUM) {
            return false;
        }
        return true;
    }
}
