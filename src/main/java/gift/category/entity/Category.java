package gift.category.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
@Schema(description = "카테고리")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "카테고리 id")
    private long id;

    @Column(unique = true, nullable = false)
    @Schema(description = "카테고리명")
    private String name;

    public String getName() {
        return name;
    }

    protected Category() {
    }

    public Category(long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Category category) {
            return id == category.id
                   && Objects.equals(name, category.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
