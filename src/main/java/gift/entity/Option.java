package gift.entity;

import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.BlankContentException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import java.util.regex.Pattern;

@Entity
public class Option {
    private static final Pattern NAME_PATTERN = Pattern.compile(
            "[a-zA-Z0-9ㄱ-ㅎ가-힣()\\[\\]+\\-&/_ ]+"
    );

    @Id
    @Column(name = "option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    protected Option() { }
    public Option(Product product, String name, Integer quantity) {
        validateName(name);
        validateQuantity(quantity);

        this.product = product;
        this.name = name;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public Product getProduct() { return product; }
    public String getName() { return name; }
    public Integer getQuantity() { return quantity;}

    private void validateName(String name){
        if(name == null || name.isBlank())
            throw new BlankContentException("옵션 이름을 입력해주세요.");

        if(name.length() > 50)
            throw new BadRequestException("옵션 이름 길이는 1~50자만 가능합니다.");

        if(!NAME_PATTERN.matcher(name).matches())
            throw new BadRequestException("( ), [ ], +, -, &, /, _을 제외한 특수문자는 입력할 수 없습니다.");
    }

    private void validateQuantity(Integer quantity){
        if(quantity == null)
            throw new BadRequestException("개수가 올바르지 않습니다.");

        if(quantity < 1 || quantity >= 100000000)
            throw new BadRequestException("옵션 수량은 1개 이상, 1억개 미만만 가능합니다.");
    }

    public void changeOption(String name, Integer quantity){
        validateName(name);
        validateQuantity(quantity);

        this.quantity = quantity;
        this.name = name;
    }

    public void subtractQuantity(Integer subtractQuantity){
        validateQuantity(subtractQuantity);
        if(quantity <= subtractQuantity)
            throw new BadRequestException("옵션 수량은 1보다 작을 수 없습니다.");
        this.quantity -= subtractQuantity;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Option option = (Option) o;
        return Objects.equals(id, option.id) && Objects.equals(product,
                option.product) && Objects.equals(name, option.name)
                && Objects.equals(quantity, option.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product, name, quantity);
    }
}
