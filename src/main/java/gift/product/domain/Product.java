package gift.product.domain;

import gift.category.domain.Category;
import gift.exception.type.InvalidProductOptionException;
import gift.exception.type.KakaoInNameException;
import gift.option.domain.Option;
import gift.product.application.command.ProductUpdateCommand;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String name;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, Integer price, String imageUrl, Category category) {
        this(null, name, price, imageUrl, category);
    }

    public Product(Long id, String name, Integer price, String imageUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void addOption(Option option) {
        validateOptionDoesNotExist(option);

        options.add(option);
        option.setProduct(this);
    }

    public void update(Category category, ProductUpdateCommand command) {
        this.name = command.name();
        this.price = command.price();
        this.imageUrl = command.imageUrl();
        this.category = category;
    }

    public void validateKakaoInProductName() {
        if (name.contains("카카오")) {
            throw new KakaoInNameException("카카오가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다.");
        }
    }

    public void validateHasAtLeastOneOption() {
        if (options.isEmpty()) {
            throw new InvalidProductOptionException("상품은 최소 1개 이상의 옵션을 가져야 합니다.");
        }
    }

    private void validateOptionDoesNotExist(Option option) {
        options.stream().filter(o -> o.getName().equals(option.getName()))
                .findAny()
                .ifPresent(o -> {
                    throw new InvalidProductOptionException("이미 존재하는 옵션입니다.");
                });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
