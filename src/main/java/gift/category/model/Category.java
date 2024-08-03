package gift.category.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gift.product.model.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EqualsAndHashCode
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product> productList;

    @Column(name = "color")
    private String color;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "description")
    private String description;

    public Category() {
    }

    public Category(String name) {
        this.name = name;
        this.productList = new ArrayList<>();
    }

    public Category(String name, List<Product> productList) {
        this.name = name;
        this.productList = productList;
    }

    public Category(Long id, String name, List<Product> productList) {
        this.id = id;
        this.name = name;
        this.productList = productList;
    }

    public Category(String name, List<Product> productList, String color, String imageUrl,
        String description) {
        this(null, name, productList, color, imageUrl, description);
    }

    public Category(Long id, String name, List<Product> productList, String color, String imageUrl,
        String description) {
        this.id = id;
        this.name = name;
        this.productList = productList;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }


    public Category setId(Long id) {
        this.id = id;
        return this;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public Category setProductList(List<Product> productList) {
        this.productList = productList;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Category{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", productList=").append(productList);
        sb.append(", color='").append(color).append('\'');
        sb.append(", imageUrl='").append(imageUrl).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
