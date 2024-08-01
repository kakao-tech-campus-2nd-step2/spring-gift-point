package gift.wish.entity;

import gift.product.entity.Product;
import gift.user.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Wish {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @NotNull
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne
  @NotNull
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }
}


