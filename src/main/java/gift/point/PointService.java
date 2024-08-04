package gift.point;

import gift.option.Option;
import gift.option.OptionRepository;
import gift.order.OrderRequest;
import gift.user.KakaoUser;
import gift.user.User;
import gift.user.UserService;
import gift.user.UserType;
import org.springframework.stereotype.Service;

@Service
public class PointService {

    private final OptionRepository optionRepository;
    private final UserService userService;

    public PointService(OptionRepository optionRepository, UserService userService) {
        this.optionRepository = optionRepository;
        this.userService = userService;
    }


    public void validatePoint(Long point, Long usePoint, Long sum){
        if(point < usePoint) throw new IllegalArgumentException("사용하려는 포인트가 현재 보유한 포인트를 초과합니다.");
        if(sum < usePoint) throw new IllegalArgumentException("사용하려는 포인트가 상품 가격을 초과합니다.");
    }

    public void usePoint(KakaoUser user, OrderRequest request){
        Long point = user.getPoint();
        Long usePoint = request.getPoint();
        Long sum = request.getQuantity() * getPrice(request.getOptionId());

        validatePoint(point, usePoint, sum);

        user.usePoint(usePoint);
        sum -= usePoint;
        user.chargePoint((long) (sum * 0.05));

        user.getUser().setPoint(user.getPoint());
    }

    public Long getPrice(Long optionID){
        Option option = optionRepository.findById(optionID).orElseThrow();
        return option.getProduct().getPrice();
    }

    public void chargeUserPoint(Long userId, PointDTO point){
        User user = userService.getUserById(userId);
        user.chargePoint(point.getPoint());
        if(user.getUserType() == UserType.KAKAO_USER){
            KakaoUser kakaoUser = userService.getKakaoUserByUser(user);
            kakaoUser.chargePoint(point.getPoint());
        }
    }
}
