package gift.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OptionDto {

  private Long id;

  @Size(min = 1, max = 50, message = "가능한 글자 수는 1~15입니다.")
  @Pattern(regexp = "^[\\w\\s()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다")
  private String name;

  @Min(value = 1, message = "옵션 수량은 최소 1개 이상이어야 합니다.")
  @Max(value = 99999999, message = "옵션 수량은 1억 개 미만이어야 합니다.")
  private int quantity;

  private ProductDto productDto;

  public OptionDto() {
  }

  public OptionDto(Long id, String name, int quantity, ProductDto productDto) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.productDto = productDto;
  }

  public OptionDto(String name, int quantity, ProductDto productDto) {
    this.name = name;
    this.quantity = quantity;
    this.productDto = productDto;
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

  public ProductDto getProductDto() {
    return this.productDto;
  }
}
