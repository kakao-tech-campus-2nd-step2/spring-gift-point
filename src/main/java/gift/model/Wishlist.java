package gift.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private SiteUser user;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private boolean hidden = false;

    @ManyToMany
    @JoinTable(
        name = "wishlist_options",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> options;


    public Wishlist() {}

    public Wishlist(SiteUser user, Product product, int quantity, int price) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public SiteUser getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getPrice() {
        return price;
    }

    public boolean isHidden() {
        return hidden;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(SiteUser user) {
        this.user = user;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }
}
