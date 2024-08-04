package gift.service;

import gift.dto.option.OptionRequestDTO;
import gift.dto.option.OptionResponseDto;
import gift.entity.Option;
import gift.entity.User;
import gift.exception.ResourceNotFoundException;
import gift.repository.OptionRepository;
import gift.repository.UserRepository;
import jakarta.persistence.LockModeType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

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
    public Option save(OptionRequestDTO optionRequestDTO, String email) {
        User user = userService.findOne(email);
        Option option = new Option(optionRequestDTO);
        option.setUser(user);
        return optionRepository.save(option);
    }

    public Option findById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Option not found with id: " + id));
    }

    public List<OptionResponseDto> findAll() {
        return optionRepository.findAll()
                .stream()
                .map(option -> new OptionResponseDto(option)).collect(Collectors.toList());
    }

    @Transactional
    public Option update(Long id, OptionRequestDTO optionRequestDTO, String email) {
        if (!optionMatchesUser(id, email)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        Option option = findById(id);
        option.setOptionDTO(optionRequestDTO);

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

    @Lock(LockModeType.OPTIMISTIC)
    public synchronized void subtract(Long id, int amount) {
        Option option = optionRepository.findById(id)
                .orElseThrow();
        option.subtract(amount);
        optionRepository.saveAndFlush(option);
    }

    public boolean optionMatchesUser(Long id, String email) {
        User user = userService.findOne(email);
        Option option = findById(id);
        return user.getId() == option.getUser().getId();
    }
}
