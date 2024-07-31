package gift.domain.model.entity;


import gift.util.TimeStamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "wishes",
    uniqueConstraints = @UniqueConstraint(name = "UK_USER_PRODUCT"
        , columnNames = {"user_id", "product_id"})
)

public class Wish extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer count = 1;

    protected Wish() {
    }

    public Wish(User user, Product product, Integer count) {
        this.user = user;
        this.product = product;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public Integer getCount() {
        return count;
    }

    public void updateWish(User user, Product product, Integer count) {
        this.user = user;
        this.product = product;
        this.count = count;
    }
}