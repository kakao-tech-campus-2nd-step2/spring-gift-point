package gift.product.entity;
import gift.exception.QuantityException;
import gift.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Option extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Size(min = 1, max = 50)
  @Pattern(regexp = "^[\\w\\s가-힣\\(\\)\\[\\]\\+\\-\\&\\/]*$", message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용 가능합니다.")
  @Column(nullable = false)
  private String name;

  @Min(1)
  @Max(99999999)
  @Column(nullable = false)
  private int quantity;

  public void subtractQuantity(int quantity){
    if (quantity <= 0) {
      throw new QuantityException("차감할 수량은 0보다 커야 합니다.");
    }
    if (this.quantity < quantity) {
      throw new QuantityException("차감할 수량이 현재 수량보다 많습니다.");
    }
    this.quantity -= quantity;
  }

  public void update(String name, int quantity) {
    this.name = name;
    this.quantity = quantity;
  }
}
