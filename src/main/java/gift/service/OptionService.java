package gift.service;

import gift.dto.OptionDto;
import gift.dto.response.OptionResponse;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OptionService {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;


    public Option getOptionByName(String name) {
        return optionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Option not found with name " + name));
    }

    public Set<Option> getAllOptions() {
        return new HashSet<>(optionRepository.findAll());
    }

    public List<OptionResponse> getOptionsByProductId(Long productId) {
        return optionRepository.findByProductId(productId).stream().map(OptionResponse::new)
                .collect(Collectors.toList());
    }

    public Option getOptionById(Long id) {
        return optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found with id " + id));
    }

    public Option saveOption(Option option) {
        return optionRepository.save(option);
    }

    public void deleteOption(Long id) {
        if (!optionRepository.existsById(id)) {
            throw new RuntimeException("Option not found with id " + id);
        }
        optionRepository.deleteById(id);
    }

    public Option updateOption(Long id, Option optionDetails) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Option not found with id " + id));
        if (!option.getProduct().getId().equals(optionDetails.getProduct().getId())) {
            throw new RuntimeException("Option does not belong to the specified product");
        }
        option.setName(optionDetails.getName());
        option.setQuantity(optionDetails.getQuantity());
        return optionRepository.save(option);
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, int quantity) {
        Option option = getOptionById(optionId);
        option.subtractQuantity(quantity);
        optionRepository.save(option);
    }

}
