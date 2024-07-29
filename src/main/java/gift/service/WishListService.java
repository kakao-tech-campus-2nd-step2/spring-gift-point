package gift.service;

import gift.dto.betweenClient.member.MemberDTO;
import gift.dto.betweenClient.product.ProductRequestDTO;
import gift.dto.betweenClient.wish.WishDTO;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.NoSuchProductIdException;
import gift.exception.BadRequestExceptions.UserNotFoundException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.MemberRepository;
import gift.repository.WishRepository;
import gift.util.validator.databaseValidator.MemberDatabaseValidator;
import gift.util.validator.databaseValidator.ProductDatabaseValidator;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final MemberRepository memberRepository;
    private final WishRepository wishRepository;
    private final MemberDatabaseValidator memberDatabaseValidator;
    private final ProductDatabaseValidator productDatabaseValidator;

    public WishListService(MemberRepository memberRepository, WishRepository wishRepository,
            MemberDatabaseValidator memberDatabaseValidator, ProductDatabaseValidator productDatabaseValidator) {
        this.memberRepository = memberRepository;
        this.wishRepository = wishRepository;
        this.memberDatabaseValidator = memberDatabaseValidator;
        this.productDatabaseValidator = productDatabaseValidator;
    }

    @Transactional(readOnly = true)
    public Page<WishDTO> getWishList(MemberDTO memberDTO, Pageable pageable) throws RuntimeException {
        try {
            Member member = memberRepository.findByEmail(memberDTO.getEmail())
                    .orElseThrow(() -> new UserNotFoundException("해당 이메일을 가지는 유저를 찾을 수 없습니다."));
            Page<Wish> wishPage = wishRepository.findByMember(member, pageable);
            return wishPage.map(WishDTO::convertToWishDTO);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }


    @Transactional
    public void addWishes(MemberDTO memberDTO, ProductRequestDTO productRequestDTO) {
        try {
            Member member = memberDatabaseValidator.validateMember(memberDTO);
            Product product = productDatabaseValidator.validateProduct(productRequestDTO);

            Optional<Wish> optionalWish = wishRepository.findByMemberAndProduct(member, product);
            if (optionalWish.isEmpty()) {
                Wish wish = new Wish(member, product, 1);
                wishRepository.save(wish);
                return;
            }
            Wish existingWish = optionalWish.get();
            existingWish.incrementQuantity();
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void removeWishListProduct(MemberDTO memberDTO, Long id)
            throws NoSuchProductIdException, EmptyResultDataAccessException {
        try {
            Member member = memberDatabaseValidator.validateMember(memberDTO);
            Product product = productDatabaseValidator.validateProduct(id);
            wishRepository.deleteByMemberAndProductId(member, product.getId());
        } catch (NoSuchProductIdException e) { //제품 목록에는 없는데 유저는 존재하는 경우
            wishRepository.deleteByMemberAndProductId(memberDatabaseValidator.validateMember(memberDTO), id);
        } catch(BadRequestException e) {
            throw e;
        } catch (Exception e){
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void setWishListNumber(MemberDTO memberDTO, ProductRequestDTO productRequestDTO, Integer quantity) throws RuntimeException {
        try {
            Member member = memberDatabaseValidator.validateMember(memberDTO);
            Product product = productDatabaseValidator.validateProduct(productRequestDTO);
            Optional<Wish> optionalWish = wishRepository.findByMemberAndProduct(member, product);
            if (optionalWish.isEmpty()) {
                throw new BadRequestException("위시리스트에 그러한 품목을 찾을 수 없습니다.");
            }
            Wish wish = optionalWish.get();
            wish.changeQuantity(quantity);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

}



