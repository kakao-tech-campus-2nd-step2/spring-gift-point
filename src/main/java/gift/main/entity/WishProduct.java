package gift.main.entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "wish-products")
public class WishProduct {

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = true)
    public Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public WishProduct() {

    }

    public WishProduct(Product product, User user) {
        this.product = product;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public User getUser() {
        return user;
    }

    public void setProductIdToNull() {
        this.product = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WishProduct that = (WishProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
