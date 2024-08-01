package gift.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish")
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private LocalDateTime createdDate;


    protected Wish() {
        this.createdDate = LocalDateTime.now();
    }

    public Wish(Long id, Member member, Product product, LocalDateTime createdDate) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
    }

    public Wish(Member member, Product product) {
        this.member = member;
        this.product = product;
        this.createdDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}
