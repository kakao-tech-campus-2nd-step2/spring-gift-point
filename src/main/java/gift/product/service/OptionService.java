package gift.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.core.exception.product.DuplicateOptionNameException;
import gift.core.exception.product.ProductNotFoundException;
import gift.dto.request.OptionRequest;
import gift.dto.request.OrderRequest;
import gift.dto.response.OptionResponse;
import gift.product.domain.Option;
import gift.product.domain.Product;
import gift.product.repository.OptionJpaRepository;
import gift.product.repository.ProductJpaRepository;

@Service
public class OptionService {
	private final OptionJpaRepository optionRepository;
	private final ProductJpaRepository productRepository;

	public OptionService(OptionJpaRepository optionRepository, ProductJpaRepository productRepository) {
		this.optionRepository = optionRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public Long addOptions(Long id, List<OptionRequest> optionRequests) {

		Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
		validateOptions(id, optionRequests);

		List<Option> options = optionRequests.stream()
			.map(optionRequest -> Option.create(optionRequest.name(), optionRequest.quantity(), product))
			.toList();

		optionRepository.saveAll(options);
		return product.getId();
	}

	@Transactional
	public List<OptionResponse> getOptions(Long productId) {
		List<Option> options = optionRepository.findAllByProductId(productId);
		return options.stream()
			.map(OptionResponse::from)
			.toList();
	}

	@Transactional
	public Long updateOptions(Long id, OptionRequest optionRequest) {
		Option option = optionRepository.findById(id)
			.orElseThrow(() -> new ProductNotFoundException(id));

		option.setName(optionRequest.name());
		option.setQuantity(optionRequest.quantity());

		return optionRepository.save(option).getId();
	}

	@Transactional
	public Long deleteOption(Long id) {
		optionRepository.deleteById(id);
		return id;
	}

	@Transactional
	public Long reduceStock(OrderRequest orderRequest) {
		Option option = optionRepository.findById(orderRequest.optionId())
			.orElseThrow(() -> new ProductNotFoundException(orderRequest.optionId()));

		option.removeStock(orderRequest.quantity());
		return option.getId();
	}

	private void validateOptions(Long id, List<OptionRequest> optionRequests) {
		List<Option> options = optionRepository.findAllByProductId(id);

		// 이미 존재하는 옵션이라면 에러 반환
		optionRequests.stream()
			.forEach(optionRequest -> {
				if (options.stream().anyMatch(option -> option.getName().equals(optionRequest.name()))) {
					throw new DuplicateOptionNameException(optionRequest.name());
				}
			});
	}
}
