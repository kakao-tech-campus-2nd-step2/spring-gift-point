package gift.domain.Category;

import gift.domain.Menu.Menu;
import jakarta.persistence.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "color", nullable = false)
    private String color;
    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @OneToMany(mappedBy = "category")
    private List<Menu> menus;

    public List<Menu> getMenus(){
        return this.menus;
    }

    public Long getId(){
        return this.id;
    }

    public Category(String name) {
        this.name = name;
        this.menus = new LinkedList<>();
    }

    public Category(Long id, String name, LinkedList<Menu> menus) {
        this.id = id;
        this.name = name;
        this.menus = menus;
    }

    public Category(Long id, String name) {
        this(id, name, new LinkedList<Menu>());
    }

    public Category() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(name, category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
