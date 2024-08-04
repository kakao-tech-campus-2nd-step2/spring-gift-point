package gift.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.annotation.CreatedDate;

@Entity
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRODUCT_ID", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private SiteUser siteUser;

    @Column(nullable = false)
    private int count;

    @Column(nullable = false)
    private boolean hidden;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @ManyToMany
    @JoinTable(
        name = "wishlist_options",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<Option> options;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public SiteUser getSiteUser() {
        return siteUser;
    }

    public int getCount() {
        return count;
    }

    public boolean isHidden() {
        return hidden;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSiteUser(SiteUser siteUser) {
        this.siteUser = siteUser;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
}
