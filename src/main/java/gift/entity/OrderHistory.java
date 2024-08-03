package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class OrderHistory {
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^(01[016789]-?\\d{3,4}-?\\d{4})$");


    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "option_id", nullable = false)
    private Option option;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private LocalDateTime orderDateTime;

    @Column(nullable = false)
    private String message;

    @Column(nullable = true)
    private String phoneNumber;

    protected OrderHistory() { }

    public OrderHistory(Option option, Integer quantity, LocalDateTime orderDateTime, String message, String phoneNumber) {
        validateQuantity(quantity);
        validateMessage(message);
        validatePhoneNumber(phoneNumber);

        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() { return id; }
    public Option getOption() { return option; }
    public Integer getQuantity() { return quantity; }
    public LocalDateTime getOrderDateTime() { return orderDateTime; }
    public String getMessage() { return message; }

    private void validateQuantity(Integer quantity){
        if(quantity == null)
            throw new BadRequestException("개수가 올바르지 않습니다.");

        if(quantity < 1 || quantity >= 100000000)
            throw new BadRequestException("주문 수량은 1개 이상, 1억개 미만만 가능합니다.");
    }

    private void validateMessage(String message){
        if(message == null || message.isEmpty())
            throw new BadRequestException("주문 메세지를 입력해주세요.");
    }

    private void validatePhoneNumber(String phoneNumber){
        if(phoneNumber != null && !PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches())
            throw new BadRequestException("올바르지 않은 전화번호 입니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderHistory orderHistory = (OrderHistory) o;
        return Objects.equals(id, orderHistory.id) && Objects.equals(option, orderHistory.option)
                && Objects.equals(quantity, orderHistory.quantity) && Objects.equals(
                orderDateTime, orderHistory.orderDateTime) && Objects.equals(message, orderHistory.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, option, quantity, orderDateTime, message);
    }
}
