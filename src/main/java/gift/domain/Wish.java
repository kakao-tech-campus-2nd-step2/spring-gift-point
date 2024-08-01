package gift.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @NotNull
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createdDate;

    protected Wish() {
    }

    private Wish(WishBuilder builder) {
        this.id = builder.id;
        this.member = builder.member;
        this.product = builder.product;
        this.createdDate = builder.createdDate;
    }

    @PrePersist
    protected void onCreate() {
        this.createdDate = LocalDateTime.now();
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

    public static class WishBuilder {
        private Long id;
        private Member member;
        private Product product;
        private LocalDateTime createdDate;

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

        public WishBuilder createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return this;
        }

        public Wish build() {
            return new Wish(this);
        }
    }
}
