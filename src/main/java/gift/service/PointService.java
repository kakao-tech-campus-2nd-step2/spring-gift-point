package gift.service;

import gift.dto.point.PointResponseDTO;
import gift.exceptions.CustomException;
import gift.model.Option;
import gift.model.User;
import gift.repository.OptionRepository;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointService {
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    public PointService(UserRepository userRepository, OptionRepository optionRepository) {
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public PointResponseDTO chargePoint(Long userId, Long point) {
        User user = userRepository.findById(userId).orElseThrow(CustomException::userNotFoundException);
        user.chargePoint(point);
        User updatedUser = userRepository.save(user);

        return new PointResponseDTO(updatedUser.getPoint());
    }

    public void subtractPoint(Long userId, Long optionId, Long quantity) {
        User user = userRepository.findById(userId).orElseThrow(CustomException::userNotFoundException);
        Option option = optionRepository.findById(optionId).orElseThrow(CustomException::optionNotFoundException);
        Long price = option.getProduct().getPrice();

        user.subtractPoint(price, quantity);
        userRepository.save(user);
    }
}
