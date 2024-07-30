package gift.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "wish")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @NotNull
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected Wish() {
    }

    private Wish(WishBuilder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.product = builder.product;
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

    public static class WishBuilder {
        private Long id;
        private Member member;
        private Product product;

        public WishBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public WishBuilder member(Member member) {
            this.member = member;
            return this;
        }

        public WishBuilder product(Product product) {
            this.product = product;
            return this;
        }

        public Wish build() {
            return new Wish(this);
        }
    }
}
