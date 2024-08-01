package gift.order.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.member.model.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name = "product_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime localDateTime;

    private String message;

    private Long optionId;

    private int quantity;

    private Long productId;


    @ManyToOne
    @JoinColumn(name = "member_id")
    @JsonIgnore
    private Member member;


    public Order(Long productId,Long optionId, int quantity, String message,Member member) {
        this.productId = productId;
        this.message = message;
        this.optionId = optionId;
        this.quantity = quantity;
        this.member = member;
        this.localDateTime = LocalDateTime.now();
    }
}
