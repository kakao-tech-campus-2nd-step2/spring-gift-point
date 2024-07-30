package gift.model.product;

import gift.common.exception.AlreadyExistName;
import gift.common.exception.OptionNotFoundException;
import gift.model.category.Category;
import gift.model.option.Option;
import gift.model.wishlist.WishList;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(max = 15, message = "이름의 최대 글자수는 15입니다.")
    @Pattern(
        regexp = "^[가-힣a-zA-Z0-9\\(\\)\\[\\]\\+\\-\\&\\/\\_\\s]*$",
        message = "상품 이름은 최대 15자, 한글과 영문, 그리고 특수기호([],(),+,-,&,/,_)만 사용 가능합니다!"
    )
    @Pattern(
        regexp = "(?!.*카카오).*",
        message = "'카카오'가 포함된 문구는 담당 MD와 협의한 경우에만 사용할 수 있습니다!"
    )
    private String name;
    private int price;
    private String imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    public Product(){}
    public Product(String name, int price, String imageUrl, Category category, String optionName, int quantity) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.options.add(new Option(optionName, quantity));
        if(options.isEmpty())
            throw new OptionNotFoundException("상품에는 최소 하나 이상의 옵션이 있어야 합니다.");
    }

    public void addOption(String name, int quantity) {
        if (validName(name)) {
            throw new AlreadyExistName("동일한 옵션명이 이미 존재합니다.");
        }
        Option option = new Option(name, quantity, this);
        options.add(option);
    }
    private boolean validName(String name) {
        return options.stream()
            .anyMatch(opt -> opt.getName().equals(name));
    }

    // getters, setters
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


    // update
    public void update(ProductRequest productRequest, Category category) {
        this.name = productRequest.name();
        this.price = productRequest.price();
        this.imageUrl = productRequest.imageUrl();
        this.category = category;
    }
}