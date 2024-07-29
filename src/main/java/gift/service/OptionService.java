package gift.service;

import gift.domain.Menu;
import gift.domain.Option;
import gift.domain.OptionRequest;
import gift.domain.OptionResponse;
import gift.repository.MenuRepository;
import gift.repository.OptionRepository;
import org.springframework.stereotype.Service;

@Service
public class OptionService {
    private MenuRepository menuRepository;
    private OptionRepository optionRepository;

    public OptionService(MenuRepository menuRepository,OptionRepository optionRepository){
        this.menuRepository = menuRepository;
        this.optionRepository = optionRepository;
    }
    public OptionResponse save(OptionRequest optionRequest) {
        Menu menu = menuRepository.findById(optionRequest.menuId()).get();
        Option option = mapOptionRequestToOption(optionRequest,menu);
        return mapOptionToOptionResponse(optionRepository.save(option));
    }

    public Option mapOptionRequestToOption(OptionRequest optionRequest,Menu menu){
        return new Option(optionRequest.id(),optionRequest.name(), optionRequest.quantity(),menu);
    }

    public OptionResponse mapOptionToOptionResponse(Option option){
        return new OptionResponse(option.getId(),option.getName(),option.getQuantity(),option.getMenu());
    }
}
