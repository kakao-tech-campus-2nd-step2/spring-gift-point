package gift.entity;


import gift.exception.MinimumOptionException;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Schema(description = "Product entity representing a product")
@Entity
@Table(name = "products")
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Schema(description = "Unique identifier of the product", example = "1")
    private Long id;
    @NotNull
    @Size(max = 15, message = "상품 이름은 공백을 포함하여 최대 15자까지 입력할 수 있습니다.")
    @Schema(description = "Name of the product", example = "Product 1")
    private String name;
    @NotNull
    @Schema(description = "Price of the product", example = "100")
    private Integer price;
    @Schema(description = "Image URL of the product", example = "image1.jpg")
    private String img;
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @Schema(description = "List of wishes associated with this product")
    private List<Wish> wishes = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "category_id")
    @NotNull
    @Schema(description = "Category to which this product belongs")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @Schema(description = "List of options for this product")
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, Integer price, String img, Category category) {
        this.name = name;
        this.price = price;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void updateProduct(String name, Integer price, String img, Category category) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.category = category;
    }

    public List<Option> sortAndBringOptions() {
        options.sort(Comparator.comparing(Option::getId));
        return options;
    }

    public void removeOption(Option option, Product product) {
        if (product.optionAmount() <= 1) {
            throw new MinimumOptionException("상품의 옵션이 1개 이하인 경우 옵션을 삭제할 수 없습니다.");
        }
        this.options.remove(option);
    }

    public Integer optionAmount() {
        return this.options.size();
    }

    public void addOption(Option option) {
        this.options.add(option);
    }
}
