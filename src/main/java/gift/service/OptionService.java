package gift.service;

import gift.entity.option.Option;
import gift.entity.option.OptionDTO;
import gift.entity.user.User;
import gift.exception.ResourceNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class OptionService {

    private final OptionRepository optionRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public OptionService(OptionRepository optionRepository, UserService userService, UserRepository userRepository) {
        this.optionRepository = optionRepository;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Transactional
    public Option save(OptionDTO optionDTO, String email) {
        User user = userService.findOne(email);
        Option option = new Option(optionDTO);
        option.setUser(user);
        return optionRepository.save(option);
    }

    public Option findById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + id));
    }

    public List<Option> findAll() {
        return optionRepository.findAll();
    }

    @Transactional
    public Option update(Long id, OptionDTO optionDTO, String email) {
        if (!optionMatchesUser(id, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Option option = findById(id);
        option.setOptionDTO(optionDTO);

        return optionRepository.save(option);
    }

    @Transactional
    public void delete(Long id, String email) {
        if (!optionMatchesUser(id, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Option option = findById(id);
        option.setUser(null);

        optionRepository.deleteById(id);
    }

    @Transactional
    public Option subtract(Long id, int amount) {
        Option option = findById(id);
        if (amount <= 0) {
            throw new IllegalArgumentException("Invalid quantity");
        }
        if (option.getQuantity() < amount) {
            throw new IllegalArgumentException("Not enough quantity");
        }
        option.subtract(amount);
        return optionRepository.save(option);
    }

    public boolean optionMatchesUser(Long id, String email) {
        User user = userService.findOne(email);
        Option option = findById(id);
        return user.getId() == option.getUser().getId();
    }
}
