package gift.model;

import gift.common.exception.DuplicateDataException;
import gift.common.exception.EntityNotFoundException;
import jakarta.persistence.*;
import org.hibernate.annotations.BatchSize;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(indexes = @Index(name = "idx_product_created_at", columnList = "created_at"))
public class Product extends BasicEntity{
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false, length = 1000)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options = new ArrayList<>();

    protected Product() {}

    public Product(Long id) {
        super(id);
    }

    public Product(String name, int price, String imageUrl, Category category, List<Option> options) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        for (Option option : options) {
            addOption(option);
        }
    }

    public void updateProduct(String name, int price, String imageUrl, Category category) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addOption(Option entity) {
        if (entity == null) {
            return;
        }
        checkDuplicateOptionName(entity.getId(), entity.getName());
        entity.setProduct(this);
        options.add(entity);
    }

    public void subOption(Option entity) {
        if (options.size() <= 1) {
            throw new IllegalArgumentException("At least 1 option is required");
        }
        options.remove(entity);
    }

    public void subtractOptionQuantity(Long optionId, int amount) {
        Option option = findOptionByOptionId(optionId);
        option.subtractQuantity(amount);
    }

    public Option findOptionByOptionId(Long optionId) {
        return options.stream()
                .filter(option -> option.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 옵션입니다."));
    }

    public void checkDuplicateOptionName(Long theirId, String theirName) {
        for (Option option : options) {
            if(option.isSameName(theirName) && option.isNotSameId(theirId)) {
                throw new DuplicateDataException("이미 존재하는 옵션명입니다.");
            }
        }
    }

    public void validatePoint(int point) {
        if (price < point) {
            throw new IllegalArgumentException("상품 가격보다 큰 포인트를 사용할 수 없습니다.");
        }
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
