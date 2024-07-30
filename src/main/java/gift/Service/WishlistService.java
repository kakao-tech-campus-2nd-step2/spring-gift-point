package gift.Service;

import gift.Entity.Member;
import gift.Entity.Wishlist;
import gift.Mapper.Mapper;
import gift.Model.MemberDto;
import gift.Model.WishlistDto;
import gift.Repository.WishlistJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistService {

    private final WishlistJpaRepository wishlistJpaRepository;
    private final Mapper mapper;

    @Autowired
    public WishlistService(WishlistJpaRepository wishlistJpaRepository, Mapper mapper) {
        this.wishlistJpaRepository = wishlistJpaRepository;
        this.mapper = mapper;
    }

    public List<Wishlist> getWishlist(long memberId) {
        return wishlistJpaRepository.findByIdMemberId(memberId);
    }

    public Page<WishlistDto> getWishlistByPage(MemberDto memberDto, Pageable pageable) {
        Member member = mapper.memberDtoToEntity(memberDto);
        Page<Wishlist> wishlist = wishlistJpaRepository.findByMember(member, pageable);
        return wishlist.map(mapper::wishlistToDto);
    }

    public void addWishlistItem(WishlistDto wishlistDto) {
        wishlistDto.setPrice(wishlistDto.getPrice() * wishlistDto.getCount());
        Wishlist wishlist = mapper.wishlistDtoToEntity(wishlistDto);
        wishlistJpaRepository.save(wishlist);
    }

    public void removeWishlistItem(WishlistDto wishListItem, Wishlist wishlistOptional) {
        int newCount = wishListItem.getCount() - wishListItem.getQuantity();

        if (newCount > 0) {
            wishlistOptional.setCount(newCount);
            wishlistOptional.setPrice(wishlistOptional.getPrice() / wishListItem.getCount() * newCount);
            wishlistJpaRepository.save(wishlistOptional);
            return;
        }

        wishlistJpaRepository.delete(wishlistOptional);

    }

    @Transactional
    public void clearWishlist(long memberId) {
        wishlistJpaRepository.deleteByIdMemberId(memberId);
    }

}
