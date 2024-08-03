package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_option")
public class ProductOption {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String value;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public ProductOption(String name, String value, Product product) {
    this.name = name;
    this.value = value;
    this.product = product;
  }

  public ProductOption(Long id, String name, String value, Product product) {
    this.id = id;
    this.name = name;
    this.value = value;
    this.product = product;
  }

  public ProductOption() {}

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getValue() {
    return value;
  }

  public Product getProduct() {
    return product;
  }
}