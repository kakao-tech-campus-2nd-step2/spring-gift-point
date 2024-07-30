package gift.application.product;

import gift.application.product.dto.OptionCommand;
import gift.application.product.dto.OptionCommand.Purchase;
import gift.application.product.dto.OptionModel;
import gift.application.product.dto.OptionModel.Info;
import gift.application.product.dto.OptionModel.PurchaseInfo;
import gift.application.product.dto.ProductCommand;
import gift.application.product.dto.ProductModel;
import gift.application.product.service.CategoryService;
import gift.application.product.service.OptionService;
import gift.application.product.service.OrdersService;
import gift.application.product.service.ProductKakaoService;
import gift.application.product.service.ProductService;

import java.util.List;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ProductFacade {

    private final OptionService optionService;
    private final ProductService productService;
    private final ProductKakaoService productKakaoService;
    private final OrdersService ordersService;

    public ProductFacade(OptionService optionService,
        ProductService productService,
        ProductKakaoService productKakaoService,
        OrdersService ordersService) {
        this.optionService = optionService;
        this.productService = productService;
        this.productKakaoService = productKakaoService;
        this.ordersService = ordersService;
    }

    @Transactional
    public Pair<ProductModel.Info, List<Info>> createProduct(
        ProductCommand.Register productCommand,
        OptionCommand.RegisterMany optionCommand
    ) {
        ProductModel.Info productModel = productService.createProduct(productCommand);
        List<Info> optionModel = optionService.createOption(productModel.id(),
            optionCommand);
        return Pair.of(productModel, optionModel);
    }

    @Transactional
    public Pair<ProductModel.Info, List<Info>> updateProduct(
        Long productId,
        ProductCommand.Update productCommand
    ) {
        ProductModel.Info productModel = productService.updateProduct(productId, productCommand);
        List<Info> optionModel = optionService.getOptions(productId);
        return Pair.of(productModel, optionModel);
    }


    public PurchaseInfo purchase(Long memberId, Purchase command) {
        PurchaseInfo purchaseInfo = optionService.purchaseOption(command);
        ordersService.saveOrders(memberId, purchaseInfo);
        productKakaoService.sendPurchaseMessage(memberId, command.message());
        return purchaseInfo;
    }

}
