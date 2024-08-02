package gift.users.wishlist;

import gift.administrator.option.Option;
import gift.administrator.option.OptionDTO;
import gift.administrator.option.OptionService;
import gift.administrator.product.Product;
import gift.administrator.product.ProductDTO;
import gift.administrator.product.ProductService;
import gift.error.NotFoundIdException;
import gift.users.user.User;
import gift.users.user.UserDTO;
import gift.users.user.UserService;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductService productService;
    private final UserService userService;
    private final OptionService optionService;

    public WishListService(WishListRepository wishListRepository, ProductService productService,
        UserService userService, OptionService optionService) {
        this.wishListRepository = wishListRepository;
        this.productService = productService;
        this.userService = userService;
        this.optionService = optionService;
    }

    public Page<WishListDTO> getWishListsByUserId(long id, int page, int size, Direction direction,
        String sortBy) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageRequest = PageRequest.of(page, size, sort);
        Page<WishList> wishListPage = wishListRepository.findAllByUserId(id, pageRequest);
        List<WishListDTO> wishLists = wishListPage.stream()
            .map(WishListDTO::fromWishList)
            .toList();
        return new PageImpl<>(wishLists, pageRequest, wishListPage.getTotalElements());
    }

    public WishListDTO addWishList(WishListDTO wishListDTO, Long userId) {
        UserDTO userDTO = userService.findById(userId);
        User user = userDTO.toUser();
        validateIfProductAndOptionExists(userId, wishListDTO.getProductId(),
            wishListDTO.getOptionId());

        validateProductId(wishListDTO.getProductId());
        validateOptionId(wishListDTO.getProductId(), wishListDTO.getOptionId());

        Product product = toProductByProductIdAndSetOption(wishListDTO.getProductId());

        OptionDTO optionDTO = optionService.findOptionById(wishListDTO.getOptionId());
        Option option = optionDTO.toOption(product);

        WishList wishList = new WishList(user, product, wishListDTO.getQuantity(), option);
        wishListRepository.save(wishList);
        return WishListDTO.fromWishList(wishList);
    }

    private void validateProductId(long productId){
        if(productService.existsByProductId(productId)){
            throw new IllegalArgumentException(productId + " 상품은 존재하지 않습니다.");
        }
    }

    private void validateOptionId(long productId, long optionId) {
        if (!optionService.existsByOptionIdAndProductId(optionId,
            productId)) {
            throw new IllegalArgumentException(
                optionId + " 옵션은 " + productId + " 상품에 존재하지 않는 옵션입니다.");
        }
    }

    public WishListDTO updateWishList(long userId, long wishListId, WishListDTO wishListDTO) {
        validateIfWishListNotExists(userId, wishListId);
        validateIfProductAndOptionExistsAndNotThis(userId, wishListDTO.getProductId(),
            wishListDTO.getOptionId(), wishListId);

        WishList wishList = wishListRepository.findById(wishListId).orElseThrow(() -> new NotFoundIdException("없는 위시리스트입니다."));

        validateProductId(wishListDTO.getProductId());
        validateOptionId(wishListDTO.getProductId(), wishListDTO.getOptionId());

        Product product = toProductByProductIdAndSetOption(wishListDTO.getProductId());

        OptionDTO newOptionDTO = optionService.findOptionById(wishListDTO.getOptionId());
        Option newOption = newOptionDTO.toOption(product);

        wishList.setProduct(product);
        wishList.setOption(newOption);
        wishList.setQuantity(wishListDTO.getQuantity());

        wishListRepository.save(wishList);
        return WishListDTO.fromWishList(wishList);
    }

    private void validateIfProductAndOptionExistsAndNotThis(long userId, long productId, long optionId,
        long wishListId){
        if (wishListRepository.existsByUserIdAndProductIdAndOptionIdAndIdNot(userId,
            productId, optionId, wishListId)) {
            throw new IllegalArgumentException(userId + "의 위시리스트에 존재하는 상품 옵션입니다.");
        }
    }

    private void validateIfWishListNotExists(long userId, long wishListId){
        if(!wishListRepository.existsByIdAndUserId(wishListId, userId)){
            throw new IllegalArgumentException(userId + "의 위시리스트에는 " + wishListId + " 위시리스트가 존재하지 않습니다.");
        }
    }

    private Product toProductByProductIdAndSetOption(long productId) {
        ProductDTO productDTO = productService.getProductById(productId);
        Product product = productDTO.toProduct(productDTO,
            productService.getCategoryById(productDTO.getCategoryId()));
        product.setOption(optionService.getAllOptionsByOptionId(
                productDTO.getOptions().stream().map(OptionDTO::getId).toList()).stream()
            .map(optionDTO -> optionDTO.toOption(product)).toList());
        return product;
    }

    private void validateIfProductAndOptionExists(long userId, long productId, long optionId) {
        if (wishListRepository.existsByUserIdAndProductIdAndOptionId(userId,
            productId, optionId)) {
            throw new IllegalArgumentException(userId + "의 위시리스트에 존재하는 상품 옵션입니다.");
        }
    }

    @Transactional
    public void findOrderAndDeleteIfExists(long userId, long productId, long optionId) {
        if (!wishListRepository.existsByUserIdAndProductIdAndOptionId(userId, productId,
            optionId)) {
            return;
        }
        wishListRepository.deleteByUserIdAndProductIdAndOptionId(userId, productId, optionId);
    }

    public void deleteWishListByWishListId(long userId, long wishListId){
        validateIfWishListNotExists(userId, wishListId);
        wishListRepository.deleteById(wishListId);
    }
}
