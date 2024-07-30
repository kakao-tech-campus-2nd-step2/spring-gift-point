package gift.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(name = "image_url")
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "product_id")
    private List<Option> options = new ArrayList<>();

    public Product() {
    }

    public Product(Long id, String name, int price, String imageUrl, Category category, List<Option> options) {
        if (name.contains("카카오") || name.equalsIgnoreCase("kakao")) {
            throw new IllegalArgumentException("\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에 사용 가능합니다.");
        }
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        validateAndSetOptions(options);
    }

    public Product(String name, int price, String imageUrl, Category category, List<Option> options) {
        if (name.contains("카카오") || name.equalsIgnoreCase("kakao")) {
            throw new IllegalArgumentException("\"카카오\"가 포함된 문구는 담당 MD와 협의한 경우에 사용 가능합니다.");
        }
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        validateAndSetOptions(options);
    }

    public void validateAndSetOptions(List<Option> options) {
        if (options.isEmpty()) {
            throw new IllegalArgumentException("상품에는 최소 하나의 옵션이 있어야 합니다.");
        }
        Set<String> optionNames = new HashSet<>();
        this.options = options.stream()
                .filter(option -> validateOption(option, optionNames))
                .collect(Collectors.toList());
    }

    private boolean validateOption(Option option, Set<String> optionNames) {
        if (option.getName().length() >= 50 || option.getName().length() <= 0) {
            throw new IllegalArgumentException("옵션 이름은 최대 50자까지 입력 가능합니다.");
        }
        if (!optionNames.add(option.getName())) {
            throw new IllegalArgumentException("옵션 이름이 중복됩니다: " + option.getName());
        }
        if (option.getQuantity() <= 0 || option.getQuantity() > 99999999) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.");
        }
        return true;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public List<Option> getOptions() {
        return options;
    }
}
