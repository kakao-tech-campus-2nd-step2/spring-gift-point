package gift.doamin.wishlist.repository;

import gift.doamin.wishlist.entity.Wish;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class MemoryWishListRepository implements WishListRepository {

    private final Map<Long, Wish> wishLists = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    @Override
    public Wish save(Wish wish) {
        Long id = sequence.getAndIncrement();
        wishLists.put(id, wish);
        return wish;
    }

    @Override
    public List<Wish> findByUserId(Long userId) {
        List<Wish> result = new ArrayList<>();

        for (Wish wish : wishLists.values()) {
            if (wish.getUser().getId().equals(userId)) {
                result.add(wish);
            }
        }

        return result;
    }

    @Override
    public Wish findByUserIdAndProductId(Long userId, Long productId) {
        for (Wish wish : wishLists.values()) {
            if (wish.getUser().getId().equals(userId) && wish.getProduct().getId()
                .equals(productId)) {
                return wish;
            }
        }
        return null;
    }

    @Override
    public void update(Wish wish) {
        wishLists.put(wish.getId(), wish);
    }

    @Override
    public void deleteById(Long id) {
        wishLists.remove(id);
    }

    @Override
    public boolean existsByUserIdAndProductId(Long userId, Long productId) {
        for (Wish wish : wishLists.values()) {
            if (wish.getUser().getId().equals(userId) && wish.getProduct().getId()
                .equals(productId)) {
                return true;
            }
        }
        return false;
    }
}
