package gift.service;

import gift.dto.giftorder.GiftOrderRequest;
import gift.dto.option.OptionRequest;
import gift.exception.BadRequestException;
import gift.exception.DuplicatedNameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;


@SpringBootTest
@Transactional
class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Test
    @DisplayName("정상 옵션 추가하기")
    void successAddOption() {
        //given
        var optionRequest = new OptionRequest("옵션1", 1000);
        //when
        var savedOption = optionService.addOption(1L, optionRequest);
        //then
        var options = optionService.getOptions(1L);
        Assertions.assertThat(options.size()).isEqualTo(1);

        optionService.deleteOption(1L, savedOption.id());
    }

    @Test
    @DisplayName("둘 이상의 옵션 추가하기")
    void successAddOptions() {
        //given
        var optionRequest1 = new OptionRequest("옵션1", 1000);
        var optionRequest2 = new OptionRequest("옵션2", 1000);
        //when
        var savedOption1 = optionService.addOption(1L, optionRequest1);
        var savedOption2 = optionService.addOption(1L, optionRequest2);
        //then
        var options = optionService.getOptions(1L);
        Assertions.assertThat(options.size()).isEqualTo(2);

        optionService.deleteOption(1L, savedOption1.id());
        optionService.deleteOption(1L, savedOption2.id());
    }

    @Test
    @DisplayName("중복된 이름으로 된 상품 옵션 추가시 예외가 발생한다.")
    void failAddOptionWithDuplicatedName() {
        //given
        var optionRequest = new OptionRequest("옵션1", 1000);
        var savedOption = optionService.addOption(1L, optionRequest);
        //when, then
        Assertions.assertThatThrownBy(() -> optionService.addOption(1L, optionRequest)).isInstanceOf(DuplicatedNameException.class);

        optionService.deleteOption(1L, savedOption.id());
    }

    @Test
    @DisplayName("옵션 수정하기")
    void successUpdateOption() {
        //given
        var optionRequest = new OptionRequest("옵션1", 1000);
        var savedOption = optionService.addOption(1L, optionRequest);
        var optionUpdateDto = new OptionRequest("수정된 옵션", 12345);
        //when
        optionService.updateOption(1L, savedOption.id(), optionUpdateDto);
        //then
        var options = optionService.getOptions(1L);
        var filteredOptions = options.stream().filter(productOptionResponse -> productOptionResponse.id().equals(savedOption.id())).toList();
        Assertions.assertThat(filteredOptions.size()).isEqualTo(1);
        Assertions.assertThat(filteredOptions.get(0).name()).isEqualTo("수정된 옵션");
        Assertions.assertThat(filteredOptions.get(0).quantity()).isEqualTo(12345);

        optionService.deleteOption(1L, savedOption.id());
    }

    @Test
    @DisplayName("옵션의 잔여수량이 0인 경우에 차감요청이 들어오면 예외를 발생시킨다.")
    void failAddOptionWithZeroQuantity() {
        //given
        var optionRequest = new OptionRequest("옵션1", 0);
        var savedOption = optionService.addOption(1L, optionRequest);
        var orderRequest = new GiftOrderRequest(savedOption.id(), 1, "hello");
        //when, then
        Assertions.assertThatThrownBy(() -> optionService.orderOption(savedOption.id(), orderRequest)).isInstanceOf(BadRequestException.class);

        optionService.deleteOption(1L, savedOption.id());
    }

    @Test
    @DisplayName("동시성 테스트 - 5개의 쓰레드풀에 500개의 요청을 보냈을 때에도 정상적으로 요청이 처리 된다.")
    public void concurrencyTest() throws InterruptedException {
        //given
        var orderRequest = new GiftOrderRequest(1L, 1, "hello");
        int requestCount = 500;
        var executorService = Executors.newFixedThreadPool(5);
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
        var option = optionService.getOptions(3L).stream()
                .filter((op) -> op.id().equals(1L))
                .findFirst()
                .get();

        Assertions.assertThat(option.quantity()).isEqualTo(9500);
    }
}
