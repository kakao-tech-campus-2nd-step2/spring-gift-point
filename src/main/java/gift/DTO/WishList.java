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
  private Long id;

  @ManyToOne(cascade = CascadeType.MERGE)
  @JoinColumn(name = "product_id")
  private Product product;

  @ManyToOne
  @JoinColumn(name = "member_id")
  private Member member;

  protected WishList() {
  }

  public WishList(Long id, Member member, Product product) {
    this.id = id;
    this.member = member;
    this.product = product;
  }

  public WishList(Member member, Product product) {
    this.member = member;
    this.product = product;
  }


  public Long getId() {
    return this.id;
  }

  public Member getMember() {
    return this.member;
  }

  public Product getProduct() {
    return this.product;
  }

}
