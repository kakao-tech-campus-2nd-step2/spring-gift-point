package gift.wish.service;

import gift.common.auth.JwtUtil;
import gift.member.model.Member;
import gift.product.domain.Product;
import gift.product.dto.ProductDTO;
import gift.wish.domain.Wish;
import gift.wish.dto.WishDTO;
import gift.member.repository.MemberRepository;
import gift.product.repository.ProductRepository;
import gift.wish.dto.WishCreateResponse;
import gift.wish.repository.WishRepository;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final JwtUtil jwtUtil;

    public WishServiceImpl(WishRepository wishlistRepository, MemberRepository memberRepository, ProductRepository productRepository, JwtUtil jwtUtil) {
        this.wishRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @Transactional
    // 각 트랜잭션마다 고유한 영속성 컨텍스트가 생성된다.
    public WishCreateResponse createWish(String token, Long productId) {
        // token을 통해 사용자 정보 들고오기
        String email = jwtUtil.extractEmail(token);
        // 영속화의 이유
        // 1. 지금 member는 연관관계를 가지는, DB에 있는 member가 아닌 그냥 생짜배기 member이다. 따라서 영속성이 없다.
        // 2. Member 엔티티가 영속성 컨텍스트에 없을 경우, 그 엔티티와 관련된 Wish 엔티티를 추가하거나 변경하는 작업이 반영되지 않을 수 있다.
        // 3. 예를 들어 Wish 엔티티를 활용할 때 영속성 컨텍스트에 없는 Member 엔티티를 활용하면 LazyInitializationException이 발생할 수 있다.
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Member not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));

        // Wish에서 Member와 Product를 추가해 연관관계 설정
        Wish wish = new Wish(member, product, LocalDateTime.now());

        // 양방향 연관 관계 유지 : Wish 엔티티를 Member 엔티티와 Product 엔티티에도 추가해준다.
        member.addWish(wish);
        product.addWish(wish);

        wishRepository.save(wish);

        return new WishCreateResponse(wish.getId(), wish.getProduct().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishDTO> getWishlistByMemberId(Member member) {
        // 영속화
        member = memberRepository.findById(member.getId()).orElseThrow(() -> new IllegalArgumentException("Member not found"));

        // 지연 로딩을 통해 Member와 연관된 Wish 엔티티들을 로드
        List<Wish> wishlist = member.getWishList();

        // Wish 엔티티들을 WishDTO로 변환 후 반환 (Json으로 변환하기 위함)
        return wishlist.stream()
                .map(wish -> new WishDTO(
                        wish.getId(),
                        new ProductDTO(
                                wish.getProduct().getId(),
                                wish.getProduct().getName(),
                                wish.getProduct().getPrice(),
                                wish.getProduct().getImageUrl()
                        )
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Wish getWishByProductId(Long productId) {
        Optional<Wish> wish = wishRepository.findByProductId(productId);
        return wish.orElse(null);
    }

    @Override
    @Transactional
    public void deleteWish(Long wishId) {
        Wish wish = wishRepository.findById(wishId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Wish not found"));
        wishRepository.delete(wish);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WishDTO> getWishlistByPage(int page, int size, String sortBy, String direction) {
        // validation
        if (page < 0 || size <= 0) {
            throw new IllegalArgumentException("Invalid page or size");
        }

        // sorting
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // paging
        Pageable pageable = PageRequest.of(page, size, sort); // Pageable 객체 생성
        Page<Wish> wishPage = wishRepository.findAll(pageable); // Page<Wish> 타입의 객체를 생성

        // Wish 엔티티를 WishDTO로 변환
        return wishPage.map(wish -> new WishDTO(
                wish.getId(),
                new ProductDTO(
                        wish.getProduct().getId(),
                        wish.getProduct().getName(),
                        wish.getProduct().getPrice(),
                        wish.getProduct().getImageUrl()
                )
        ));
    }
}
