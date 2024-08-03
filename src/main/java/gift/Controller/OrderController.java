package gift.Controller;

import gift.DTO.MemberDto;
import gift.LoginUser;
import gift.ResponseDto.RequestOrderDto;
import gift.ResponseDto.ResponseOrderDto;
import gift.Service.OrderService;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<ResponseOrderDto> orderOption(@RequestBody RequestOrderDto requestOrderDto,
    @LoginUser MemberDto memberDto) throws IllegalAccessException {
    ResponseOrderDto responseOrderDto = orderService.orderOption(requestOrderDto, memberDto);
    return ResponseEntity.created(URI.create("/orders")).body(responseOrderDto);
  }
}
