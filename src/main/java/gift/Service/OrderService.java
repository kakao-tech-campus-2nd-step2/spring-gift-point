package gift.Service;

import gift.DTO.KakaoJwtToken;
import gift.DTO.OptionDto;
import gift.DTO.OrderDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.DTO.WishList;
import gift.KakaoApi;
import gift.Repository.KakaoJwtTokenRepository;
import gift.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final String URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
  private final OptionService optionService;
  private final ProductRepository productRepository;
  private final WishListService wishListService;
  private final KakaoJwtTokenRepository kakaoJwtTokenRepository;
  private final KakaoApi kakaoApi;

  public OrderService(OptionService optionService, ProductRepository productRepository,
    WishListService wishListService, KakaoJwtTokenRepository kakaoJwtTokenRepository,
    KakaoApi kakaoApi) {
    this.optionService = optionService;
    this.productRepository = productRepository;
    this.wishListService = wishListService;
    this.kakaoJwtTokenRepository = kakaoJwtTokenRepository;
    this.kakaoApi = kakaoApi;
  }

  @Transactional
  public OrderDto orderOption(OrderDto orderDto) {
    OptionDto optionDto = orderDto.getOptionDto();
    optionService.optionQuantitySubtract(optionDto, orderDto.getQuantity());
    ProductDto productDto = optionDto.getProductDto();
    Product product = productRepository.findById(productDto.getId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다", 1));
    List<WishList> wishLists = product.getWishlists();
    for (WishList wishList : wishLists) {
      wishListService.deleteProductToWishList(wishList.getId());
    }

    KakaoJwtToken kakaoJwtToken = kakaoJwtTokenRepository.findById(1L)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));
    kakaoApi.kakaoSendMe(orderDto, kakaoJwtToken, URL);

    return orderDto;
  }


}
