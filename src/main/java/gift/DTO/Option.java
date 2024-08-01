package gift.DTO;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table
public class Option {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Size(min = 1, max = 50, message = "가능한 글자 수는 1~15입니다.")
  @Pattern(regexp = "^[\\w\\s()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다")
  @Column(nullable = false, unique = true)
  private String name;
  @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
  @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
  @Column(nullable = false)
  private int quantity;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @OneToMany(mappedBy = "option")
  private List<Orders> orders;

  protected Option() {
  }

  public Option(Long id, String name, int quantity, Product product) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.product = product;
  }

  public Option(String name, int quantity, Product product) {
    this.name = name;
    this.quantity = quantity;
    this.product = product;
  }

  public Long getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public int getQuantity() {
    return this.quantity;
  }

  public Product getProduct() {
    return this.product;
  }

  public void subtract(int amount) {
    this.quantity -= amount;
  }
}
