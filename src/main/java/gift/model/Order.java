package gift.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_option_id", nullable = false)
  private ProductOption productOption;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  private int quantity;
  private LocalDateTime orderDateTime;
  private String message;
  private int pointsUsed;
  private int pointsEarned;

  public Order() {}

  public Order(ProductOption productOption, Member member, int quantity, String message, LocalDateTime orderDateTime, int pointsUsed, int pointsEarned) {
    this.productOption = productOption;
    this.member = member;
    this.quantity = quantity;
    this.message = message;
    this.orderDateTime = orderDateTime;
    this.pointsUsed = pointsUsed;
    this.pointsEarned = pointsEarned;
  }

  public Long getId() {
    return id;
  }

  public ProductOption getProductOption() {
    return productOption;
  }

  public Member getMember() {
    return member;
  }

  public int getQuantity() {
    return quantity;
  }

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public String getMessage() {
    return message;
  }

  public int getPointsUsed() {
    return pointsUsed;
  }

  public int getPointsEarned() {
    return pointsEarned;
  }

  public void updatePoints(int pointsUsed, int pointsEarned) {
    this.pointsUsed = pointsUsed;
    this.pointsEarned = pointsEarned;
  }
}