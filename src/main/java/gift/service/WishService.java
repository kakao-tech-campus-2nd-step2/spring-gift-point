package gift.service;

import gift.dto.PageRequestDTO;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wish;
import gift.dto.WishDTO;
import gift.repository.WishRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishService {
    private final ProductService productService;
    private final WishRepository wishRepository;

    public WishService(ProductService productService, WishRepository wishRepository) {
        this.productService = productService;
        this.wishRepository = wishRepository;
    }

    public Page<WishDTO> getWishlist(long memberId, PageRequestDTO pageRequestDTO) throws AuthenticationException {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage(),
                pageRequestDTO.getSize(), pageRequestDTO.getSort());

        Page<Wish> WishlistPage = wishRepository.findByMember_Id(memberId, pageable);

        return WishlistPage.map(WishDTO::getWishProductDTO);
    }

    public void postWishlist(Long productId, Member member) throws AuthenticationException {
        Product product = productService.getProductById(productId);
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);
    }

    public void deleteProduct(Long id){
        wishRepository.deleteById(id);
    }
}
