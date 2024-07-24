package gift.model;

import gift.exceptionAdvisor.exceptions.GiftException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import org.springframework.http.HttpStatus;

@Entity
@Table(name = "PRODUCT_TABLE")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PRODUCT_ID")
    private Long id;

    @Column(name = "PRODUCT_NAME", length = 15, nullable = false)
    private String name;

    @Column(name = "PRODUCT_PRICE", nullable = false)
    private Integer price;

    @Column(name = "PRODUCT_IMAGE_URL", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_ID", referencedColumnName = "CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<GiftOption> giftOptionList = new ArrayList<>();



    public Product(Long id, String name, Integer price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product() {

    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void addGiftOption(GiftOption giftOption) {
        giftOptionList.add(giftOption);
    }

    public void deleteGiftOption(GiftOption giftOption) {
        if (giftOptionList.size()<=1) {
            throw new GiftException("옵션은 1개 이상이여야합니다.", HttpStatus.BAD_REQUEST);
            //TODO : exception 정리하기
        }
        giftOptionList.remove(giftOption);
    }

    //getter
    public Long getId() {
        return id;
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

    public Long getCategoryId() {
        return category.getId();
    }

    public List<GiftOption> getGiftOptionList() {
        return giftOptionList;
    }
}
