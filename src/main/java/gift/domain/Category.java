package gift.domain;

import gift.domain.base.BaseEntity;
import gift.domain.base.BaseTimeEntity;
import gift.domain.vo.Color;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import java.net.URL;
import org.hibernate.annotations.ColumnDefault;

@Entity
public class Category extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @ColumnDefault("'https://gift-s3.s3.ap-northeast-2.amazonaws.com/default-image.png'")
    private URL imageUrl;

    @Column(nullable = false)
    private Color color;

    protected Category() {
    }

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private String name;
        private String description;
        private URL imageUrl;
        private Color color;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder color(Color color) {
            this.color = color;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Category build() {
            return new Category(this);
        }
    }

    private Category(Builder builder) {
        super(builder);
        name = builder.name;
        description = builder.description;
        imageUrl = builder.imageUrl;
        color = builder.color;
    }

    public Category update(Category category) {
        name = category.name;
        description = category.description;
        imageUrl = category.imageUrl;
        color = category.color;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public Color getColor() {
        return color;
    }
}