package gift.service.wish;

import gift.dto.paging.PagingResponse;
import gift.dto.wish.WishResponse;
import gift.exception.*;
import gift.model.product.Product;
import gift.model.user.User;
import gift.model.wish.Wish;
import gift.repository.product.ProductRepository;
import gift.repository.wish.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    public void addGiftToUser(User user, Long giftId, int quantity) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));

        wishRepository.findByUserAndProduct(user, product)
                .ifPresent(wish -> {
                    throw new AlreadyExistException("이미 위시 리스트에 추가된 상품입니다.");
                });

        Wish userGift = new Wish(user, product, quantity);
        wishRepository.save(userGift);
    }

    @Transactional
    public void removeGiftFromUser(User user, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 위시리스트입니다."));
        if (wish.getUser().getId() != user.getId()) {
            throw new WishInvalidAuthException("본인의 위시리스트만 삭제 가능합니다.");
        }
        wishRepository.deleteByUserAndId(user, wishId);
    }

    public PagingResponse<WishResponse.Info> getGiftsFromUser(User user, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Wish> wishes = wishRepository.findByUser(user, pageRequest);
        List<WishResponse.Info> wishResponses = wishes.getContent()
                .stream()
                .map(wish -> new WishResponse.Info(wish.getId(), wish.getProduct().getId(), wish.getProduct().getName(), wish.getProduct().getPrice(), wish.getProduct().getImageUrl()))
                .collect(Collectors.toList());
        return new PagingResponse<>(page, wishResponses, size, wishes.getTotalElements(), wishes.getTotalPages());
    }

    @Transactional
    public void updateWishQuantity(User user, Long giftId, int quantity) {
        Product product = productRepository.findById(giftId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 상품입니다."));
        wishRepository.findByUserAndProduct(user, product)
                .ifPresentOrElse(
                        existingWish -> {
                            existingWish.modifyQuantity(quantity);
                            wishRepository.save(existingWish);
                        },
                        () -> {
                            throw new EntityNotFoundException("존재하지 않는 위시리스트입니다.");
                        }
                );
    }
}