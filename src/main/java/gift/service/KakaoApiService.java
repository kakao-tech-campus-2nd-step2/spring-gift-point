package gift.service;

import gift.controller.dto.KakaoApiDTO;
import gift.controller.dto.KakaoApiDTO.KakaoOrderResponse;
import gift.controller.dto.KakaoTokenDto;
import gift.controller.dto.TokenResponseDTO;
import gift.domain.Option;
import gift.domain.Order;
import gift.domain.Token;
import gift.domain.UserInfo;
import gift.domain.Wish;
import gift.repository.OptionRepository;
import gift.repository.OrderRepository;
import gift.repository.TokenRepository;
import gift.repository.UserInfoRepository;
import gift.repository.WishRepository;
import gift.utils.ExternalApiService;
import gift.utils.JwtTokenProvider;
import gift.utils.config.KakaoProperties;
import gift.utils.error.KakaoLoginException;
import gift.utils.error.OptionNotFoundException;
import gift.utils.error.TokenAuthException;
import gift.utils.error.UserNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class KakaoApiService {
    private final ExternalApiService externalApiService;
    private final KakaoProperties kakaoProperties;
    private final OptionRepository optionRepository;
    private final UserInfoRepository userInfoRepository;
    private final TokenRepository tokenRepository;
    private final OrderRepository orderRepository;
    private final WishRepository wishRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public KakaoApiService(ExternalApiService externalApiService, KakaoProperties kakaoProperties,
        OptionRepository optionRepository, UserInfoRepository userInfoRepository,
        TokenRepository tokenRepository, OrderRepository orderRepository,
        WishRepository wishRepository,
        JwtTokenProvider jwtTokenProvider) {
        this.externalApiService = externalApiService;
        this.kakaoProperties = kakaoProperties;
        this.optionRepository = optionRepository;
        this.userInfoRepository = userInfoRepository;
        this.tokenRepository = tokenRepository;
        this.orderRepository = orderRepository;
        this.wishRepository = wishRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    public String createKakaoCode(){
        String kakaoAuthUrl = "https://kauth.kakao.com/oauth/authorize";

        return UriComponentsBuilder.fromHttpUrl(kakaoAuthUrl)
            .queryParam("client_id", kakaoProperties.getRestApiKey())
            .queryParam("redirect_uri", kakaoProperties.getRedirectUri())
            .queryParam("response_type", "code")
            .toUriString();
    }
    @Transactional
    public TokenResponseDTO createKakaoToken(String code,
        String error,
        String error_description,
        String state){
        if (code==null && error!=null){
            throw new KakaoLoginException(error_description);
        }
        ResponseEntity<KakaoTokenDto> kakaoTokenDtoResponseEntity = externalApiService.postKakaoToken(
            code);
        String s = externalApiService.postKakaoUserId(
            kakaoTokenDtoResponseEntity.getBody().getAccessToken());

        String email = s+"@kakao.com";

        Optional<UserInfo> byEmail = userInfoRepository.findByEmail(email);
        if (byEmail.isEmpty()){
            UserInfo userInfo = new UserInfo(email, email);
            userInfoRepository.save(userInfo);
        }
        Token token = new Token(email, kakaoTokenDtoResponseEntity.getBody().getAccessToken(),
            kakaoTokenDtoResponseEntity.getBody().getRefreshToken(),kakaoTokenDtoResponseEntity.getBody().getExpiresIn()
            ,kakaoTokenDtoResponseEntity.getBody().getRefreshTokenExpiresIn(),LocalDateTime.now());
        tokenRepository.save(token);

        return new TokenResponseDTO(jwtTokenProvider.createToken(email,kakaoTokenDtoResponseEntity.getBody().getExpiresIn()));

    }

    @Transactional
    public KakaoApiDTO.KakaoOrderResponse kakaoOrder(KakaoApiDTO.KakaoOrderRequest kakaoOrderRequest,String jwttoken){
        Option option = optionRepository.findById(kakaoOrderRequest.optionId()).orElseThrow(
            () -> new OptionNotFoundException("Option Not Found")
        );
        String emailFromToken = jwtTokenProvider.getEmailFromToken(jwttoken);

        Token byToken = tokenRepository.findByEmail(emailFromToken).orElseThrow(
            () -> new TokenAuthException("Token not found user")
        );
        String accesstoken = byToken.getAccesstoken();

        UserInfo userInfo = userInfoRepository.findByEmail(emailFromToken).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );


        Order order = new Order(kakaoOrderRequest.quantity(),
            LocalDateTime.now(), kakaoOrderRequest.message());

        userInfo.addOrder(order);
        option.addOrder(order);

        Optional<Wish> byUserInfoIdAndProductId = wishRepository.findByUserInfoIdAndProductId(
            userInfo.getId(), option.getProduct().getId());

        byUserInfoIdAndProductId.ifPresent(wishRepository::delete);

        option.subtract(kakaoOrderRequest.quantity());

        orderRepository.save(order);

        KakaoOrderResponse kakaoOrderResponse = new KakaoOrderResponse(order.getId(),
            option.getId(), order.getQuantity(), order.getOrderDateTime(),
            order.getMessage());

        externalApiService.postSendMe(kakaoOrderResponse,accesstoken);

        return kakaoOrderResponse;

    }

    @Transactional
    public KakaoApiDTO.KakaoOrderResponse Order(KakaoApiDTO.KakaoOrderRequest kakaoOrderRequest,String jwttoken){
        Option option = optionRepository.findById(kakaoOrderRequest.optionId()).orElseThrow(
            () -> new OptionNotFoundException("Option Not Found")
        );
        String emailFromToken = jwtTokenProvider.getEmailFromToken(jwttoken);

        UserInfo userInfo = userInfoRepository.findByEmail(emailFromToken).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        Order order = new Order(kakaoOrderRequest.quantity(),
            LocalDateTime.now(), kakaoOrderRequest.message());

        userInfo.addOrder(order);
        option.addOrder(order);

        Optional<Wish> byUserInfoIdAndProductId = wishRepository.findByUserInfoIdAndProductId(
            userInfo.getId(), option.getProduct().getId());

        byUserInfoIdAndProductId.ifPresent(wishRepository::delete);

        option.subtract(kakaoOrderRequest.quantity());

        orderRepository.save(order);

        KakaoOrderResponse kakaoOrderResponse = new KakaoOrderResponse(order.getId(),
            option.getId(), order.getQuantity(), order.getOrderDateTime(),
            order.getMessage());

        return kakaoOrderResponse;

    }

    @Transactional
    public Page<KakaoApiDTO.KakaoOrderResponse> kakaoGetOrder(Pageable pageable){
        Page<Order> all = orderRepository.findAll(pageable);
        return all.map(this::convertToOrderDto);
    }

    private KakaoApiDTO.KakaoOrderResponse convertToOrderDto(Order order){
        return new KakaoOrderResponse(order.getId(),order.getOption().getId(),order.getQuantity(),
            order.getOrderDateTime(),order.getMessage());
    }




}
