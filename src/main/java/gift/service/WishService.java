package gift.service;

import gift.domain.Product;
import gift.domain.User;
import gift.domain.Wish;
import gift.dto.common.PageInfo;
import gift.dto.requestdto.WishRequestDTO;
import gift.dto.responsedto.WishListPageResponseDTO;
import gift.dto.responsedto.WishResponseDTO;
import gift.repository.JpaProductRepository;
import gift.repository.JpaUserRepository;
import gift.repository.JpaWishRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.data.domain.Page;
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
    public WishListPageResponseDTO getAllWishes(Long userId, int page, int size, String criteria) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(criteria));

        Page<Wish> wishList = jpaWishRepository.findAllByUser(userId, pageable);

        List<WishResponseDTO> wishResponseDTOList = wishList
            .stream()
            .map(WishResponseDTO::from)
            .toList();

        PageInfo pageInfo = new PageInfo(page, wishList.getTotalElements(), wishList.getTotalPages());
        return new WishListPageResponseDTO(pageInfo, wishResponseDTOList);
    }

    @Transactional(readOnly = true)
    public WishResponseDTO getOneWish(Long wishId) {
        Wish wish = getWish(wishId);
        return WishResponseDTO.from(wish);
    }

    public WishResponseDTO addWish(WishRequestDTO wishRequestDTO) {
        //TODO: db에 존재하는 product는 insert하면 안됨
        User user = jpaUserRepository.findById(wishRequestDTO.userId())
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        Product product = jpaProductRepository.findById(wishRequestDTO.productId())
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
        Wish wish = new Wish(user, product, wishRequestDTO.count());

        jpaWishRepository.save(wish);
        return WishResponseDTO.from(wish);
    }

    public Long deleteWish(Long wishId) {
        Wish wish = getWish(wishId);
        jpaWishRepository.delete(wish);
        return wish.getId();
    }

    private Wish getWish(Long wishId) {
        return jpaWishRepository.findById(wishId)
            .orElseThrow(() -> new NoSuchElementException("id가 잘못되었습니다."));
    }
}
