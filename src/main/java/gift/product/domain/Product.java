package gift.product.domain;

import gift.category.domain.Category;
import gift.option.domain.Option;
import gift.product.dto.ProductRequestDto;
import gift.wish.domain.Wish;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductName name;
    @Embedded
    private ProductPrice price;
    @Embedded
    private ImageUrl imageUrl;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<Wish> wishes = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.PERSIST, orphanRemoval = true)
    List<Option> options = new ArrayList<>();

    // product 를 가져올 때, category 를 함께 가져와야 함으로, EAGER 로 가져온다
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Product() {
    }

    public Product(Long id, ProductName name, ProductPrice price, ImageUrl imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Product(Long id, ProductName name, ProductPrice price, ImageUrl imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(Long id, ProductRequestDto productRequestDto) {
        this.id = id;
        this.name = productRequestDto.name();
        this.price = productRequestDto.price();
        this.imageUrl = productRequestDto.imageUrl();
    }


    public Long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public ImageUrl getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public boolean checkNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Product item = (Product) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
