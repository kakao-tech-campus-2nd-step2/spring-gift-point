package gift.Service;

import gift.ConverterToDto;
import gift.DTO.Option;
import gift.DTO.OptionDto;
import gift.DTO.Product;
import gift.DTO.ProductDto;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import java.util.List;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OptionService {

  private final OptionRepository optionRepository;
  private final ProductRepository productRepository;

  public OptionService(OptionRepository optionRepository, ProductRepository productRepository) {
    this.optionRepository = optionRepository;
    this.productRepository = productRepository;
  }

  public OptionDto addOption(OptionDto optionDto) {
    ProductDto productDto = optionDto.getProductDto();
    Product product = productRepository.findById(productDto.getId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    Option option = new Option(optionDto.getName(), optionDto.getQuantity(), product);
    Option addedOption = optionRepository.save(option);

    return ConverterToDto.convertToOptionDto(addedOption);
  }

  public List<OptionDto> getAllOptions() {
    List<Option> options = optionRepository.findAll();
    List<OptionDto> optionDtos = options.stream()
      .map(ConverterToDto::convertToOptionDto)
      .toList();

    return optionDtos;
  }

  public List<OptionDto> getOptionsById(Long productId) {
    Product product = productRepository.findById(productId)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    List<Option> options = product.getOptions();
    List<OptionDto> optionDtos = options.stream().map(ConverterToDto::convertToOptionDto).toList();
    return optionDtos;
  }

  public void deleteOption(Long id) {
    Option option = optionRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));
    optionRepository.deleteById(id);
  }

  public OptionDto updateOption(Long id, OptionDto updateOptionDto) {
    Option existingOption = optionRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    Product product = productRepository.findById(updateOptionDto.getId())
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));

    Option updateOption = new Option(id, updateOptionDto.getName(), updateOptionDto.getQuantity(),
      product);
    Option updatedOption = optionRepository.save(updateOption);

    return ConverterToDto.convertToOptionDto(updatedOption);
  }

  public void optionQuantitySubtract(OptionDto optionDto, int amount) {
    Long id = optionDto.getId();
    Option option = optionRepository.findById(id)
      .orElseThrow(() -> new EmptyResultDataAccessException("해당 데이터가 없습니다", 1));
    option.subtract(amount);
    optionRepository.save(option);
  }
}
