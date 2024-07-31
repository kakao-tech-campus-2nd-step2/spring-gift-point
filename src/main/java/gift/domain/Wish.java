package gift.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name="wishes")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name="created_date", nullable = false)
    private LocalDateTime createdDate;

    protected Wish () {
    }

    public Wish(Long id, Member member, Product product, LocalDateTime createdDate) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
    }

    public Wish(Member member, Product product, LocalDateTime createdDate) {
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
        if(this.member != null){
            this.member.addWish(this);
        }
        if(this.product != null){
            this.product.addWish(this);
        }
    }

    public void remove(){
        if(this.member != null){
            this.member.removeWish(this);
        }
        if(this.product != null){
            this.product.removeWish(this);
        }
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wish wish = (Wish) o;
        return Objects.equals(id, wish.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}