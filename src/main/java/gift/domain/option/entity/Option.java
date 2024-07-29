package gift.domain.option.entity;

import gift.domain.option.exception.OptionNameValidException;
import gift.domain.option.exception.OptionQuantityValidException;
import gift.domain.order.entity.Orders;
import gift.domain.product.entity.Product;
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
import java.util.List;

@Entity
public class Option {

    private static final String regex = "[\\w\\s\\(\\)\\[\\]\\+\\-\\&\\/가-힣]*";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private int quantity;
    @OneToMany(mappedBy = "option", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected Option() {
    }

    public Option(String name, int quantity) {
        this(null, name, quantity, null);
    }

    public Option(String name, int quantity, Product product) {
        this(null, name, quantity, product);
    }

    public Option(Long id, String name, int quantity, Product product) {
        validAllowedCharacter(name);
        validNameLength(name);
        validQuantity(quantity);

        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.product = product;
    }

    private static void validNameLength(String name) {
        if (name.length() > 50) {
            throw new OptionNameValidException("옵션 이름 50자 초과");
        }
    }

    private static void validQuantity(int quantity) {
        if (quantity < 1 || quantity >= 100_000_000) {
            throw new OptionQuantityValidException("수량은 1개 이상 1억개 미만으로 설정해주세요.");
        }
    }

    private static void validAllowedCharacter(String name) {
        if (!name.matches(regex)) {
            throw new OptionNameValidException("특수 문자는 '(), [], +, -, &, /, _ '만 사용가능 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void checkDuplicateName(List<Option> optionList) {

        if (optionList.stream().map(Option::getName).anyMatch((name) -> name.equals(this.name))) {
            throw new OptionNameValidException(this.name + "은 중복된 이름입니다.");
        }
    }

    public void addProduct(Product product) {
        this.product = product;
    }

    public void subtractQuantity(int quantity) {
        if (this.quantity < quantity) {
            throw new OptionQuantityValidException("수량이 부족합니다.");
        }
        this.quantity -= quantity;
    }
}
