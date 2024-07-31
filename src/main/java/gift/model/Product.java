package gift.model;

import jakarta.persistence.*;
import gift.repository.CategoryRepository;
import gift.exception.CustomNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false, length = 15)
  private String name;

  @Column(name = "price", nullable = false)
  private int price;

  @Column(name = "image_url", nullable = false, length = 255)
  private String imageUrl;

  @ManyToOne
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Option> options = new ArrayList<>();


  protected Product() {
  }

  public Product(String name, int price, String imageUrl, Category category) {
    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.category = category;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public int getPrice() {
    return price;
  }

  public Category getCategory() {
    return category;
  }
  public List<Option> getOptions() {
    return options;
  }

  public void removeOption(Option option) {
    options.remove(option);
    option.setProduct(null);
  }
  public void updateFromDto(String name, int price, String imageUrl, Category category) {

    if (name == null || name.isEmpty() || name.length()  > 15) {
      throw new IllegalArgumentException("이름 필드를 비워둘 수 없습니다.");
    }

    if (price < 0) {
      throw new IllegalArgumentException("- 값은 유효하지 않은 값입니다.");
    }

    if (imageUrl == null || imageUrl.isEmpty()) {
      throw new IllegalArgumentException("Invalid image URL.");
    }

    if (category == null) {
      throw new IllegalArgumentException("Category 필드를 비워둘 수 없습니다.");
    }

    this.name = name;
    this.price = price;
    this.imageUrl = imageUrl;
    this.category = category;
  }
  }
