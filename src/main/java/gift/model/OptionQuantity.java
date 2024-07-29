package gift.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class OptionQuantity {
    private static final int QUANTITY_LIMIT = 99999999;

    private int quantity;

    protected OptionQuantity() {
    }

    public OptionQuantity(int quantity) {
        if (quantity < 1 || quantity > QUANTITY_LIMIT) {
            throw new IllegalArgumentException("옵션 수량은 1개 이상 1억 개 미만이어야 합니다.");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}