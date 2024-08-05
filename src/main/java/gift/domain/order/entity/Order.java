package gift.domain.order.entity;

import gift.domain.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();;

    @Column
    private String recipientMessage;

    @Embedded
    @Column(nullable = false)
    private Price purchasePrice;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime orderDateTime;

    protected Order() {
    }

    public Order(Long id, Member member, String recipientMessage) {
        this.id = id;
        this.member = member;
        this.recipientMessage = recipientMessage;
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public String getRecipientMessage() {
        return recipientMessage;
    }

    public int getPurchasePrice() {
        return purchasePrice.value();
    }

    public void assignPurchasePrice(Price purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }
}
