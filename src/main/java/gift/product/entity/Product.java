package gift.product.entity;

import gift.category.entity.Category;
import gift.product.entity.embedded.Name;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Embedded
    @AttributeOverride(
        name = "value",
        column = @Column(name = "name", nullable = false)
    )
    private Name name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @JoinColumn(name = "category", nullable = false)
    @ManyToOne
    private Category category;

    public long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    protected Product() {
    }

    public Product(
        long id,
        String name,
        int price,
        String imageUrl,
        Category category
    ) {
        this.id = id;
        this.name = new Name(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void update(
        String name,
        int price,
        String imageUrl,
        Category category
    ) {
        this.name.update(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product product) {
            return this.id == product.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }
}
