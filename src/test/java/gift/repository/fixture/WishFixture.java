package gift.repository.fixture;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;

public class WishFixture {
    public static Wish createWish(Member member, Product product,int quantity){
        return new Wish(member,product,quantity);
    }
}
