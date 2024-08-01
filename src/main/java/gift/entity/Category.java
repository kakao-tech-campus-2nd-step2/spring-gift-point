package gift.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name = "category")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "BIGINT")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String name;

    @NotNull
    @Column(name = "color", nullable = false, columnDefinition = "VARCHAR(50)")
    private String color;

    @NotNull
    @Column(name = "img_url", nullable = false, columnDefinition = "VARCHAR(255)")
    private String imgUrl;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("category")
    private List<Product> products;

    protected Category() {}

    public Category(String name, String color, String imgUrl, String description) {
        this.name = name;
        this.color = color;
        this.imgUrl = imgUrl;
        this.description = description;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public String getImgUrl() { return imgUrl; }
    public String getDescription() { return description; }
    public List<Product> getProducts() { return products; }
}