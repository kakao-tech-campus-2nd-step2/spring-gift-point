package gift.wishList.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gift.product.domain.Product;
import gift.product.repository.ProductJpaRepository;
import gift.user.domain.User;
import gift.user.repository.UserJpaRepository;
import gift.dto.response.WishProductResponse;
import gift.wishList.domain.WishProduct;
import gift.wishList.repository.WishProductJpaRepository;

@Service
public class WishProductService {
	private final WishProductJpaRepository wishProductJpaRepository;
	private final ProductJpaRepository productJpaRepository;
	private final UserJpaRepository userJpaRepository;

	public WishProductService(WishProductJpaRepository wishProductJpaRepository,
		ProductJpaRepository productJpaRepository,
		UserJpaRepository userJpaRepository) {
		this.wishProductJpaRepository = wishProductJpaRepository;
		this.productJpaRepository = productJpaRepository;
		this.userJpaRepository = userJpaRepository;
	}

	@Transactional(readOnly = true)
	public List<WishProductResponse> getByUserId(Long userId) {
		return wishProductJpaRepository.findByUserId(userId).stream()
			.map(WishProductResponse::from)
			.toList();
	}

	@Transactional(readOnly = true)
	public Page<WishProductResponse> getByUserId(Long userId, Pageable pageable) {
		return wishProductJpaRepository.findByUserId(userId, pageable)
			.map(WishProductResponse::from);
	}

	@Transactional
	public Long save(Long userId, Long productId) {
		User user = userJpaRepository.findById(userId).orElseThrow();
		Product product = productJpaRepository.findById(productId).orElseThrow();
		WishProduct wishProduct = WishProduct.of(user, product);
		wishProductJpaRepository.save(wishProduct);
		return wishProduct.getId();
	}

	@Transactional
	public void deleteWishProduct(Long wishId) {
		wishProductJpaRepository.deleteById(wishId);
	}
}
