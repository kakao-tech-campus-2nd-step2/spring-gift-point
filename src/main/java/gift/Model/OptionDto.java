package gift.Model;

import jakarta.validation.constraints.*;

public class OptionDto {

    private long id;
    private long productId;
    @Size(min = 1, max = 50, message = "이름의 글자 수는 1자 이상, 50자 이하여야 합니다.")
    @Pattern(regexp = "^[\\w\\s()+\\-/&_\\[\\]가-힣]*$", message = "( ), [ ], +, -, &, /, _" +
            "외의 다른 특수 문자 사용 불가")
    @NotNull(message = "옵션 이름은 필수입니다.")
    private String name;
    private int price;

    @Min(value = 1, message = "옵션의 수량은 최소 하나 이상입니다.")
    @Max(value = 100_000_000, message = "옵션의 수량은 1억 이하입니다.")
    private int quantity;

    public OptionDto() {
    }

    public OptionDto(long id, long productId, String name, int price, int quantity) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }
}
