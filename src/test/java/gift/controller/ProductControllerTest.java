package gift.controller;

import gift.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;
import org.springframework.web.reactive.function.BodyInserters;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;
    private String baseUrl;
    @Autowired
    private WebTestClient webClient;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        webClient = WebTestClient.bindToServer().baseUrl(baseUrl).build();
    }

    //사실상 get test는 필요없다고 생각되어짐.
    //post나 put 등의 작업에서 잘못된 데이터에 대해서 해당 응답을 돌려주는 지를 검증해야함.
    @Test
    @DisplayName("정상 get 응답 확인")
    void getProduct() {

        webClient.get().uri("/api/products").accept(MediaType.APPLICATION_JSON).exchange()
            .expectStatus().isOk();

    }


    private static ProductDTO getProductDTO(String name, Integer price, String imageUrl) {
        return new ProductDTO(null, name, price, imageUrl,1L);
    }

    @Test
    @DisplayName("name이 15글자가 넘는 경우 실패")
    void addMore15word() {
        //given
        String name = "123123123123123123123";
        int price = 123;
        String imageUrl = "abcd";
        ProductDTO dto = getProductDTO(name, price, imageUrl);

        //when then
        createPostAndCheckBadRequest(dto, "잘못된 이름입니다.");

    }

    @Test
    @DisplayName("name이 0글자인 경우 실패")
    void addZero() {
        //given
        ProductDTO dto = getProductDTO("", 123, "test");

        //when
        createPostAndCheckBadRequest(dto, "잘못된 이름입니다.");

    }

    @Test
    @DisplayName("price가 0인 경우 실패")
    void priceZero() {

        ProductDTO dto = getProductDTO("asd", null, "test");

        createPostAndCheckBadRequest(dto, "가격을 입력해주세요");

    }

    @Test
    @DisplayName("imgUrl 이 없는 경우 실패")
    void imgUrlNotInput() {
        ProductDTO dto = getProductDTO("asd", 123, null);

        createPostAndCheckBadRequest(dto, "이미지 주소를 입력해주세요");
    }

    @Test
    @DisplayName("사용할 수 없는 특수문자")
    void nameCantUseWords() {
        ProductDTO dto = getProductDTO("asd!", 123, "test");
        ProductDTO dto2 = getProductDTO("asd?", 123, "test2");


        dto.setCategoryId(1L);
        dto2.setCategoryId(2L);


        createPostAndCheckBadRequest(dto, "잘못된 이름입니다.");
        createPostAndCheckBadRequest(dto2, "잘못된 이름입니다.");

    }

    @Test
    @DisplayName("카카오 사용하기")
    void useKakao() {
        ProductDTO dto = getProductDTO("나카카오콩따러간다", 123, "test");

        createPostAndCheckBadRequest(dto, "카카오 문구는 md협의 이후 사용할 수 있습니다.");
    }

    //private function//


    private ResponseSpec createPostReqeust(ProductDTO dto) {
        return webClient.post().uri("/api/products").accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(dto)).exchange();

        //request body 에는 BodyInserters.formValue로 객체 -> body 데이터로 변환
    }

    private void createPostAndCheckBadRequest(ProductDTO dto, String compareMsg) {
        createPostReqeust(dto).expectStatus().isBadRequest().expectBody().jsonPath("$.message")
            .isEqualTo(compareMsg);
    }

}