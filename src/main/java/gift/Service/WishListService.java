package gift.Service;

import gift.ConverterToDto;
import gift.DTO.Member;
import gift.DTO.Product;
import gift.DTO.WishList;
import gift.DTO.WishListDto;
import gift.Repository.MemberRepository;
import gift.Repository.ProductRepository;
import gift.Repository.WishListRepository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

  private final WishListRepository wishListRepository;
  private final MemberRepository memberRepository;
  private final ProductRepository productRepository;

  public WishListService(WishListRepository wishListRepository, ProductRepository productRepository,
    MemberRepository memberRepository) {
    this.wishListRepository = wishListRepository;
    this.memberRepository = memberRepository;
    this.productRepository = productRepository;
  }

  public void addProductToWishList(Long productId) {
    Product product = productRepository.findById(productId)
      .orElseThrow(()->new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    WishList wishList = new WishList(product);
    wishListRepository.save(wishList);
  }

  public Page<WishListDto> getWishList(Pageable pageable) {

    Page<WishList> wishLists = wishListRepository.findAll(pageable);
    Page<WishListDto> wishListDtos = wishLists.map(ConverterToDto::convertToWishListDto);

    return wishListDtos;
  }

  public void deleteProductToWishList(Long id) {
    WishList wishList = wishListRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));
    wishListRepository.deleteById(id);
  }
}
