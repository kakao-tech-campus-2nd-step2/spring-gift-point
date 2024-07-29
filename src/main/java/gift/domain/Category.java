package gift.domain;

import gift.dto.request.CategoryRequestDto;
import gift.utils.TimeStamp;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Category extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    public Category() {
    }

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getColor() {
        return color;
    }
    public List<Product> getProducts() {
        return products;
    }

    public void update(CategoryRequestDto categoryRequestDto){
        this.name = categoryRequestDto.name();
        this.color = categoryRequestDto.color();
    }
}
