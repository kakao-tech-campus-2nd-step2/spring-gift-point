package gift.service;

import gift.dto.betweenClient.option.OptionRequestDTO;
import gift.dto.betweenClient.option.OptionResponseDTO;
import gift.entity.Option;
import gift.entity.Product;
import gift.exception.BadRequestExceptions.BadRequestException;
import gift.exception.BadRequestExceptions.InvalidIdException;
import gift.exception.BadRequestExceptions.NoSuchProductIdException;
import gift.exception.InternalServerExceptions.InternalServerException;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OptionService {
    private final OptionRepository optionRepository;
    private final ProductRepository productRepository;

    public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
        this.optionRepository = optionRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<OptionResponseDTO> getOneProductIdAllOptions(Long productId) {
        List<Option> optionList = optionRepository.findAllByProductId(productId);
        return optionList.stream().map(OptionResponseDTO::convertToDTO).toList();
    }

    @Transactional(readOnly = true)
    public Option getOption(Long optionId) {
        Option option = optionRepository.findById(optionId).orElseThrow(() -> new BadRequestException("해당 옵션 Id를 찾지 못했습니다."));
        return new Option(option.getProduct(), option.getName(), option.getQuantity());
    }

    @Transactional
    public void addOption(Long productId, OptionRequestDTO optionRequestDTO){
        try {
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new NoSuchProductIdException("해당 상품 Id인 상품을 찾지 못했습니다."));
            checkUniqueOptionName(product, optionRequestDTO.name());
            optionRepository.save(optionRequestDTO.convertToOption(product));
        } catch (BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException("해당 상품에 이미 존재하는 옵션 이름 입니다.");
        } catch (Exception e) {
            throw new InvalidIdException(e.getMessage());
        }
    }

    @Transactional
    public void updateOption(Long productId, Long optionId, OptionRequestDTO optionRequestDTO){
        try {
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new NoSuchProductIdException("해당 상품 Id인 상품을 찾지 못했습니다."));
            Option optionToReplace = optionRequestDTO.convertToOption(product);
            Option optionInDb = optionRepository.findById(optionId).orElseThrow(
                    () -> new BadRequestException("그러한 Id를 가지는 옵션을 찾을 수 없습니다."));
            checkUniqueOptionName(product, optionRequestDTO.name());
            optionInDb.changeOption(optionToReplace.getName(), optionToReplace.getQuantity());
        } catch (BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void subtractOptionQuantity(Long optionId, Integer quantity){
        try {
            Option optionInDb = optionRepository.findById(optionId).orElseThrow(
                    () -> new BadRequestException("그러한 Id를 가지는 옵션을 찾을 수 없습니다."));
            optionInDb.subtractQuantity(quantity);
        } catch (BadRequestException e) {
            throw e;
        } catch (DataIntegrityViolationException e) {
            throw new BadRequestException(e.getMessage());
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    @Transactional
    public void deleteOption(Long productId, Long optionId){
        try {
            Option optionToDelete = optionRepository.findByIdAndProductId(optionId, productId).orElseThrow(
                    () -> new BadRequestException("그러한 Id를 가지는 옵션을 찾을 수 없습니다."));
            if(optionRepository.countByProduct(optionToDelete.getProduct()) <= 1)
                throw new BadRequestException("상품에는 적어도 하나의 옵션이 있어야 해서 제거할 수 없습니다.");
            optionRepository.delete(optionToDelete);
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            throw new InternalServerException(e.getMessage());
        }
    }

    void checkUniqueOptionName(Product product, String optionName) throws DataIntegrityViolationException {
        if(optionRepository.existsByProductAndName(product, optionName))
            throw new DataIntegrityViolationException("이미 있는 옵션 이름입니다.");
    }

}
