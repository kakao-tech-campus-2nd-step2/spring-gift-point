package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.model.Member;
import gift.model.Order;
import gift.service.MemberService;
import gift.service.OptionService;
import gift.service.OrderService;
import gift.service.WishlistService;
import gift.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    @Autowired
    private final OptionService optionService;

    @Autowired
    private final WishlistService wishlistService;
    @Autowired
    private OrderService orderService;

    public OrderController(OptionService optionService, MemberService memberService, WishlistService wishlistService) {
        this.optionService = optionService;
        this.wishlistService = wishlistService;
        this.memberService = memberService;
        this.jwtUtil = new JwtUtil();
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@RequestHeader("Authorization") String token,@RequestBody Order order)
        throws JsonProcessingException {
        orderService.save(order);
        Claims claims = jwtUtil.extractClaims(token.replace("Bearer ", ""));
        Long memberId = Long.parseLong(claims.getSubject());
        Member member = memberService.getMemberById(memberId);// 토큰에서 멤버 정보 추출
        orderService.sendMessage(order, member);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(HttpHeaders.AUTHORIZATION, token)
            .body(order);
    }

}
