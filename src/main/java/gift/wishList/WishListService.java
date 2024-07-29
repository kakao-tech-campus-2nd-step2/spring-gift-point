package gift.wishList;

import gift.option.Option;
import gift.option.OptionRepository;
import gift.product.Product;
import gift.user.IntegratedUser;
import gift.user.KakaoUser;
import gift.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final OptionRepository optionRepository;

    public WishListService(WishListRepository wishListRepository, OptionRepository optionRepository) {
        this.wishListRepository = wishListRepository;
        this.optionRepository = optionRepository;
    }

    public WishListResponse addWish(WishListRequest wishListDTO, IntegratedUser user) {
        Option option = optionRepository.findById(wishListDTO.getOptionID()).orElseThrow();
        Product product = option.getProduct();
        Optional<WishList> exist = Optional.empty();
        if(user instanceof User){
            exist = wishListRepository.findByUserAndOptionId((User) user, option.getId());
        }
        if(user instanceof KakaoUser){
            exist = wishListRepository.findByKakaouserAndOptionId((KakaoUser) user, option.getId());
        }
        if(exist.isPresent()){
            return updateCount(new CountDTO(wishListDTO.count + exist.get().getCount()), exist.get().getId());
        }

        WishList wishList = new WishList(wishListDTO.getCount());
        user.addWishList(wishList);
        option.addWishList(wishList);
        product.addWishList(wishList);
        wishListRepository.save(wishList);
        return new WishListResponse(wishList);
    }

    public List<WishListResponse> findByUser(User user) {
        List<WishList> wishLists = wishListRepository.findByUser(user);
        List<WishListResponse> wishListDTOS = new ArrayList<>();
        wishLists.forEach((wishList -> wishListDTOS.add(new WishListResponse(wishList))));
        return wishListDTOS;
    }

    public List<WishListResponse> findByIntegratedUser(IntegratedUser user) {
        if(user instanceof User) return findByUser((User) user);
        if(user instanceof KakaoUser) return findByKakaoUser((KakaoUser) user);
        return null;
    }

    public List<WishListResponse> findByKakaoUser(KakaoUser kakaoUser) {
        List<WishList> wishLists = wishListRepository.findByKakaouser(kakaoUser);
        List<WishListResponse> wishListDTOS = new ArrayList<>();
        wishLists.forEach((wishList -> wishListDTOS.add(new WishListResponse(wishList))));
        return wishListDTOS;
    }

    public WishListResponse updateCount(CountDTO count, Long id) {
        WishList wishList = wishListRepository.findById(id).orElseThrow();
        wishList.setCount(count.getCount());
        return new WishListResponse(wishList);
    }

    public Optional<WishList> findByKakaoUserAndOptionID(Long optionID, KakaoUser kakaoUser){
        return wishListRepository.findByKakaouserAndOptionId(kakaoUser, optionID);
    }

    public void deleteByID(Long id, IntegratedUser user) {
        WishList wishList = wishListRepository.findById(id).orElseThrow();
        if(user instanceof User){
            wishList.getUser().removeWishList(wishList);
        }
        if(user instanceof KakaoUser){
            wishList.getKakaouser().removeWishList(wishList);
        }

        wishList.getOption().removeWishList(wishList);
        wishListRepository.deleteById(id);
    }

    public Page<WishListResponse> getWishListsPages(int pageNum, int size, IntegratedUser user, String sortBy, String sortDirection) {
        Pageable pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.asc(sortBy)));
        if (Objects.equals(sortDirection, "desc")) {
            pageable = PageRequest.of(pageNum, size, Sort.by(Sort.Order.desc(sortBy)));
        }

        if(user instanceof User) {
            return wishListRepository.findByUser((User) user, pageable).map(WishListResponse::new);
        }
        if(user instanceof KakaoUser) {
            return wishListRepository.findByKakaouser((KakaoUser) user, pageable).map(WishListResponse::new);
        }
        return null;

    }
}
