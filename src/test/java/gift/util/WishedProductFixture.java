package gift.util;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishedProduct;

public class WishedProductFixture {

    public static WishedProduct createWishedProduct(Member member, Product product) {
        return new WishedProduct(member, product, 3);
    }
}
