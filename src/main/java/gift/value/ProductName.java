package gift.value;

import gift.exception.InvalidProductNameException;

public class ProductName {
    private final String name;

    public ProductName(String name) {
        if (name.contains("카카오")) {
            throw new InvalidProductNameException("상품명에 '카카오'를 포함할 경우, 담당 MD에게 문의바랍니다.");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
