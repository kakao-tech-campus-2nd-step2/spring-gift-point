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

    @Column(nullable = false, columnDefinition = "integer COMMENT '장바구니에 담은 상품 개수'")
    private int amount;

    protected Wish(){
    }

    public Wish(Product product, Member member, int amount){
        this.product = product;
        this.member = member;
        this.amount = amount;
    }

    public void updateWish(Wish wish){
        this.product = wish.getProduct();
        this.member = wish.getMember();
        this.amount = wish.getAmount();
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

    public int getAmount(){
        return amount;
    }
}
