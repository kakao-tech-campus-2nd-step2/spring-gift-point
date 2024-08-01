package gift.service;

import gift.dto.ProductDTO;
import gift.dto.WishDTO;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public WishDTO addWish(Member member, Long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productService.convertToEntity(productDTO);
        Wish wish = new Wish(member, product);
        Wish savedWish = wishRepository.save(wish);
        return convertToDTO(savedWish);
    }

    @Transactional
    public void deleteWish(Long memberId, Long productId) {
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }

    private WishDTO convertToDTO(Wish wish) {
        WishDTO wishDTO = new WishDTO();
        wishDTO.setId(wish.getId());
        wishDTO.setMemberId(wish.getMember().getId());
        wishDTO.setProductId(wish.getProduct().getId());
        return wishDTO;
    }
}
