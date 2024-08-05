package gift.DTO;

import jakarta.persistence.Embedded;
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

  @Embedded
  private PointVo pointVo;

  public Orders(Option option, int quantity, String message, PointVo pointVo) {
    this.option = option;
    this.quantity = quantity;
    this.orderDateTime = LocalDateTime.now();
    this.message = message;
    this.pointVo = pointVo;
  }

  public Orders(Long id, Option option, int quantity, String message, PointVo pointVo) {
    this.id = id;
    this.option = option;
    this.quantity = quantity;
    this.orderDateTime = LocalDateTime.now();
    this.message = message;
    this.pointVo = pointVo;
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

  public PointVo getPointVo() {
    return this.pointVo;
  }
}
