package gift.service;

import gift.domain.MenuDomain.Menu;
import gift.domain.OptionDomain.Option;
import gift.domain.OptionDomain.OptionRequest;
import gift.domain.OptionDomain.OptionResponse;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class OptionService {
    private MenuRepository menuRepository;
    private OptionRepository optionRepository;

    public OptionService(MenuRepository menuRepository,OptionRepository optionRepository){
        this.menuRepository = menuRepository;
        this.optionRepository = optionRepository;
    }

    public OptionResponse save(OptionRequest optionRequest,Long productId) {
        Menu menu = menuRepository.findById(productId).get();
        Option option = mapOptionRequestToOption(optionRequest,menu);
        return mapOptionToOptionResponse(optionRepository.save(option));
    }

    public OptionResponse update(OptionRequest optionRequest,Long productId) {
        return save(optionRequest,productId);
    }

    public void delete(Long optionId) {
        optionRepository.deleteById(optionId);
    }

    public List<OptionResponse> readAll(Long productId) {
        Menu menu = menuRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("메뉴 정보가 없습니다."));
        List<Option> options = menu.getOptions();
        return options.stream()
                .map(this::mapOptionToOptionResponse)
                .collect(Collectors.toList());
    }

    public Option mapOptionRequestToOption(OptionRequest optionRequest,Menu menu){
        return new Option(optionRequest.id(),optionRequest.name(), optionRequest.quantity(),menu);
    }

    public OptionResponse mapOptionToOptionResponse(Option option){
        return new OptionResponse(option.getId(),option.getName(),option.getQuantity());
    }

}
