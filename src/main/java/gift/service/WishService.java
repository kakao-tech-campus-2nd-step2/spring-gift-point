package gift.service;

import gift.domain.Product;
import gift.domain.User;
import gift.domain.Wish;
import gift.dto.requestdto.WishRequestDTO;
import gift.dto.responsedto.WishResponseDTO;
import gift.repository.JpaProductRepository;
import gift.repository.JpaUserRepository;
import gift.repository.JpaWishRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class WishService {
    private final JpaWishRepository jpaWishRepository;
    private final JpaProductRepository jpaProductRepository;
    private final JpaUserRepository jpaUserRepository;

    public WishService(JpaWishRepository jpaWishRepository,
        JpaProductRepository jpaProductRepository,
        JpaUserRepository jpaUserRepository) {
        this.jpaWishRepository = jpaWishRepository;
        this.jpaProductRepository = jpaProductRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Transactional(readOnly = true)
    public List<WishResponseDTO> getAllWishes(Long userId) {
        return jpaWishRepository.findAllByUserId(userId)
            .stream()
            .map(WishResponseDTO::from)
            .toList();
    }

    @Transactional(readOnly = true)
    public List<WishResponseDTO> getAllWishes(Long userId, int page, int size, String criteria) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(criteria));
        List<WishResponseDTO> wishResponseDTOList = jpaWishRepository.findAllByUser(userId, pageable)
            .stream()
            .map(WishResponseDTO::from)
            .toList();
        return wishResponseDTOList;
    }

    @Transactional(readOnly = true)
    public WishResponseDTO getOneWish(Long wishId) {
        Wish wish = getWish(wishId);
        return WishResponseDTO.from(wish);
    }

    public Long addWish(WishRequestDTO wishRequestDTO) {
        //TODO: db에 존재하는 product는 insert하면 안됨
        User user = jpaUserRepository.findById(wishRequestDTO.userId())
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        Product product = jpaProductRepository.findById(wishRequestDTO.productId())
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        Wish wish = new Wish(user, product, wishRequestDTO.count());
        return jpaWishRepository.save(wish).getId();
    }

    public Long deleteWish(Long wishId) {
        Wish wish = getWish(wishId);
        jpaWishRepository.delete(wish);
        return wish.getId();
    }

    private Wish getWish(Long wishId) {
        Wish wish = jpaWishRepository.findById(wishId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        return wish;
    }
}
