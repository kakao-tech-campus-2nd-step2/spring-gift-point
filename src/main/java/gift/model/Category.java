package gift.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="CATEGORY_TABLE")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "CATEGORY_NAME",unique = true, nullable = false)
    private String name;

    @Column(name = "CATEGORY_COLOR",length = 7, nullable = false)
    private String color;

    @Column(name = "CATEGORY_DESCRIPTION")
    private String description;

    @Column(name = "CATEGORY_IMAGE_URL")
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Product> productList = new ArrayList<>();

    public Category() {
    }

    public Category(Long id, String name, String color, String description, String imageUrl) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    //getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
