package gift.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 15)
    private String name;
    @Column(nullable = false)
    private int price;
    @Column(name = "imageurl", nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 10)
    private List<Option> options = new ArrayList<>();

    protected Product() {

    }

    public Product(String name, int price, String imageUrl, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public Product(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
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

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }

    public void addOption(Option newOption) {
        for(Option option: options) {
            if(option.getName().equals(newOption.getName())){
                throw new IllegalStateException("옵션에 중복 이름 안됨");
            }
        }
        options.add(newOption);
        newOption.setProduct(this);
    }

    public void updateOption(Option updatedOption) {
        options.stream()
            .filter(option -> option.getId().equals(updatedOption.getId()))
            .findFirst()
            .ifPresentOrElse(
                option -> {
                    options.set(options.indexOf(option), updatedOption);
                },
                () -> {
                    throw new NoSuchElementException("해당 ID의 옵션 없음: " + updatedOption.getId());
                }
            );
    }

    public void deleteOption(Long optionId) {
        options.removeIf(option -> option.getId().equals(optionId));
    }

    public List<Option> getOptions() {
        return options;
    }

    public Option getOptionByName(String name) {
        return options.stream()
            .filter(option -> option.getName().equals(name))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 이름의 옵션 없음: " + name));
    }

    public Option getOptionById(Long optionId) {
        return options.stream()
            .filter(option -> option.getId().equals(optionId))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("해당 ID의 옵션 없음: " + optionId));
    }

    public void subtractOptionQuantity(Long optionId, int amount) {
        Option option = getOptionById(optionId);
        option.subtract(amount);
    }
}