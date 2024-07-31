package gift.DTO;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Product {

  @OneToMany(mappedBy = "product")
  private final List<WishList> wishlists = new ArrayList<>();
  @OneToMany(mappedBy = "product")
  private final List<Option> options = new ArrayList<>();
  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Size(min = 1, max = 15, message = "가능한 글자 수는 1~15입니다.")
  @Pattern.List({
    @Pattern(regexp = "^[가-힣a-zA-Z0-9()\\[\\]+\\-&/_]*$", message = "유효한 이름이 아닙니다"),
    @Pattern(regexp = "^(?!.*카카오).*$", message = "'카카오' 포함된 경우 담당 MD와 협의가 필요합니다.")
  })
  @Column(nullable = false, unique = true)
  private String name;
  @Min(value = 1)
  @Column(nullable = false)
  private int price;
  @Column(nullable = false)
  private String imageUrl;

  protected Product() {
  }

  public Product(Long id, String name, int price, String imageUrl, Category category) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.category = category;
  }

  public Product(String name, int price, String imageUrl, Category category) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.category = category;
  }

  public Long getId() {
    return this.id;
  }


  public String getName() {
    return this.name;
  }


  public int getPrice() {
    return this.price;
  }


  public String getImageUrl() {
    return this.imageUrl;
  }

  public Category getCategory() {
    return this.category;
  }

  public List<WishList> getWishlists() {
    return wishlists;
  }

  public List<Option> getOptions() {
    return options;
  }
}
