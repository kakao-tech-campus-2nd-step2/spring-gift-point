package gift.model;

import jakarta.persistence.*;
import java.util.regex.Pattern;

@Entity
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 50)
  private String name;

  @Column(name = "quantity", nullable = false)
  private int quantity;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  protected Option() {}

  public Option(String name, int quantity, Product product) {
    setName(name);
    setQuantity(quantity);
    setProduct(product);
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    if (!isValidName(name)) {
      throw new IllegalArgumentException("Invalid option name");
    }
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    if (quantity < 1 || quantity >= 100_000_000) {
      throw new IllegalArgumentException("Invalid quantity");
    }
    this.quantity = quantity;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  private boolean isValidName(String name) {
    if (name.length() > 50) return false;
    return Pattern.matches("^[\\p{L}\\p{N}\\s\\(\\)\\[\\]\\+\\-\\&\\/_]+$", name);
  }
  public void subtractQuantity(int quantity) {
    if (this.quantity < quantity) {
      throw new IllegalArgumentException("재고가 부족합니다.");
    }
    this.quantity -= quantity;
  }

}
