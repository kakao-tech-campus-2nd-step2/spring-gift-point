package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category", uniqueConstraints = @UniqueConstraint(name = "uk_category", columnNames = "name"))
public class Category {

    @Getter
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank
    @Size(max = 7)
    @Column(nullable = false)
    private String color;

    @NotBlank
    @Size(max = 255)
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Size(max = 255)
    private String description;

    public Category() {}

    public Category(String name, String color, String imageUrl, String description) {
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public @NotBlank @Size(max = 255) String getName() {
        return name;
    }

    public void setName(@NotBlank @Size(max = 255) String name) {
        this.name = name;
    }

    public @NotBlank @Size(max = 7) String getColor() {
        return color;
    }

    public void setColor(@NotBlank @Size(max = 7) String color) {
        this.color = color;
    }

    public @NotBlank @Size(max = 255) String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(@NotBlank @Size(max = 255) String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public @Size(max = 255) String getDescription() {
        return description;
    }

    public void setDescription(@Size(max = 255) String description) {
        this.description = description;
    }
}