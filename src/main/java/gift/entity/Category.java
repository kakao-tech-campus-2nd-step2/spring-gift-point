package gift.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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

    private Category(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.color = builder.color;
        this.imageUrl = builder.imageUrl;
        this.description = builder.description;
    }

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

    public static class Builder {
        private Long id;
        private String name;
        private String color;
        private String imageUrl;
        private String description;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }
        public Builder name(String name) {
            this.name = name;
            return this;
        }
        public Builder color(String color) {
            this.color = color;
            return this;
        }
        public Builder imageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }
        public Builder description(String description) {
            this.description = description;
            return this;
        }
        public Category build() {
            return new Category(this);
        }
    }

}
