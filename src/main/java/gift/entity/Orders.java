package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Orders {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name="option_id", nullable = false)
    @ManyToOne
    private Option option;

    @Column(nullable = false)
    private int quantity;

    private String message;

    @ManyToOne
    private Member member;

    @CreatedDate
    private LocalDateTime orderDateTime;

    public Orders() {
    }

    public Orders(Option option, int quantity, String message, Member member,LocalDateTime time) {
        this.option = option;
        this.quantity = quantity;
        this.message = message;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
