package gift.service;

import gift.domain.member.Member;
import gift.domain.member.MemberRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.wish.Wish;
import gift.domain.wish.WishRepository;
import gift.dto.OrderRequestDto;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.infra.TemplateObject;
import gift.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;

@Service
public class OrderService {
    private final OptionRepository optionRepository;
    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final RestClient restClient;
    private final Logger log = LoggerFactory.getLogger(getClass());


    private final static String ORDER_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    public OrderService(OptionRepository optionRepository, WishRepository wishRepository, MemberRepository memberRepository, RestClient.Builder builder) {
        this.optionRepository = optionRepository;
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.restClient = builder.build();
    }

    @Transactional
    public void handleOrder(Long memberId, OrderRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MEMBER, memberId));
        Option option = optionRepository.findById(request.optionId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_OPTION, request.optionId()));

        int totalPoint = calculateTotalPrice(request, option);

        if ((option.checkValidateQuantity(request.quantity()) && member.checkPoint(totalPoint))) {
            option.subtract(request.quantity());
            member.buyProduct(totalPoint);
        }
        deleteWish(memberId, option);
        sendMessage(member.getAccessToken());
    }

    private int calculateTotalPrice(OrderRequestDto request, Option option) {
        return (int) (option.getProduct().getPrice() * request.quantity() * 0.95);
    }

    private void deleteWish(Long memberId, Option option) {
        Optional<Wish> wish = wishRepository.findByMemberIdAndProductId(memberId, option.getProduct().getId());
        wish.ifPresent(wishRepository::delete);
    }

    private void sendMessage(String accessToken) {
        String templateObjectJson = createJsonMessage();
        var body = new LinkedMultiValueMap<String, String>();
        body.add("template_object", templateObjectJson);
        var response = restClient.post()
                .uri(URI.create(ORDER_URI))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    log.error("{}: msg : {}", res.getStatusCode(), res.getHeaders());
                    throw new RuntimeException("주문 처리 중 에러가 발생하였습니다.");
                });
        log.info(response.toString());
    }

    private String createJsonMessage() {
        TemplateObject templateObject = new TemplateObject("text", "상품 주문",
                new TemplateObject.Link("https://gift.kakao.com/home", "https://gift.kakao.com/home"));
        return JsonUtil.toJson(templateObject);
    }
}
