package gift.ServiceTest.FakeRepository;

import gift.Model.Entity.Member;
import gift.Model.Entity.Product;
import gift.Model.Entity.Wish;

import java.util.*;

public class FakeWishRepository {
    private final Map<Long, Wish> wishMap = new HashMap<>();
    private Long id = 1L;

    public Wish save(Wish wish) {
        wishMap.put(id++, wish);
        return wish;
    }

    public List<Wish> findByMember(Member member) {
        return wishMap.values()
                .stream()
                .filter(it -> it.getMember().equals(member))
                .toList();
    }

    public List<Wish> findByMemberAndProduct(Member member, Product product) {
        return wishMap.values()
                .stream()
                .filter(it -> it.getMember().equals(member) && it.getProduct().equals(product))
                .toList();
    }

    public Optional<Wish> findById(Long id) {
        return Optional.ofNullable(wishMap.get(id));
    }

    public void deleteById(Long id) {
        wishMap.remove(id);
    }
}
