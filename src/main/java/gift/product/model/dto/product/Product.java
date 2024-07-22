package gift.product.model.dto.product;


import gift.BaseTimeEntity;
import gift.category.model.dto.Category;
import gift.user.model.dto.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "product")
@SQLDelete(sql = "UPDATE product SET deletion_date = CURRENT_TIMESTAMP WHERE id = ?")
@SQLRestriction("deletion_date IS NULL")
public class Product extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;    // 선물의 이름

    @Column(nullable = false)
    private int price = 0;      // 선물의 가격

    @Column(name = "image_url")
    private String imageUrl; // 선물 이미지 URL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser seller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Product(String name, int price, String imageUrl, AppUser seller, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.seller = seller;
        this.category = category;
    }

    public Product() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public AppUser getSeller() {
        return seller;
    }

    public Category getCategory() {
        return category;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void updateProduct(String name, int price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public boolean isOwner(Long id) {
        return seller.getId().equals(id);
    }
}