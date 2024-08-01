package gift.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Order {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_option_id", nullable = false)
  private ProductOption productOption;

  private int quantity;
  private LocalDateTime orderDateTime;
  private String message;

  public Order() {}

  public Order(ProductOption productOption, int quantity, String message) {
    this.productOption = productOption;
    this.quantity = quantity;
    this.orderDateTime = LocalDateTime.now();
    this.message = message;
  }

  public Long getId() {
    return id;
  }

  public ProductOption getProductOption() {
    return productOption;
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
}