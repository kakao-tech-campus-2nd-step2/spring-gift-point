package gift.value;

public class OptionQuantity {
    private static final int MIN_QUANTITY = 1;
    private static final int MAX_QUANTITY = 1_000_000_000;
    private static final int MIN_SUBTRACT_QUANTITY = 0;

    private final int quantity;

    public OptionQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity >= MAX_QUANTITY) {
            throw new IllegalArgumentException("옵션 수량은 " + MIN_QUANTITY + "개 이상 " + (MAX_QUANTITY - 1) + "개 미만 가능합니다. 시도된 값: " + quantity);
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public OptionQuantity subtract(int quantityToSubtract) {
        if (quantityToSubtract < MIN_SUBTRACT_QUANTITY) {
            throw new IllegalArgumentException("수량은 음수가 될 수 없습니다. 시도된 값: " + quantityToSubtract);
        }
        if (this.quantity - quantityToSubtract < MIN_SUBTRACT_QUANTITY) {
            throw new IllegalArgumentException("수량이 부족합니다. 현재 수량: " + this.quantity + ", 시도된 값: " + quantityToSubtract);
        }
        return new OptionQuantity(this.quantity - quantityToSubtract);
    }
}