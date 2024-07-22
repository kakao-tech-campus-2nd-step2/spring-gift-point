package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "name must not be blank")
    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false)
    private String color;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @Column(nullable = false)
    private String description;

    public Category() {}

    public Long getId() {return id;}

    public String getName() {return name;}

    public String getColor() {return color;}

    public String getImageUrl() {return imageUrl;}

    public String getDescription() {return description;}

    public void update(String name, String color,  String imageUrl, String description) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.color = color;
    }

}
