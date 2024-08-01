package gift.domain.category;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull
    private long id;

    @NotNull
    @Column(unique = true)
    private String name;

    @NotNull
    @Column(length = 7)
    private String color;

    @NotNull
    private String description;

    @NotNull
    private String imageUrl;

    protected Category() {
    }

    public Category(String name, String color, String description, String imageUrl) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void update(String name, String color, String description, String imageUrl) {
        this.name = name;
        this.color = color;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
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

    public String getName() {
        return name;
    }
}
