package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

@Entity
@Schema(description = "선물 주문")
public class GiftOrder {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "선물 주문 고유 id ")
    private Long id;

    @OneToOne
    @JoinColumn(name = "option_id", nullable = false)
    @Schema(description = "선택한 옵션 id")
    private Option option;

    @Column
    @Schema(description = "선물 주문 일시")
    private LocalDateTime orderDateTime;

    @OneToOne
    @JoinColumn(name = "product_id", nullable = false)
    @Schema(description = "선택한 상품 정보")
    private Product product;

    @Column
    private Long quantity;


    @Column
    @Schema(description = "선물과 함께 보낼 메세지")
    private String message;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public GiftOrder() {
    }

    public GiftOrder(Option option, Long quantity, LocalDateTime orderDateTime, String message) {
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public GiftOrder(Long id, LocalDateTime orderDateTime, Product product, Member member, Option option, String message) {
        this.id = id;
        this.orderDateTime = orderDateTime;
        this.product = product;
        this.member = member;
        this.option = option;
        this.message = message;
    }

    public GiftOrder(String message, Product product, Member member) {
        this.message = message;
        this.product = product;
        this.member = member;
    }

    public GiftOrder(Option option, Long quantity, LocalDateTime orderDateTime, String message, Member member) {
        this.option = option;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Option getOption() {
        return option;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public Member getMember() {
        return member;
    }
}
