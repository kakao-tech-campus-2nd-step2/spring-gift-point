package gift.service;

import org.springframework.stereotype.Service;

import gift.entity.User;
import gift.exception.InvalidPointException;
import gift.repository.UserRepository;

@Service
public class PointService {

    private final UserService userService;
    private final UserRepository userRepository;

    public PointService(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public void chargePoints(Long userId, int points) {
        User user = userService.findById(userId);
        user.addPoints(points);
        userRepository.save(user);
    }

    public int deductPoints(Long userId, int totalPrice) {
        User user = userService.findById(userId);
        int maxDeductiblePoints = (int) (totalPrice * 0.1);
        int pointsToUse = Math.min(user.getPoints(), maxDeductiblePoints);

        if (user.getPoints() < pointsToUse) {
            throw new InvalidPointException("Not enough points.");
        }

        user.deductPoints(pointsToUse);
        userRepository.save(user);
        return pointsToUse;
    }

    public void addPoints(Long userId, int totalPrice) {
        User user = userService.findById(userId);
        int pointsToEarn = calculatePointsToEarn(totalPrice);
        user.addPoints(pointsToEarn);
        userRepository.save(user);
    }

    public int calculatePointsToEarn(int totalPrice) {
        return (int) (totalPrice * 0.1);
    }

    public boolean hasSufficientPoints(Long userId, int totalPrice) {
        User user = userService.findById(userId);
        int maxDeductiblePoints = (int) (totalPrice * 0.1);
        return user.getPoints() >= maxDeductiblePoints;
    }
}
