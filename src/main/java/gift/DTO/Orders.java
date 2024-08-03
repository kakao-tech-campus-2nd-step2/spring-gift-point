package gift.DTO;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table
public class Orders {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "orders_id")
  private Option option;
  private int quantity;

  private LocalDateTime orderDateTime;
  private String message;

  private int usedPoint;

  public Orders(Option option, int quantity, String message, int usedPoint) {
    this.option = option;
    this.quantity = quantity;
    this.orderDateTime = LocalDateTime.now();
    this.message = message;
    this.usedPoint = usedPoint;
  }

  public Orders(Long id, Option option, int quantity, String message, int usedPoint) {
    this.id = id;
    this.option = option;
    this.quantity = quantity;
    this.orderDateTime = LocalDateTime.now();
    this.message = message;
    this.usedPoint = usedPoint;
  }

  protected Orders() {
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

  public LocalDateTime getOrderDateTime() {
    return orderDateTime;
  }

  public String getMessage() {
    return message;
  }

  public int getUsedPoint() {
    return usedPoint;
  }
}
