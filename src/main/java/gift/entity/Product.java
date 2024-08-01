package gift.entity;

import gift.constants.ErrorMessage;
import gift.dto.OptionEditRequest;
import gift.dto.OptionSubtractRequest;
import gift.dto.ProductRequest;
import gift.exception.ProductOptionRequiredException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
public class Product extends BaseEntity {

    @Column(name = "name", length = 15, nullable = false, unique = true)
    private String name;

    @Column(name = "price", nullable = false)
    private long price;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Wishlist> wishlist = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category")
    private Category category;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(ProductRequest productRequest, Category category) {
        this(productRequest.getName(), productRequest.getPrice(), productRequest.getImageUrl(),
            category);
    }

    public Product(String name, long price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    @Override
    public Long getId() {
        return super.getId();
    }

    public String getName() {
        return name;
    }

    public long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Wishlist> getWishlist() {
        return wishlist;
    }

    public Category getCategory() {
        return category;
    }

    public List<Option> getOptions() {
        return options;
    }

    public void updateProduct(ProductRequest productRequest, Category category) {
        this.name = productRequest.getName();
        this.price = productRequest.getPrice();
        this.imageUrl = productRequest.getImageUrl();
        this.category = category;
    }

    /**
     * 옵션은 상품에 종속된 하위 개념이므로 Product에서 Option을 수정하도록 명령.
     */
    public void updateOption(OptionEditRequest optionEditRequest) {
        Option option = getOptionById(optionEditRequest.getId());
        option.updateOption(optionEditRequest);
    }

    public void addWishlist(Wishlist wishlist) {
        if (wishlist == null) {
            throw new NullPointerException(ErrorMessage.NULL_POINTER_EXCEPTION_MSG);
        }
        this.wishlist.add(wishlist);
    }

    public void addOption(Option newOption) {
        if (newOption == null) {
            throw new NullPointerException(ErrorMessage.NULL_POINTER_EXCEPTION_MSG);
        }
        options.add(newOption);
    }

    public boolean isOptionNameDuplicate(Option newOption) {
        return options.stream()
            .anyMatch(option ->
                option.getName().equals(newOption.getName())
            );
    }

    public boolean canDeleteOption(Long optionId) {
        if (!isOptionSizeGreaterThanOne()) {
            throw new ProductOptionRequiredException(ErrorMessage.OPTION_MUST_MORE_THAN_ZERO);
        }
        return getOptionById(optionId) != null;
    }

    private boolean isOptionSizeGreaterThanOne() {
        return options.size() > 1;
    }

    public int subtractOption(OptionSubtractRequest subtractRequest) {
        Option option = getOptionById(subtractRequest.getId());
        return option.subtractQuantity(subtractRequest);
    }

    /**
     * optionId로 옵션을 찾고, 없으면 throw까지 수행.
     */
    private Option getOptionById(Long optionId) {
        return options.stream()
            .filter(option ->
                option.getId().equals(optionId)
            )
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException(ErrorMessage.OPTION_NOT_EXISTS_MSG));
    }
}
