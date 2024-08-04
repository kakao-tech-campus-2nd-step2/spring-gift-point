package gift.order.service;

import gift.exception.IllegalProductException;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import gift.option.model.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import gift.order.dto.OrderRequest;
import gift.order.model.Order;
import gift.order.repository.OrderRepository;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
public class OrderService {

    private final MemberRepository memberRepository;
    private final OptionService optionService;
    private final OptionRepository optionRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(MemberRepository memberRepository, OptionService optionService,
        OptionRepository optionRepository, OrderRepository orderRepository,
        ProductRepository productRepository) {
        this.memberRepository = memberRepository;
        this.optionService = optionService;
        this.optionRepository = optionRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }


    public Page<Order> getOrderListPage(Member member, int page, int size, String property,
        Direction direction) {
        Pageable pageable = PageRequest.of(page, size,
            Sort.by(new Sort.Order(direction, property)));
        return orderRepository.findAllByMember(member, pageable);

    }

    @Transactional
    public Order addOrder(
        Member member,
        OrderRequest orderRequest
    ) {
        var selectedWish = member.getWishList().stream()
            .filter(wish -> wish.getProduct().getId().equals(orderRequest.getProductId()))
            .findFirst().orElse(null);

        // 위시리스트에서 제거
        if (selectedWish != null) {
            member.getWishList().remove(selectedWish);
            memberRepository.save(member);
        }

        // 옵션 수량 업데이트
        try {
            optionService.updateOptionQuantity(orderRequest.getOptionId(),
                orderRequest.getQuantity());
        } catch (Exception e) {
            throw new IllegalArgumentException("옵션 수량 업데이트 중 오류가 발생했습니다: " + e.getMessage());
        }
        System.out.println("포인트");

        // 포인트 적립
        addPoint(member, orderRequest);
        Logger logger = LoggerFactory.getLogger(OrderService.class);
        logger.atInfo().log(member.toString());

        // 5만원 이상이면 10% 할일
        int totalPrice = getTotalPrice(orderRequest,
            productRepository.findById(orderRequest.getProductId())
                .orElseThrow(() -> new IllegalProductException("존재 하지 않는 상품입니다.")));
        if (totalPrice > 50000) {
            totalPrice = (int) (totalPrice * 0.9);
        }

        logger.atInfo().log(orderRequest.toString());
        // 현금 영수증 사용 여부
        if (orderRequest.isCashReceipt()) {
            // 휴대폰 번호 여부 확인
            if (orderRequest.getPhoneNumber() != null) {
                logger.atInfo().log(orderRequest.getPhoneNumber());
                Order order = new Order(orderRequest.getProductId(), orderRequest.getOptionId(),
                    orderRequest.getQuantity(), orderRequest.getMessage(), member, totalPrice,
                    orderRequest.getPhoneNumber());
                return orderRepository.save(order);
            }
        }

        return orderRepository.save(new Order(
            orderRequest.getProductId(), orderRequest.getOptionId(), orderRequest.getQuantity(),
            orderRequest.getMessage(), member, totalPrice, ""
        ));
    }

    public void addPoint(Member member, OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.getProductId())
            .orElseThrow(() -> new IllegalProductException("존재 하지 않는 상품입니다."));

        int totalPrice = getTotalPrice(orderRequest, product);

        int point = totalPrice / 100;
        member.addPoint(point);
    }

    private int getTotalPrice(OrderRequest orderRequest, Product product) {
        return product.getPrice() * orderRequest.getQuantity();
    }

    public void discountCharge(OrderRequest orderRequest) {
        Product product = productRepository.findById(orderRequest.getProductId())
            .orElseThrow(() -> new IllegalProductException("존재 하지 않는 상품입니다."));

    }


    public MultiValueMap<String, String> makeOrderMessage() {
        // Create the template object as a JSON string
        String templateObject =
            "{" + "\"object_type\": \"feed\"," + "\"content\": {" + "  \"title\": \"오늘의 디저트\","
                + "  \"description\": \"아메리카노, 빵, 케익\","
                + "  \"image_url\": \"https://mud-kage.kakao.com/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg\","
                + "  \"image_width\": 640," + "  \"image_height\": 640," + "  \"link\": {"
                + "    \"web_url\": \"http://www.daum.net\","
                + "    \"mobile_web_url\": \"http://m.daum.net\","
                + "    \"android_execution_params\": \"contentId=100\","
                + "    \"ios_execution_params\": \"contentId=100\"" + "  }" + "},"
                + "\"item_content\": {" + "  \"profile_text\": \"Kakao\","
                + "  \"profile_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\","
                + "  \"title_image_url\": \"https://mud-kage.kakao.com/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png\","
                + "  \"title_image_text\": \"Cheese cake\","
                + "  \"title_image_category\": \"Cake\"," + "  \"items\": ["
                + "    {\"item\": \"Cake1\", \"item_op\": \"1000원\"},"
                + "    {\"item\": \"Cake2\", \"item_op\": \"2000원\"},"
                + "    {\"item\": \"Cake3\", \"item_op\": \"3000원\"},"
                + "    {\"item\": \"Cake4\", \"item_op\": \"4000원\"},"
                + "    {\"item\": \"Cake5\", \"item_op\": \"5000원\"}" + "  ],"
                + "  \"sum\": \"Total\"," + "  \"sum_op\": \"15000원\"" + "}," + "\"social\": {"
                + "  \"like_count\": 100," + "  \"comment_count\": 200,"
                + "  \"shared_count\": 300," + "  \"view_count\": 400,"
                + "  \"subscriber_count\": 500" + "}," + "\"buttons\": [" + "  {"
                + "    \"title\": \"웹으로 이동\"," + "    \"link\": {"
                + "      \"web_url\": \"http://www.daum.net\","
                + "      \"mobile_web_url\": \"http://m.daum.net\"" + "    }" + "  }," + "  {"
                + "    \"title\": \"앱으로 이동\"," + "    \"link\": {"
                + "      \"android_execution_params\": \"contentId=100\","
                + "      \"ios_execution_params\": \"contentId=100\"" + "    }" + "  }" + "]" + "}";

        MultiValueMap<String, String> bodyTemplate = new LinkedMultiValueMap<>();
        bodyTemplate.add("template_object", templateObject);
        return bodyTemplate;
    }
}
