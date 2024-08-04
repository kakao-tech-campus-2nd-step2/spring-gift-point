package gift.doamin.wishlist.repository;

import gift.doamin.wishlist.entity.Wish;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
    public Page<Wish> findAllByUserId(Long userId, Pageable pageable) {
        List<Wish> result = new ArrayList<>();

        for (Wish wish : wishLists.values()) {
            if (wish.getUser().getId().equals(userId)) {
                result.add(wish);
            }
        }

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), result.size());

        return new PageImpl<>(result.subList(start, end), pageable, result.size());
    }

    @Override
    public void update(Wish wish) {
        wishLists.put(wish.getId(), wish);
    }

    @Override
    public Optional<Wish> findById(Long id) {
        return Optional.ofNullable(wishLists.get(id));
    }

    @Override
    public void deleteById(Long id) {
        wishLists.remove(id);
    }

    @Override
    public Optional<Wish> findByUserIdAndOptionId(Long userId, Long optionId) {
        for (Wish wish : wishLists.values()) {
            if (wish.getUser().getId().equals(userId) && wish.getOption().getId()
                .equals(optionId)) {
                return Optional.of(wish);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByUserIdAndOptionId(Long userId, Long optionId) {
        return wishLists.values().stream().anyMatch(
            wish -> wish.getUser().getId().equals(userId) && wish.getOption().getId()
                .equals(optionId));
    }

    @Override
    public void delete(Wish wish) {
        wishLists.remove(wish.getId());
    }
}
