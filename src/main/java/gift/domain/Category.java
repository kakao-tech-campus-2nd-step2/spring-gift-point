package gift.domain;

import jakarta.persistence.*;

@Entity
@Table(name="categories")
public class Category extends BaseEntity{
    @Column(name = "name", nullable = false)
    String name;

    protected Category() {
        super();
    }

    public Category(String name) {
        this.name = name;
    }

    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public void update(String name){
        this.name = name;
    }
}
