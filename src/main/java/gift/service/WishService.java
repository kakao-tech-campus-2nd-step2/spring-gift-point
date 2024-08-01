package gift.service;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.dto.request.WishRequestDTO;
import gift.dto.response.PagingProductResponseDTO;
import gift.dto.response.PagingWishResponseDTO;
import gift.dto.response.ProductResponseDTO;
import gift.dto.response.WishResponseDTO;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.WishException;
import gift.exception.memberException.MemberNotFoundException;
import gift.exception.optionException.OptionNotFoundException;
import gift.exception.productException.ProductNotFoundException;
import gift.exception.wishException.DuplicatedWishException;
import gift.exception.wishException.WishNotFoundException;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WishService(WishRepository wishRepository,
                       OptionRepository optionRepository,
                       MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }


    public PagingWishResponseDTO getWishes(LoginMemberDTO loginMemberDTO, Pageable pageable) {
        Long memberId = loginMemberDTO.memberId();

        Page<Wish> wishPage = wishRepository.findByMemberId(memberId, pageable);
        List<WishResponseDTO> wishResponseDTOs = wishPage.getContent()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());

        return new PagingWishResponseDTO(
                wishResponseDTOs,
                wishPage.getNumber(),
                (int) wishPage.getTotalElements(),
                wishPage.getSize(),
                wishPage.isLast()
        );

    }


    @Transactional
    public void addWish(WishRequestDTO wishRequestDTO , LoginMemberDTO loginMemberDTO) {
        Long productId = wishRequestDTO.productId();
        Long memberId = loginMemberDTO.memberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("잘못된 회원입니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductNotFoundException(productId));

        Optional<Wish> existingWish = wishRepository.findByMemberIdAndProductId(memberId, productId);

        if (existingWish.isPresent()) {
            throw new DuplicatedWishException("이미 존재하는 상풉입니다.");
        }

        Wish wish = new Wish(member, product, LocalDateTime.now() );
        wishRepository.save(wish);
    }

    public void removeWish(Long wishId, LoginMemberDTO loginMemberDTO) {
        Long memberId = loginMemberDTO.memberId();
        Wish wish = wishRepository.findByIdAndMemberId(wishId, memberId)
                .orElseThrow(()-> new WishNotFoundException("상품을 찾을 수 없습니다") );
        wishRepository.delete(wish);
    }


    private Wish getWishEntity(Long memberId){
        return wishRepository.findById(memberId)
                .orElseThrow( () -> new WishNotFoundException("Wish Not Found"));
    }

    private WishResponseDTO toDto(Wish wish){
        return new WishResponseDTO(
                wish.getId(),
                productToDto(wish.getProduct())
        );
    }


    private ProductResponseDTO productToDto(Product product){
        return new ProductResponseDTO(product.getId(),
                product.getName(),
                product.getImageUrl(),
                product.getPrice()
        );
    }


    /*public Page<WishResponseDTO> paging(Pageable pageable){

        int page = pageable.getPageNumber() - 1;
        int pageSize = pageable.getPageSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Page<Wish> wishesPage = wishRepository.findAll(PageRequest.of(page, pageSize, sort));

        Page<WishResponseDTO> wishResponseDTOs = wishesPage.map(wish -> {
            return new WishResponseDTO(
                    wish.getMember().getEmail(),
                    wish.getProduct().getName(),
                    wish.getProduct().getImageUrl(),
                    wish.getCount(),
                    wish.getProduct().getPrice()
            );
        });

        return wishResponseDTOs;
    }*/

}