package gift.model;

import gift.validation.ValidProductName;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "이름을 1자 이상 입력하세요")
    @Size(max = 15, message = "이름은 최대 15자까지 입력 가능합니다")
    @Pattern(regexp = "^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣()\\[\\]+\\-&/_\\s]*$", message = "사용 불가능한 특수 문자가 포함되어 있습니다")
    @ValidProductName
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @NotNull
    @URL(message = "유효한 URL 형식이 아닙니다")
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;

    public Product(){}

    public Product(Long id, String name, int price, String imageUrl, Category category, List<Option> options) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options = options;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    public int getPrice(){
        return price;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public Category getCategory(){
        return category;
    }

    public List<Option> getOptions(){
        return options;
    }

    public static Product createWithId(Long id, String name, int price, String imageUrl, Category category, List<Option> options) {
        return new Product(id, name, price, imageUrl, category, options);
    }

    public void validateOptions() {
        if (options != null) {
            Set<String> optionNames = new HashSet<>();
            for (Option option : options) {
                if (!optionNames.add(option.getName())) {
                    throw new IllegalArgumentException("Duplicate option name: " + option.getName());
                }
            }
        }
    }

}