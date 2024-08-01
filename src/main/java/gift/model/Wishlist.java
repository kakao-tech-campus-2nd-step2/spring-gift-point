package gift.model;

import jakarta.persistence.*;

@Entity
@Table(name = "wishlist")
public class Wishlist {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "option_id", nullable = false)
  private Long optionId;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  private Member member;

  @ManyToOne
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public Wishlist() {}

  public Wishlist(Product product, Member member, Long optionId) {
    this.product = product;
    this.member = member;
    this.optionId = optionId;
  }

  public Long getId() {
    return id;
  }

  public Long getOptionId() {
    return optionId;
  }

  public Member getMember() {
    return member;
  }

  public Product getProduct() {
    return product;
  }
}