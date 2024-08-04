package gift.vo;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_wish_member_id_ref_member_id"))
    private Member member;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"))
    private Product product;

    @NotNull
    private LocalDateTime createdDate = LocalDateTime.now();

    public Wish() {}

    public Wish(Member member, Product product) {
        this(null, member, product);
    }

    public Wish(Long id, Member member, Product product) {
        this.id = id;
        this.member = member;
        this.product = product;
    }

    public @NotNull LocalDateTime getCreatedDate() {
        return createdDate;
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
}
