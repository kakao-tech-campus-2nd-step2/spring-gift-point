package gift.product.entity;

import gift.category.entity.Category;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
public class Product {


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

  @ManyToOne
  @NotNull(message = "카테고리를 입력해야 합니다.")
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Size(min = 1)
  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Option> options = new ArrayList<>();

  public Product(Long id, String name, int price, String imageUrl, Category category,
      List<Option> options) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.category = category;
    this.options = options;
  }

  public Product() {

  }

  public void addOption(Option option) {
    options.add(option);
    option.setProduct(this);
  }

  public void removeOption(Option option) {
    options.remove(option);
    option.setProduct(null);
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

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public List<Option> getOptions() {
    return options;
  }

  public void setOptions(List<Option> options) {
    this.options = options;
  }
}
