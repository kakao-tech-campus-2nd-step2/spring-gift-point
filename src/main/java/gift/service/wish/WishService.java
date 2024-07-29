package gift.service.wish;

import gift.dto.paging.PagingResponse;
import gift.exception.WishItemNotFoundException;
import gift.model.gift.Gift;
import gift.model.user.User;
import gift.model.wish.Wish;
import gift.repository.gift.GiftRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final GiftRepository giftRepository;

    @Autowired
    public WishService(WishRepository wishRepository, UserRepository userRepository, GiftRepository giftRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.giftRepository = giftRepository;
    }

    public void addGiftToUser(Long userId, Long giftId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new IllegalArgumentException("Invalid gift ID"));

        wishRepository.findByUserAndGift(user, gift)
                .ifPresentOrElse(
                        existingWish -> {
                            existingWish.increaseQuantity();
                            wishRepository.save(existingWish);
                        },
                        () -> {
                            Wish userGift = new Wish(user, gift, quantity);
                            wishRepository.save(userGift);
                        }
                );
    }

    @Transactional
    public void removeGiftFromUser(Long userId, Long giftId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new IllegalArgumentException("Invalid gift ID"));
        wishRepository.deleteByUserAndGift(user, gift);
    }

    public PagingResponse<Wish> getGiftsForUser(Long userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Page<Wish> wishes = wishRepository.findByUser(user, pageRequest);

        return new PagingResponse<>(page, wishes.getContent(), size, wishes.getTotalElements(), wishes.getTotalPages());
    }

    @Transactional
    public void updateWishQuantity(Long userId, Long giftId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Gift gift = giftRepository.findById(giftId).orElseThrow(() -> new IllegalArgumentException("Invalid gift ID"));
        wishRepository.findByUserAndGift(user, gift)
                .ifPresentOrElse(
                        existingWish -> {
                            existingWish.modifyQuantity(quantity);
                            wishRepository.save(existingWish);
                        },
                        () -> {
                            throw new WishItemNotFoundException("해당 위시리스트 아이템을 찾을 수 없습니다.");
                        }
                );
    }
}