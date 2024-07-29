package gift.service;

import gift.domain.Member;
import gift.domain.Option;
import gift.domain.Wish;
import gift.dto.request.MemberRequest;
import gift.dto.response.OptionResponse;
import gift.dto.request.WishRequest;
import gift.dto.response.WishResponse;
import gift.exception.customException.OptionAlreadyInWishlistException;
import gift.exception.customException.WishNotFoundException;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gift.exception.errorMessage.Messages.NOT_FOUND_WISH;
import static gift.exception.errorMessage.Messages.OPTION_ALREADY_IN_WISHLIST;

@Service
public class WishService {
    private final WishRepository wishRepository;
    private final OptionService optionService;

    public WishService(WishRepository wishRepository, OptionService optionService) {
        this.wishRepository = wishRepository;
        this.optionService = optionService;
    }

    @Transactional
    public void save(MemberRequest memberRequest, WishRequest wishRequest){
        if(wishRepository.existsByOptionId(wishRequest.optionId())){
            throw new OptionAlreadyInWishlistException(OPTION_ALREADY_IN_WISHLIST);
        }

        OptionResponse optionResponse = optionService.findById(wishRequest.optionId());

        Option option = optionResponse.toEntity();
        Member member = memberRequest.toEntity();

        Wish newWish = new Wish(member,option,wishRequest.quantity());

        wishRepository.save(newWish);
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getMemberWishesByMemberId(Long memberId){
        return wishRepository.findByMemberId(memberId)
                .stream()
                .map(WishResponse::from)
                .toList();

    }

    @Transactional(readOnly = true)
    public Page<WishResponse> getPagedMemberWishesByMemberId(Long memberId, Pageable pageable){
        Page<Wish> wishPage = wishRepository.findByMemberId(memberId,pageable);
        return wishPage.map(WishResponse::from);
    }

    @Transactional
    public void deleteWishByMemberIdAndId(Long memberId, Long id){
        Wish foundWish = wishRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(()-> new WishNotFoundException(NOT_FOUND_WISH));

        foundWish.remove();
        wishRepository.deleteById(id);
    }

    @Transactional
    public void updateQuantityByMemberIdAndId(Long memberId, Long id, WishRequest request){
        Wish existingWish = wishRepository.findByIdAndMemberId(id, memberId)
                .orElseThrow(()-> new WishNotFoundException(NOT_FOUND_WISH));

        existingWish.updateQuantity(request.quantity());
    }

    @Transactional(readOnly = true)
    public boolean existsByOptionId(Long optionId){
        return wishRepository.existsByOptionId(optionId);
    }

    @Transactional
    public void deleteByOptionId(Long optionId){
        wishRepository.deleteByOptionId(optionId);
    }
}
