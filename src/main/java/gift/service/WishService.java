package gift.service;

import gift.dto.ProductDTO;
import gift.dto.WishDTO;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    @Autowired
    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    public Page<WishDTO> getWishesByMemberId(Long memberId, Pageable pageable) {
        return wishRepository.findByMemberId(memberId, pageable)
                .map(this::convertToDTO);
    }

    @Transactional
    public WishDTO addWish(Long memberId, Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productService.convertToEntity(productDTO);
        Wish wish = new Wish(memberId, product);
        Wish savedWish = wishRepository.save(wish);
        return convertToDTO(savedWish);
    }

    @Transactional
    public void deleteWish(Long wishId) {
        wishRepository.deleteById(wishId);
    }

    private WishDTO convertToDTO(Wish wish) {
        return new WishDTO(wish.getId(), wish.getMemberId(), wish.getProduct().getId());
    }
}
