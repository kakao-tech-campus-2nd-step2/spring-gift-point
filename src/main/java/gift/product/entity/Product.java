package gift.product.entity;

import gift.category.entity.Category;
import gift.product.dto.ProductRequestDto;
import gift.util.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
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
public class Product extends BaseEntity {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "이름을 입력해야 합니다.")
  @Size(max = 15, message = "이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
  @Pattern(regexp = "^[a-zA-Z0-9가-힣 \\[\\]\\(\\)\\+\\-\\&\\/\\_]*$", message = "특수 문자는 ( ), [ ], +, -, &, /, _ 만 사용할 수 있습니다.")
  @Pattern(regexp = "^(?!.*카카오).*$", message = "이름에 '카카오' 포함은 불가합니다. 예외적 사용 시 담당 MD의 사전 승인이 필수입니다.")
  @Column(name = "name", nullable = false, unique = true)
  private String name;

  @NotNull(message = "가격을 입력해야 합니다.")
  @Min(value = 1, message = "가격은 양수여야 합니다.")
  @Column(name = "price", nullable = false)
  private int price;

  @NotNull(message = "이미지 url을 입력해야 합니다.")
  @Column(name = "image_url", nullable = false)
  private String imageUrl;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull(message = "카테고리를 입력해야 합니다.")
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Size(min = 1)
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Option> options;


  public void addOption(Option option) {
    if (this.options == null){
      this.options = new ArrayList<>();
    }

    options.add(option);
    option.setProduct(this);
  }

  public void removeOption(Option option) {
    this.options.clear();
  }

  public void update(ProductRequestDto productRequestDto, Category category) {
    this.name = productRequestDto.name();
    this.price = productRequestDto.price();
    this.imageUrl = productRequestDto.imageUrl();
    this.category = category;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public int getPrice() {
    return price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public Category getCategory() {
    return category;
  }

  public List<Option> getOptions() {
    return options;
  }
}
