package gift.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private final Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Product product;

    @Column(nullable = false)
    @CreationTimestamp
    private final LocalDateTime createdDate;

    protected Wish() {
        this(null, null, null, null);
    }

    public Wish(Member member, Product product) {
        this(null, member, product, null);
    }

    public Wish(Long id, Member member, Product product, LocalDateTime createdDate) {
        this.id = id;
        this.member = member;
        this.product = product;
        this.createdDate = createdDate;
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