package gift.domain;

import gift.domain.base.BaseEntity;
import gift.domain.base.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.net.URL;
import java.util.List;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@DynamicInsert
@Entity
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    @ColumnDefault("'https://gift-s3.s3.ap-northeast-2.amazonaws.com/default-image.png'")
    private URL imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Category category;

    @OneToMany(mappedBy = "productId", fetch = FetchType.LAZY)
    private List<ProductOption> productOptions;

    protected Product() {
    }

    public static class Builder extends BaseTimeEntity.Builder<Builder> {

        private String name;
        private Integer price;
        private URL imageUrl;
        private Category category;
        private List<ProductOption> productOptions;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(Integer price) {
            this.price = price;
            return this;
        }

        public Builder imageUrl(URL imageUrl) {
            this.imageUrl = imageUrl;
            return this;
        }

        public Builder category(Category category) {
            this.category = category;
            return this;
        }

        public Builder productOptions(List<ProductOption> productOptions) {
            this.productOptions = productOptions;
            return this;
        }

        @Override
        protected Builder self() {
            return this;
        }

        @Override
        public Product build() {
            return new Product(this);
        }
    }

    private Product(Builder builder) {
        super(builder);
        name = builder.name;
        price = builder.price;
        imageUrl = builder.imageUrl;
        category = builder.category;
        productOptions = builder.productOptions;
        validateProductOptionsPresence(productOptions);
    }

    public Product updateBasicInfo(String name, Integer price, URL imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        return this;
    }

    private void validateProductOptionsPresence(List<ProductOption> productOptions) {
        if (productOptions == null || productOptions.isEmpty()) {
            throw new IllegalArgumentException("상품 옵션은 최소 1개 이상이어야 합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public URL getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public List<ProductOption> getProductOptions() {
        return productOptions;
    }
}
