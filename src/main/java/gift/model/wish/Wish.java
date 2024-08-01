package gift.model.wish;

import gift.model.member.Member;
import gift.model.product.Product;
import jakarta.persistence.*;

@Entity
@Table(name = "wishes")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
  
    @ManyToOne
    @JoinColumn(name = "products_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "members_id", nullable = false)
    private Member member;

    protected Wish(){
    }

    public Wish(Product product, Member member){
        this.product = product;
        this.member = member;
    }

    public void updateWish(Wish wish){
        this.product = wish.getProduct();
        this.member = wish.getMember();
    }
  
    public Long getId(){
        return id;
    }

    public Product getProduct(){
        return product;
    }

    public Member getMember(){
        return member;
    }
}
