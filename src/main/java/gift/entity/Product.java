package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.BlankContentException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Size;
import java.util.Objects;
import java.util.regex.Pattern;


@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected Product() { }

    public Product(String name, Integer price, String imageUrl, Category category) {
        validateName(name);
        validatePrice(price);
        validateImageUrl(imageUrl);

        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Integer getPrice() {
        return price;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public Category getCategory() { return category; }

    public void changeProduct(String name, Integer price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BlankContentException("상품 이름을 입력해주세요.");
        }
    }

    private void validatePrice(Integer price) {
        if (price == null) {
            throw new BlankContentException("가격을 입력해주세요.");
        }
        if (price < 0) {
            throw new BadRequestException("가격은 0 이상, 2147483647 이하이여야 합니다.");
        }
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new BlankContentException("이미지 URL을 입력해주세요.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(price, product.price) &&
                Objects.equals(imageUrl, product.imageUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}