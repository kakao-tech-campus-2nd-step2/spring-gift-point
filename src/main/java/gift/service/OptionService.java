package gift.service;

import gift.dto.optionsDTOs.AddOptionDTO;
import gift.dto.optionsDTOs.AllOptionDto;
import gift.dto.optionsDTOs.GetOptionDTO;
import gift.model.entity.Option;
import gift.model.valueObject.OptionList;
import gift.model.entity.Product;
import gift.model.valueObject.OptionNameList;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void deleteOption(String option, Long productId){
        OptionList optionList = new OptionList(optionRepository.findAllByProduct_Id(productId));
        if(optionList.canDelete()){
            optionRepository.deleteByName(option);
        }
    }

    public void addOptions(String options, String quantitiy, Long productID){
        List<String> optionList = List.of(options.split(","));
        OptionNameList optionListWrapper = new OptionNameList(optionList);
        if(optionListWrapper.hasDuplicates()){
            throw new IllegalArgumentException("옵션은 중복될 수 없습니다.");
        }
        List<String> optionQuantities = List.of(quantitiy.split(","));

        Product product = productRepository.findById(productID)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다."));

        List<Option> optionsToSave = new ArrayList<>();
        for(int i=0; i<optionList.size(); i++){
            Option addOption = new Option(
                    optionList.get(i),
                    Long.parseLong(optionQuantities.get(i)),
                    product
            );
            optionsToSave.add(addOption);
        }
        optionRepository.saveAll(optionsToSave);
    }

    @Transactional
    public void addOption(AddOptionDTO addOptionDTO, Long productId){
        OptionList optionList = new OptionList(optionRepository.findAll());
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("해당 상품이 없습니다."));
        Option addOption = new Option(
                addOptionDTO.getOptionName(),
                addOptionDTO.getQuantity(),
                product
        );
        if(!optionList.isContains(addOption)){
            optionRepository.save(addOption);
        }
    }

    public void updateOption(String oldName, String newName, long productID){
        Option updateOption = optionRepository.findByName(oldName)
                .orElseThrow(() -> new NoSuchElementException("해당 옵션이 없습니다."));
        OptionList optionList = new OptionList(optionRepository.findAll());
        Option newOption = updateOption.update(newName);
        if(!optionList.isContains(newOption)){
            optionRepository.save(newOption);
        }
    }

    @Transactional
    public void removeOption(Option option, int num){
        Option updateOption = option.quantityUpdate(-num);
        optionRepository.save(updateOption);
    }

    public GetOptionDTO getOptionsForHtml(Long productId){
        List<Option> optionList = optionRepository.findAllByProduct_Id(productId);
        List<String> optionNameList = optionList.stream()
                .map(Option::getName)
                .toList();
        GetOptionDTO getOptionDTO = new GetOptionDTO(
                productId,
                optionNameList
        );
        return getOptionDTO;
    }

    public List<AllOptionDto> getAllOptions(Long productId){
        List<Option> optionListT = optionRepository.findAllByProduct_Id(productId);
        List<AllOptionDto> optionList = optionListT.stream()
                .map(AllOptionDto::getAllOptionDto)
                .toList();
        return optionList;
    }

    public Option getOption(Long id){
        return optionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 옵션이 없습니다."));
    }
}