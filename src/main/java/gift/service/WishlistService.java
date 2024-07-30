package gift.service;

import gift.entity.Member;
import gift.entity.Wish;
import gift.exception.DataNotFoundException;
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
    private final int PAGE_SIZE = 5;

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
            .orElseThrow(() -> new DataNotFoundException("위시리스트가 존재하지 않습니다."));
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
    public Page<Wish> getWishPage(String email, int page, int size, String[] sort) {
        List<Sort.Order> sorts = new ArrayList<>();
        if (sort[1].equals("asc")) {
            sorts.add(Sort.Order.asc(sort[0]));
        }
        if (sort[1].equals("desc")) {
            sorts.add(Sort.Order.desc(sort[0]));
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(sorts));
        Member member = memberService.findByEmail(email);
        return wishRepository.findByMemberId(member.getId(), pageable);

    }
}
