package gift.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gift.config.KakaoProperties;
import gift.dto.InputProductDTO;
import gift.dto.OrderDTO;
import gift.model.BearerToken;
import gift.service.KakaoAuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private static final Logger logger = LoggerFactory.getLogger(KakaoController.class);

    private final KakaoAuthService kakaoAuthService;

    public OrderController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping
    public void orderProduct(HttpServletRequest request, @RequestBody OrderDTO orderDTO) throws JsonProcessingException {
        BearerToken token = (BearerToken) request.getAttribute("bearerToken");
        kakaoAuthService.orderProduct(token.getToken(), orderDTO);
    }
}
