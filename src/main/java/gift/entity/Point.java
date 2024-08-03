package gift.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int balance;

    public Point() {
        this.balance = 0;
    }

    public int getBalance() {
        return balance;
    }

    public void subtractBalance(int discountedPrice) {
        this.balance -= discountedPrice;
    }

    public void updateBalance(int newPoint) {
        this.balance = newPoint;
    }
}
