package gift.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.Util.JWTUtil;
import gift.dto.option.*;
import gift.entity.Option;
import gift.entity.Order;
import gift.entity.Product;
import gift.entity.User;
import gift.exception.exception.BadRequestException;
import gift.exception.exception.NotFoundException;
import gift.exception.exception.ServerInternalException;
import gift.exception.exception.UnAuthException;
import gift.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OptionService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;


    public OptionResponseDTO saveOption(int productId, SaveOptionDTO saveOptionDTO) {
        Product product = findProductById(productId);
        checkDuplicateOption(productId, saveOptionDTO);
        Option option = new Option(product, saveOptionDTO.name());
        return optionRepository.save(option).toResponseDTO();
    }

    public OptionResponseDTO deleteOption(int productId, int optionId) {
        Product product = findProductById(productId);
        Option option = findOptionById(optionId);
        checkContains(product, option);

        option.getProduct().deleteOption(option);
        optionRepository.deleteById(optionId);
        return option.toResponseDTO();
    }

    @Transactional
    public OptionResponseDTO updateOption(int productId, int optionId, SaveOptionDTO saveOptionDTO) {
        Option option = findOptionById(optionId);
        Product product = findProductById(productId);
        checkContains(product, option);
        product.getOptions().remove(option);

        checkDuplicateOption(productId, saveOptionDTO);
        option = option.changeOption(saveOptionDTO);
        product.addOptions(option);

        return option.toResponseDTO();
    }

    public List<OptionResponseDTO> getOptions(int productId) {
        findProductById(productId);
        return optionRepository.findByProductId(productId);
    }

    @Transactional
    public OptionResponseDTO refillQuantity(int productId, int optionId, OptionQuantityDTO optionQuantityDTO) {
        Product product = findProductById(productId);
        Option option = findOptionById(optionId);
        checkContains(product, option);

        option = option.addQuantity(optionQuantityDTO.quantity());
        return option.toResponseDTO();
    }

    private Product findProductById(int productId) {
        return productRepository.findById(productId).orElseThrow(() -> new NotFoundException("해당 물품이 없음"));
    }

    private void checkDuplicateOption(int productId, SaveOptionDTO saveOptionDTO) {
        optionRepository.findByOption(productId, saveOptionDTO.name()).ifPresent(c -> {
            throw new BadRequestException("이미 존재하는 옵션");
        });
    }

    private static void checkContains(Product product, Option option) {
        if (!product.getOptions().contains(option)) throw new NotFoundException("제품id에 해당하는 옵션id가 없음");
    }

    private Option findOptionById(int optionId) {
        return optionRepository.findById(optionId).orElseThrow(() -> new NotFoundException("해당 옵션이 없음"));
    }

}
