package gift.domain;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "wishList")
@EntityListeners(AuditingEntityListener.class)
public class WishProduct extends BaseEntity{
    @ManyToOne
    @JoinColumn(
            name = "user_id",
            foreignKey = @ForeignKey(name = "fk_wish_product_id_ref_product_id"),
            nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(
            name = "product_id",
            foreignKey = @ForeignKey(name = "fk_wish_user_id_ref_user_id"),
            nullable = false)
    private Product product;
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createDate;


    protected WishProduct(){
        super();
    }

    public WishProduct(User user, Product product) {
        this.user = user;
        this.product = product;
    }

    public Long getId(){
        return super.getId();
    }
    public User getUser() {
        return user;
    }

    public Product getProduct() {
        return product;
    }
    public String getCreateDate(){
        return createDate.toString();
    }
}
