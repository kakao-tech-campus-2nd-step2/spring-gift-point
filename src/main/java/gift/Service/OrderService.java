package gift.Service;

import gift.DTO.Member;
import gift.DTO.MemberDto;
import gift.DTO.Option;
import gift.DTO.Orders;
import gift.Repository.MemberRepository;
import gift.Repository.OptionRepository;
import gift.Repository.OrderRepository;
import gift.Repository.ProductRepository;
import gift.ResponseDto.RequestOrderDto;
import gift.ResponseDto.ResponseOrderDto;
import jakarta.transaction.Transactional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

  private final String URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
  private final OptionService optionService;
  private final WishListService wishListService;
  private final ProductRepository productRepository;
  private final OptionRepository optionRepository;
  private final OrderRepository orderRepository;

  private final MemberRepository memberRepository;

  public OrderService(OptionService optionService, ProductRepository productRepository,
    WishListService wishListService, MemberRepository memberRepository,
    OptionRepository optionRepository, OrderRepository orderRepository) {
    this.optionService = optionService;
    this.productRepository = productRepository;
    this.wishListService = wishListService;
    this.optionRepository = optionRepository;
    this.orderRepository = orderRepository;
    this.memberRepository=memberRepository;
  }

  @Transactional
  public ResponseOrderDto orderOption(RequestOrderDto requestOrderDto, MemberDto memberDto)
    throws IllegalAccessException {

    Option option = optionRepository.findById(requestOrderDto.getOptionId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 상품이 없습니다", 1));

    optionService.optionQuantitySubtract(option.getId(), requestOrderDto.getQuantity());

    Orders order = new Orders(option, requestOrderDto.getQuantity(), requestOrderDto.getMessage(),
      requestOrderDto.getUsedPoint());

    Long memberId = memberDto.getId();
    Member member = memberRepository.findById(memberId)
      .orElseThrow(()->new EmptyResultDataAccessException("해당 고객이 없습니다.",1));
    member.subtractPoint(requestOrderDto.getUsedPoint());
    Orders savedOrder = orderRepository.save(order);
    ResponseOrderDto responseOrderDto = new ResponseOrderDto(savedOrder.getId(),
      requestOrderDto.getOptionId(), requestOrderDto.getQuantity(),
      savedOrder.getOrderDateTime(), requestOrderDto.getMessage());
    return responseOrderDto;
  }
}
