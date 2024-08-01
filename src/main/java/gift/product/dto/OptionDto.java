package gift.product.dto;

import gift.product.entity.Option;
import gift.product.entity.Product;
import jakarta.persistence.Column;
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

public class OptionDto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Size(min = 1, max = 50)
  @Pattern(regexp = "^[\\w\\s가-힣\\(\\)\\[\\]\\+\\-\\&\\/]*$", message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용 가능합니다.")
  @Column(nullable = false)
  private String name;

  @Min(1)
  @Max(99999999)
  @Column(nullable = false)
  private int quantity;

  public static OptionDto toDto(Option option) {
    return new OptionDto(
        option.getId(), option.getName(), option.getQuantity()
    );
  }

  public static Option toEntity(OptionDto optionDto, Product product) {
    return new Option(optionDto.getId(), product, optionDto.getName(), optionDto.getQuantity());
  }

  public OptionDto(Long id, String name, int quantity) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
