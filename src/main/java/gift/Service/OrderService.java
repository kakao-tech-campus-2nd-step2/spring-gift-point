package gift.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gift.Model.Link;
import gift.Model.Member;
import gift.Model.MessageDTO;
import gift.Model.Option;
import gift.Model.OrderRequestDTO;
import gift.Model.OrderResponseDTO;

import gift.Model.Product;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OrderService {
    private final OptionService optionService;
    private final MemberService memberService;
    private final LoginService loginService;
    private final WishlistService wishlistService;
    private final MessageService messageService;
    private final MemberAccessTokenProvider memberAccessTokenProvider;

    private final RestClient client = RestClient.builder().build();

    public OrderService(OptionService optionService, MemberService memberService, LoginService loginService, WishlistService wishlistService, MessageService messageService, MemberAccessTokenProvider memberAccessTokenProvider){
        this.optionService = optionService;
        this.memberService = memberService;
        this.loginService = loginService;
        this.wishlistService = wishlistService;
        this.messageService = messageService;
        this.memberAccessTokenProvider = memberAccessTokenProvider;
    }

    public OrderResponseDTO makeResponse(OrderRequestDTO orderRequestDTO, Long productId){
        //현재 날짜 가져오기
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        // respone 생성
        OrderResponseDTO response = new OrderResponseDTO();
        response.setId(productId);
        response.setOptionId(orderRequestDTO.getOptionId());
        response.setQuantity(orderRequestDTO.getQuantity());
        response.setOrderDateTime(formatter.format(date));
        response.setMessage(orderRequestDTO.getMessage());
        return response;
    }

    public String removeBearer(String jwtToken){
        // beraer jwtToken 상태에서 jwtToken만 얻기
        return jwtToken.substring(6).trim();
    }

    public ResponseEntity<String> sendMessage(String message, String jwtToken){
        String accessToken =  memberService.getMemberByEmail(memberAccessTokenProvider.getEmail(removeBearer(jwtToken))).getAccessToken();
        return messageService.sendMessage(message, accessToken);
    }

    public OrderResponseDTO doOrder(String jwtToken, OrderRequestDTO orderRequestDTO){
        Option option = optionService.getOption(orderRequestDTO.getOptionId());
        // option에 해당하는 product 가져오기
        Product orderProduct = option.getProduct();
        // jwt토큰으로 email을 얻어 member 가져오기
        Member member = memberService.getMemberByEmail(memberAccessTokenProvider.getEmail(removeBearer(jwtToken)));
        // wishlistId가 존재 한다면 wishlist 삭제
        Long wishlistId = wishlistService.getWishlistId(member.getEmail(),orderProduct.getId());
        if (wishlistId != null){
            wishlistService.deleteWishlist(member.getEmail(),orderProduct.getId(),wishlistId);
        }
        // 해당하는 quantity만큼 option에서 빼기
        optionService.subtractQuantity(orderRequestDTO);
        return makeResponse(orderRequestDTO, orderProduct.getId());
    }

}
