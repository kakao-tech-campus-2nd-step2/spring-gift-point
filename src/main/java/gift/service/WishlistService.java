package gift.service;

import gift.dto.WishlistDTO;
import gift.model.Option;
import gift.model.User;
import gift.model.Wishlist;
import gift.repository.OptionRepository;
import gift.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final OptionRepository optionRepository;

    public WishlistService(WishlistRepository wishlistRepository, OptionRepository optionRepository) {
        this.wishlistRepository = wishlistRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public WishlistDTO addWishlist(User user, WishlistDTO wishlistDTO) {
        Option option = optionRepository.findById(wishlistDTO.getOptionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid option Id:" + wishlistDTO.getOptionId()));

        Wishlist wishlist = new Wishlist(user, option.getProduct(), option);
        wishlistRepository.save(wishlist);
        return new WishlistDTO(user.getEmail(), option.getProduct().getId(), option.getId());
    }

    @Transactional(readOnly = true)
    public Page<WishlistDTO> getWishlist(User user, int page, int size, String[] sort) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc(sort[0])));
        return wishlistRepository.findByUserEmail(user.getEmail(), pageable)
                .map(wishlist -> new WishlistDTO(wishlist.getUser().getEmail(), wishlist.getProduct().getId(), wishlist.getOption().getId()));
    }

    @Transactional
    public void deleteWishlist(User user, Long wishId) {
        wishlistRepository.deleteById(wishId);
    }
}