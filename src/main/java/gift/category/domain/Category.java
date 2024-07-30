package gift.category.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.category.dto.CategoryRequestDto;
import gift.product.domain.Product;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private CategoryName name;
    @Embedded
    private CategoryColor color;
    @Embedded
    private CategoryImageUrl imageUrl;
    @Embedded
    private CategoryDescription description;

    // 카테고리를 삭제했다고 상품이 삭제되면 안 되므로, orphanRemoval = false 를 명시적으로 주었다
    @JsonIgnore
    @OneToMany(mappedBy = "category", cascade = CascadeType.PERSIST, orphanRemoval = false)
    List<Product> products = new ArrayList<>();

    // JDBC 에서 엔티티 클래스를 인스턴스화할 때 반드시 기본 생성자와 파라미터 생성자가 필요하다
    public Category() {
    }

    public Category(Long id, CategoryName name, CategoryColor color, CategoryImageUrl imageUrl, CategoryDescription description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Category(Long id, CategoryRequestDto categoryRequestDto) {
        this.id = id;
        this.name = categoryRequestDto.name();
        this.color = categoryRequestDto.color();
        this.imageUrl = categoryRequestDto.imageUrl();
    }

    public Long getId() {
        return id;
    }

    public CategoryName getName() {
        return name;
    }

    public CategoryColor getColor() {
        return color;
    }

    public CategoryImageUrl getImageUrl() {
        return imageUrl;
    }

    public CategoryDescription getDescription() {
        return description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public boolean checkNew() {
        return id == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o))
            return false;
        Category item = (Category) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
