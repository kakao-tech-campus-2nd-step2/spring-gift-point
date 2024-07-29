package gift.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "product")
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false)
    private String url;
    @Column(nullable = false)
    private Integer price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true) //상품에 어떤 옵션들이 있는지 보려면 필요할듯
    private List<Option> options = new ArrayList<>();

    public Product() {
    }

    public Product(String name, Integer price, String url, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

    public Product(Long id, String name, Integer price, String url, Category category, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

    public Product(String name, Integer price, String url) {
        this.name = name;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getUrl() {
        return url;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(String name, Integer price, String url, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.url = url;
        this.category = category;
        this.options = options;
    }

}
