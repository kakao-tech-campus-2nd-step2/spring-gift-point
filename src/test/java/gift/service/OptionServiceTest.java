package gift.service;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.option.OptionAddRequest;
import gift.dto.option.OptionUpdateRequest;
import gift.exception.BadRequestException;
import gift.exception.DuplicatedNameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


@SpringBootTest
@Transactional
class OptionServiceTest {

    private final Pageable pageable = PageRequest.of(0, 10);
    @Autowired
    private OptionService optionService;

    @Test
    @DisplayName("정상 옵션 추가하기")
    void successAddOption() {
        //given
        var optionRequest = new OptionAddRequest("옵션1", 1000, 1L);
        //when
        var savedOption = optionService.addOption(optionRequest);
        //then
        var options = optionService.getOptions(1L, pageable);
        Assertions.assertThat(options.size()).isEqualTo(1);

        optionService.deleteOption(savedOption.id());
    }

    @Test
    @DisplayName("둘 이상의 옵션 추가하기")
    void successAddOptions() {
        //given
        var optionRequest1 = new OptionAddRequest("옵션1", 1000, 1L);
        var optionRequest2 = new OptionAddRequest("옵션2", 1000, 1L);
        //when
        var savedOption1 = optionService.addOption(optionRequest1);
        var savedOption2 = optionService.addOption(optionRequest2);
        //then
        var options = optionService.getOptions(1L, pageable);
        Assertions.assertThat(options.size()).isEqualTo(2);

        optionService.deleteOption(savedOption1.id());
        optionService.deleteOption(savedOption2.id());
    }

    @Test
    @DisplayName("중복된 이름으로 된 상품 옵션 추가시 예외가 발생한다.")
    void failAddOptionWithDuplicatedName() {
        //given
        var optionRequest = new OptionAddRequest("옵션1", 1000, 1L);
        var savedOption = optionService.addOption(optionRequest);
        //when, then
        Assertions.assertThatThrownBy(() -> optionService.addOption(optionRequest)).isInstanceOf(DuplicatedNameException.class);

        optionService.deleteOption(savedOption.id());
    }

    @Test
    @DisplayName("옵션 수정하기")
    void successUpdateOption() {
        //given
        var optionRequest = new OptionAddRequest("옵션1", 1000, 1L);
        var savedOption = optionService.addOption(optionRequest);
        var optionUpdateDto = new OptionUpdateRequest("수정된 옵션", 12345);
        //when
        optionService.updateOption(savedOption.id(), optionUpdateDto);
        //then
        var options = optionService.getOptions(1L, pageable);
        var filteredOptions = options.stream().filter(productOptionResponse -> productOptionResponse.id().equals(savedOption.id())).toList();
        Assertions.assertThat(filteredOptions.size()).isEqualTo(1);
        Assertions.assertThat(filteredOptions.get(0).name()).isEqualTo("수정된 옵션");
        Assertions.assertThat(filteredOptions.get(0).quantity()).isEqualTo(12345);

        optionService.deleteOption(savedOption.id());
    }

    @Test
    @DisplayName("옵션의 잔여수량이 0인 경우에 차감요청이 들어오면 예외를 발생시킨다.")
    void failAddOptionWithZeroQuantity() {
        //given
        var optionRequest = new OptionAddRequest("옵션1", 0, 1L);
        var savedOption = optionService.addOption(optionRequest);
        var orderRequest = new GiftOrderRequest(savedOption.id(), 1, "hello");
        //when, then
        Assertions.assertThatThrownBy(() -> optionService.orderOption(savedOption.id(), orderRequest)).isInstanceOf(BadRequestException.class);

        optionService.deleteOption(savedOption.id());
    }

    @Test
    @DisplayName("동시성 테스트 - 500개의 쓰레드풀에 10000개의 요청을 보냈을 때에도 정상적으로 요청이 처리 된다.")
    public void concurrencyTest() throws InterruptedException {
        //given
        var orderRequest = new GiftOrderRequest(1L, 1, "hello");
        int requestCount = 10000;
        var executorService = Executors.newFixedThreadPool(500);
        var countDownLatch = new CountDownLatch(requestCount);
        //when
        for (int i = 0; i < requestCount; i++) {
            executorService.execute(() -> {
                try {
                    optionService.orderOption(1L, orderRequest);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }
        countDownLatch.await();
        //then
        var option = optionService.getOptions(3L, Pageable.unpaged()).stream()
                .filter((op) -> op.id().equals(1L))
                .findFirst()
                .get();

        Assertions.assertThat(option.quantity()).isEqualTo(0);
    }
}
