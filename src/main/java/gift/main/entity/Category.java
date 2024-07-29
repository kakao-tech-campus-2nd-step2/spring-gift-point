package gift.main.entity;

import gift.main.dto.CategoryRequest;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private int uniNumber;

    @Column(nullable = false, unique = true)
    private String name;


    public Category() {
    }

    public Category(CategoryRequest categoryRequest) {
        this.name = categoryRequest.name();
        this.uniNumber = categoryRequest.uniNumber();
    }

    public void updateCategory(CategoryRequest categoryRequest) {
        this.name = categoryRequest.name();
        this.uniNumber = categoryRequest.uniNumber();
    }

    public long getId() {
        return id;
    }

    public int getUniNumber() {
        return uniNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return id == category.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
