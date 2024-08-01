package gift.domain.Menu;

import gift.domain.Category.Category;
import gift.domain.Option.Option;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private MenuName name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id",nullable = false)
    private Category category;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<Option> options;

    public void setCategory(Category category) {
        this.category = category;
    }

    public Menu() {
    }

    public Menu(String name, int price, String imageUrl,Category category,Set<Option> options) {
        this(null, name, price, imageUrl, category, options);
    }

    public Menu(Long id, String name, int price, String imageUrl, Category category, Set<Option> options) {
        this.id = id;
        this.name = new MenuName(name);
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public Menu(Long id, MenuRequest menuRequest,Category category) {
        this(id, menuRequest.name(), menuRequest.price(), menuRequest.imageUrl(), category,new HashSet<Option>());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getMenuName();
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Set<Option> getOptions() {return options;}

    public void update(Menu menu) {
        this.id = menu.id;
        this.name = menu.name;
        this.price = menu.price;
        this.imageUrl = menu.imageUrl;
        this.category = menu.category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Category getCategory() {
        return category;
    }
}
