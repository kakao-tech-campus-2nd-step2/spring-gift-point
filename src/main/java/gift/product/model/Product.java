package gift.product.model;

import gift.category.model.Category;
import gift.common.exception.OptionException;
import gift.common.exception.ProductException;
import gift.common.model.BaseEntity;
import gift.option.OptionErrorCode;
import gift.option.model.Option;
import gift.product.ProductErrorCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Product extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private Integer price;
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @OneToMany(mappedBy = "product")
    private List<Option> options = new ArrayList<>();

    protected Product() {
    }

    public Product(String name, int price, String imageUrl, Category category) {
        Product.Validator.validateKakaoWord(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public Product(Long id, String name, int price, String imageUrl, Category category) {
        this.setId(id);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public void updateInfo(String name, Integer price, String imageUrl, Category category) {
        Product.Validator.validateKakaoWord(name);
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    public void addOption(Option option) throws OptionException {
        this.options.add(option);
        Product.Validator.validateDuplicated(options);
    }

    public void validateOptions() throws OptionException {
        Product.Validator.validateDuplicated(options);
    }

    public Integer getTotalPrice(Integer quantity) {
        return price * quantity;
    }

    private static class Validator {

        private static void validateKakaoWord(String name) throws ProductException {
            if (name.contains("카카오")) {
                throw new ProductException(ProductErrorCode.HAS_KAKAO_WORD);
            }
        }

        private static void validateDuplicated(List<Option> optionList) throws OptionException {
            List<String> optionNameList = getOptionNames(optionList);
            Set<String> optionNameSet = new HashSet<>(optionNameList);
            if (optionNameList.size() != optionNameSet.size()) {
                throw new OptionException(OptionErrorCode.NAME_DUPLICATED);
            }
        }

        private static List<String> getOptionNames(List<Option> optionList) {
            return optionList.stream().map(Option::getName).toList();
        }
    }
}
