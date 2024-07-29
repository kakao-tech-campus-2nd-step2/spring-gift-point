package gift.Model.Entity;

import gift.Model.Value.Quantity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Option option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Embedded
    @AttributeOverride(name="value", column = @Column(name="quantity"))
    private Quantity quantity;

    private LocalDateTime orderDateTime;

    private String message;

    protected Order() {
    }

    public Order(Option option, Member member, Quantity quantity, LocalDateTime orderDateTime, String message) {
        validateOption(option);
        validateMember(member);
        validateQuantity(quantity);

        this.option = option;
        this.quantity = quantity;
        this.member = member;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }


    public Order(Option option, Member member, Integer quantity, LocalDateTime orderDateTime, String message) {
        this(option, member, new Quantity(quantity), orderDateTime, message);
    }

    private void validateMember(Member member) {
        if (member == null)
            throw new IllegalArgumentException("member는 null이 될 수 없습니다");
    }

    private void validateQuantity(Quantity quantity) {
        if (quantity == null)
            throw new IllegalArgumentException("quantity는 null이 될 수 없습니다");
    }

    private void validateOption(Option option) {
        if(option == null)
            throw new IllegalArgumentException("Option은 null이 될 수 없습니다");
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Option getOption() {
        return option;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void checkOrderBelongsToMember(Member member) {
        if (this.member != member) {
            throw new IllegalArgumentException("이 주문은 해당 member의 주문이 아닙니다");
        }
    }

    public void addQuantity(Quantity deltaQuantity) {
        validateQuantity(deltaQuantity);
        this.quantity.add(deltaQuantity.getValue());
        this.option.subtractQuantity(deltaQuantity.getValue());
    }

    public void subtractQuantity(Quantity deltaQuantity) {
        validateQuantity(deltaQuantity);
        this.quantity.subtract(deltaQuantity.getValue());
        this.option.addQuantity(deltaQuantity.getValue());
    }
}
