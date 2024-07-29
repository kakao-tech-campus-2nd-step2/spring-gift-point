package gift.service;

import gift.dto.WishPageResponseDTO;
import gift.dto.WishRequestDTO;

import gift.exceptions.CustomException;
import gift.model.Option;
import gift.model.User;
import gift.model.Wish;

import gift.repository.OptionRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class WishService {
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    public WishService(WishRepository wishRepository, UserRepository userRepository, OptionRepository optionRepository) {
        this.wishRepository = wishRepository;
        this.userRepository = userRepository;
        this.optionRepository = optionRepository;
    }

    public void addWishOption(Long userId, WishRequestDTO wishRequestDTO) {
        User user = userRepository.findById(userId).orElseThrow(CustomException::userNotFoundException);
        Option option = optionRepository.findById(wishRequestDTO.optionId()).orElseThrow(CustomException::optionNotFoundException);
        Wish wish = new Wish(user, option);

        wishRepository.save(wish);
    }


    public WishPageResponseDTO getWishlist(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(CustomException::userNotFoundException);
        Page<Wish> pages = wishRepository.findAllByUser(user, pageable);

        return new WishPageResponseDTO(pages.getContent(),
                pages.getNumber(),
                pages.getTotalPages());
    }

    @Transactional
    public void deleteWishOption(Long userId, Long optionId) {
        User user = userRepository.findById(userId).orElseThrow(CustomException::userNotFoundException);
        Option option = optionRepository.findById(optionId).orElseThrow(CustomException::optionNotFoundException);

        wishRepository.deleteByUserAndOption(user, option);
    }
}
