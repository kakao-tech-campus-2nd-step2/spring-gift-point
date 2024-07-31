package gift.domain;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long category_id;

    @Column(nullable = false, unique = true)
    private String name;

    private String color;
    private String image_url;
    private String description;

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Category(String name) {
        this.name = name;
    }

    public Category() {

    }

    public Long getId() {
        return category_id;
    }
    public String getName(){
        return name;
    }
    public String getColor() {
        return color;
    }
    public String getImage_url() {
        return image_url;
    }
    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
}
