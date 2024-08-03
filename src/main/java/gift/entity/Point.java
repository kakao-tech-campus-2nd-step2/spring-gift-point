package gift.entity;

import gift.exception.InsufficientPointException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    private int balance;

    public Point() {
        this.balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void subtractBalance(int totalPrice) {
        int discountedPrice = (int) (0.95 * totalPrice);
        if (discountedPrice > balance) {
            throw new InsufficientPointException();
        }
        this.balance -= discountedPrice;
    }

    public void updateBalance(int newPoint) {
        if (newPoint < 0) {
            throw new IllegalArgumentException("포인트는 0 이상이어야 합니다.");
        }
        this.balance = newPoint;
    }
}
