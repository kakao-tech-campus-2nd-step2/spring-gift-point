package gift.model.item;

import gift.exception.ErrorCode;
import gift.exception.customException.CustomDuplicateException;
import gift.exception.customException.CustomNotFoundException;
import gift.model.categories.Category;
import gift.model.option.Option;
import gift.model.wishList.WishItem;
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
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.annotations.BatchSize;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column
    private String name;
    @NotNull
    @Column
    private Long price;
    private String imgUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
    private final List<WishItem> wishes = new ArrayList<>();
    @BatchSize(size = 100)
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Option> options = new ArrayList<>();

    protected Item() {
    }

    public Item(Long id, String name, Long price, String imgUrl, Category category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imgUrl = imgUrl;
        this.category = category;
    }

    public Item(String name, Long price, String imgUrl, Category category) {
        this(null, name, price, imgUrl, category);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public List<WishItem> getWishes() {
        return wishes;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void update(ItemDTO itemDTO, Category category) {
        this.name = itemDTO.getName();
        this.price = itemDTO.getPrice();
        this.imgUrl = itemDTO.getImgUrl();
        this.category = category;
    }

    public void addOption(Option option) throws CustomDuplicateException {
        if (checkDuplicateOptionName(option.getName())) {
            throw new CustomDuplicateException(ErrorCode.DUPLICATE_NAME);
        }
        options.add(option);
    }

    public void addOptionList(List<Option> options) {
        this.options.addAll(options.stream().collect(Collectors.toList()));
    }

    public Option getOptionByOptionId(Long optionId) {
        return options.stream().filter(o -> o.getId().equals(optionId)).findFirst()
            .orElseThrow(() -> new CustomNotFoundException(ErrorCode.OPTION_NOT_FOUND));
    }

    public boolean checkDuplicateOptionName(String name) {
        return options.stream().map(Option::getName).toList().contains(name);
    }
}
