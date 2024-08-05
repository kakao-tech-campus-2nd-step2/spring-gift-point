package gift.member.points;

import org.springframework.stereotype.Component;

@Component
public class PercentageBasedPointsStrategy implements PointsStrategy {

    private final Integer FIVE_PERCENT = 5;

    private final Integer percentage;

    public PercentageBasedPointsStrategy() {
        this.percentage = FIVE_PERCENT;
    }

    @Override
    public Integer calculatePointsToAdd(Integer price) {
        return price * percentage / 100;
    }
}
