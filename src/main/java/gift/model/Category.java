package gift.model;

import gift.exception.InputException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_category",
        columnNames = {"name"}
    )
})
public class Category extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(nullable = false)
    private String name;

    protected Category() {
    }

    public Category(Long id, String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateCategory(String newName) {
        validateName(newName);
        this.name = newName;
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty() || name.length() > 20) {
            throw new InputException("이름을 1~20자 사이로 입력해주세요");
        }
    }


}
