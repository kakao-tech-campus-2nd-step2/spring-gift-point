package gift.bill.service;

import gift.bill.domain.Bill;
import gift.bill.persistence.BillRepository;
import gift.bill.service.command.BillCommand;
import gift.bill.service.dto.BillInfo;
import gift.member.domain.Member;
import gift.member.exception.MemberNotFoundException;
import gift.member.persistence.MemberRepository;
import gift.product.domain.ProductOption;
import gift.product.persistence.ProductOptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillService {
    private final BillRepository billRepository;
    private final MemberRepository memberRepository;
    private final ProductOptionRepository productOptionRepository;

    public BillService(BillRepository billRepository, MemberRepository memberRepository,
                       ProductOptionRepository productOptionRepository) {
        this.billRepository = billRepository;
        this.memberRepository = memberRepository;
        this.productOptionRepository = productOptionRepository;
    }

    @Transactional
    public BillInfo purchaseProduct(BillCommand billCommand) {
        var member = memberRepository.findById(billCommand.memberId())
                .orElseThrow(MemberNotFoundException::new);
        var productOption = getExistsProductOptionForUpdate(billCommand.optionId());

        executePayment(member, billCommand.usePoint(), billCommand.pointValue());
        productOption.buy(billCommand.quantity());

        var order = createBill(billCommand, productOption, member);
        billRepository.save(order);
        
        return BillInfo.from(order);
    }

    private static Bill createBill(BillCommand billCommand, ProductOption productOption, Member member) {
        var order = Bill.of(billCommand.memberId(), productOption.getProduct().getId(),
                billCommand.optionId(), productOption.getProduct().getPrice(), billCommand.quantity(),
                billCommand.usePoint(), billCommand.pointValue());
        var accumulatedPoint = member.accumulatePoint(order.getBilledPrice());
        order.setAccumulatedPoint(accumulatedPoint);

        return order;
    }


    private ProductOption getExistsProductOptionForUpdate(Long optionId) {

        return productOptionRepository.findByIdWithProduct(optionId)
                .orElseThrow(() -> new IllegalArgumentException("Product option not found"));
    }

    private void executePayment(Member member, Boolean usePoint, Integer pointValue) {
        // 결제 처리

        // 포인트 사용
        if (usePoint) {
            member.usePoint(pointValue);
        }
    }
}
