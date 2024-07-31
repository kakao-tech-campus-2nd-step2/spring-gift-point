package gift.DTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wish_list")
public class WishList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long wishId;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "product_id")
  private Product product;

  protected WishList() {
  }

  public WishList(Long wishId, Product product) {
    this.wishId = wishId;
    this.product = product;
  }

  public WishList(Product product) {
    this.product = product;
  }


  public Long getWishId() {
    return this.wishId;
  }

  public Product getProduct() {
    return this.product;
  }

}
