package gift.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.URL;

@Entity
@Table
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "카테고리 이름은 필수입니다")
    @Column(nullable = false, unique = true)
    private String name;

    @NotBlank(message = "색상 코드는 필수입니다")
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$", message = "유효한 색상 코드가 아닙니다")
    @Column(nullable = false)
    private String color;

    @NotNull
    @URL(message = "유효한 URL 형식이 아닙니다")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    private String description;

    protected Category() {}

    public Category(Long id, String name, String color, String imageUrl, String description) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
