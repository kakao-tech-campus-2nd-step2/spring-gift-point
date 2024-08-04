package gift.service;

import gift.dto.pageDTO.WishlistPageResponseDTO;
import gift.dto.wishlistDTO.WishlistRequestDTO;
import gift.dto.wishlistDTO.WishlistResponseDTO;
import gift.exception.InvalidInputValueException;
import gift.exception.NotFoundException;
import gift.model.Member;
import gift.model.Option;
import gift.model.Wishlist;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberRepository memberRepository;
    private final OptionRepository optionRepository;

    public WishlistService(WishlistRepository wishlistRepository, MemberRepository memberRepository,
        OptionRepository optionRepository) {
        this.wishlistRepository = wishlistRepository;
        this.memberRepository = memberRepository;
        this.optionRepository = optionRepository;
    }

    public WishlistPageResponseDTO getWishlistsByEmail(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new InvalidInputValueException("유효하지 않은 이메일입니다.");
        }
        Page<Wishlist> wishlists = wishlistRepository.findByMember(member, pageable);
        return new WishlistPageResponseDTO(wishlists);
    }

    @Transactional
    public WishlistResponseDTO addWishlist(String email, WishlistRequestDTO wishlistRequestDTO) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new InvalidInputValueException("유효하지 않은 이메일입니다.");
        }
        Option option = optionRepository.findById(wishlistRequestDTO.optionId())
            .orElseThrow(() -> new NotFoundException("옵션을 찾을 수 없습니다."));
        Wishlist wishlist = new Wishlist(null, member, option);
        wishlistRepository.save(wishlist);
        return new WishlistResponseDTO(wishlist.getId(), option.getProduct().getId());
    }

    @Transactional
    public void removeWishlist(Long wishlistId) {
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
            .orElseThrow(() -> new NotFoundException("위시리스트를 찾을 수 없습니다."));
        wishlistRepository.delete(wishlist);
    }

    @Transactional
    public void removeWishlistByOptionId(Long optionId) {
        wishlistRepository.deleteByOptionId(optionId);
    }
}