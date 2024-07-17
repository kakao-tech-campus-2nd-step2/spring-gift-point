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
    private String description;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    public Category() {}

    public Long getId() {return id;}

    public String getName() {return name;}

    public String getDescription() {return description;}

    public String getImageUrl() {return imageUrl;}

    public void update(String id, String name, String description, String imageUrl) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
    }

}
