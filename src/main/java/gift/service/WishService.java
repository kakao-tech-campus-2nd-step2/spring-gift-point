package gift.service;

import gift.dto.request.LoginMemberDTO;
import gift.dto.request.TokenLoginRequestDTO;
import gift.dto.response.WishResponseDTO;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.WishException;
import gift.exception.memberException.MemberNotFoundException;
import gift.exception.optionException.OptionNotFoundException;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final OptionRepository optionRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public WishService(WishRepository wishRepository,
                       OptionRepository optionRepository,
                       MemberRepository memberRepository) {
        this.wishRepository = wishRepository;
        this.optionRepository = optionRepository;
        this.memberRepository = memberRepository;
    }

    public List<WishResponseDTO> getWishes(LoginMemberDTO loginMemberDTO){
        Long memberId = loginMemberDTO.memberId();
        List<Wish> wishes = wishRepository.findByMemberId(memberId);
        return wishes.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public void addWish(Long optionId, LoginMemberDTO loginMemberDTO) {
        Long memberId = loginMemberDTO.memberId();

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException("잘못된 회원입니다."));

        Option option = optionRepository.findById(optionId)
                .orElseThrow(() -> new OptionNotFoundException("옵션을 찾을 수 없습니다."));
        Optional<Wish> existingWish = wishRepository.findByMemberIdAndOptionId(memberId, optionId);

        if (existingWish.isPresent()) {
            throw new DuplicatedWishException("이미 존재하는 상풉입니다.");
        }
        Wish wish = new Wish(member, option);
        wishRepository.save(wish);
    }

    public void removeWish(Long optionId, LoginMemberDTO loginMemberDTO) {
        Long memberId = loginMemberDTO.memberId();
        Wish existingWish = wishRepository.findByMemberIdAndOptionId(memberId, optionId)
                .orElseThrow(()-> new WishNotFoundException("상품을 찾을 수 없습니다") );
        wishRepository.delete(existingWish);
    }


    private Wish getWishEntity(Long memberId){
        return wishRepository.findById(memberId)
                .orElseThrow( () -> new WishNotFoundException("Wish Not Found"));
    }

    private WishResponseDTO toDto(Wish wish){
        return new WishResponseDTO(
                wish.getId());
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