package gift.category;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
}
