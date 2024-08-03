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

    private int value;

    public Point() {
        this.value = 0;
    }

    public int getValue() {
        return value;
    }

    public void subtractValue(int discountedPrice) {
        this.value -= discountedPrice;
    }
}
