package gift.domain.wishlist.entity;

import gift.domain.member.entity.Member;
import gift.domain.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column
    private LocalDateTime createdDate;

    protected Wish() {
    }

    public Wish(Member member, Product product, LocalDateTime createdDate) {
        this(null, member, product, createdDate);
    }

    public Wish(Long id, Member member, Product product, LocalDateTime createdDate) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;

        member.getWishList().add(this);
    }

    public Long getId() {
        return id;
    }

    public Member getMember() {
        return member;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
