package gift.service;

import gift.entity.Member;
import gift.entity.Wish;
import gift.exception.CustomException;

import gift.exception.ErrorCode;
import gift.repository.MemberRepository;
import gift.repository.WishRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class WishlistService {

    private final WishRepository wishRepository;
    private final MemberService memberService;

    public WishlistService(WishRepository wishRepository, MemberService memberService) {
        this.wishRepository = wishRepository;
        this.memberService = memberService;
    }

    public void addWishlist(Wish wish, String email) {
        Member wishMember = wish.getMember();
        Member authMember = memberService.findByEmail(email);

        if (wishMember.getId().equals(authMember.getId())) {
            wishRepository.save(wish);
        }

    }

    public Wish findById(Long id) {
        return wishRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.WISHLIST_NOT_FOUND));
    }


    public void deleteWishlist(Long productId, String email) {
        Member authMember = memberService.findByEmail(email);
        wishRepository.deleteByMemberIdAndProductId(authMember.getId(), productId);

    }

    public List<Wish> getWishlistByEmail(String email) {
        Member member = memberService.findByEmail(email);
        return wishRepository.findByMemberId(member.getId());
    }

    @Transactional
    public Page<Wish> getWishPage(Pageable pageable, String email) {
        Member member = memberService.findByEmail(email);
        return wishRepository.findAllByMemberId(pageable, member.getId());

    }
}
