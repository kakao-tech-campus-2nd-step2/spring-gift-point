package gift.intergrationTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.exception.ErrorCode;
import gift.interceptor.AuthInterceptor;
import gift.model.categories.Category;
import gift.model.item.ItemForm;
import gift.model.option.OptionDTO;
import gift.repository.CategoryRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ItemWithOptionTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @MockBean
    private AuthInterceptor authInterceptor;

    private static final String TEST_CATEGORY_NAME = "가구";
    private static final String TEST_ITEM_NAME = "의자";
    private static final String TEST_URL = "img";
    private static final Long TEST_PRICE = 1000L;
    private static final String WRONG_OPTION_NAME = "%$#%$잘못된 옵션이름";

    private Category testCategory;
    private ItemForm testItemForm;

    @BeforeEach
    void setUp() throws Exception {
        given(authInterceptor.preHandle(any(), any(), any())).willReturn(true);
        testCategory = categoryRepository.save(new Category(TEST_CATEGORY_NAME, TEST_URL));
        testItemForm = createTestItemForm();
    }

    private ItemForm createTestItemForm() {
        List<OptionDTO> options = new ArrayList<>(List.of(
            new OptionDTO("흰색", 100L),
            new OptionDTO("검정색", 50L),
            new OptionDTO("핑크색", 200L)
        ));
        return new ItemForm(TEST_ITEM_NAME, TEST_PRICE, TEST_URL, testCategory.getId(), options);
    }

    private ResultActions performPostRequest(String url, Object content) throws Exception {
        return mockMvc.perform(post(url)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(content)));
    }

    @Test
    @DisplayName("상품과 옵션 동시 추가 실패 (잘못된 옵션명)")
    void testInsertItemWithWrongNameOptions() throws Exception {
        testItemForm.getOptions().add(new OptionDTO(WRONG_OPTION_NAME, 100L));

        performPostRequest("/product", testItemForm)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()));
    }

    @Test
    @DisplayName("상품과 옵션 동시 추가 실패 (중복 옵션)")
    void testInsertItemWithDuplicateNameOptions() throws Exception {
        testItemForm.getOptions().add(new OptionDTO("흰색", 100L));

        performPostRequest("/product", testItemForm)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_NAME.getMessage()));
    }

    @Test
    @DisplayName("상품과 옵션 동시 추가 성공")
    void testInsertItemWithOptionsSuccess() throws Exception {
        performPostRequest("/product", testItemForm)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @DisplayName("옵션 단독 추가 실패 (잘못된 이름)")
    void testInsertWrongOption() throws Exception {
        Long itemId = createTestItem();
        OptionDTO wrongOption = new OptionDTO(WRONG_OPTION_NAME, 100L);

        performPostRequest("/option/" + itemId, wrongOption)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.INVALID_INPUT.getMessage()));
    }

    @Test
    @DisplayName("옵션 단독 추가 실패 (중복된 이름)")
    void testInsertDuplicateOption() throws Exception {
        Long itemId = createTestItem();
        OptionDTO duplicateOption = new OptionDTO("흰색", 100L);

        performPostRequest("/option/" + itemId, duplicateOption)
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(ErrorCode.DUPLICATE_NAME.getMessage()));
    }

    @Test
    @DisplayName("옵션 단독 추가 성공")
    void testInsertOptionSuccess() throws Exception {
        Long itemId = createTestItem();
        OptionDTO newOption = new OptionDTO("회색", 150L);

        performPostRequest("/option/" + itemId, newOption)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNumber());
    }

    @Test
    @DisplayName("옵션 수정 성공")
    void testUpdateOptionSuccess() throws Exception {
        Long itemId = createTestItem();
        List<OptionDTO> options = getOptionsInItem(itemId);
        OptionDTO optionToUpdate = options.get(0);
        OptionDTO updatedOption = new OptionDTO(optionToUpdate.getId(), "수정된 옵션", 200L);

        mockMvc.perform(put("/option/" + itemId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedOption)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isNumber());

        mockMvc.perform(get("/option/" + itemId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$[0].name").value("수정된 옵션"))
            .andExpect(jsonPath("$[0].quantity").value(200));
    }

    @Test
    @DisplayName("옵션 삭제 성공")
    void testDeleteOptionsSuccess() throws Exception {
        Long itemId = createTestItem();
        List<OptionDTO> options = getOptionsInItem(itemId);
        Long optionIdToDelete = options.get(0).getId();

        mockMvc.perform(delete("/option/" + itemId + "/" + optionIdToDelete))
            .andExpect(status().isOk())
            .andExpect(content().string(optionIdToDelete.toString()));

        mockMvc.perform(get("/option/" + itemId)).andDo(print());
    }

    @Test
    @DisplayName("옵션 목록 조회 성공")
    void testFindOptionsSuccess() throws Exception {
        Long itemId = createTestItem();

        mockMvc.perform(get("/option/" + itemId))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").exists())
            .andExpect(jsonPath("$[0].quantity").exists());
    }

    @Test
    @DisplayName("상품 삭제 시 옵션도 함께 삭제")
    void testDeleteItemWithOptionsSuccess() throws Exception {
        Long itemId = createTestItem();

        mockMvc.perform(delete("/product/" + itemId))
            .andExpect(status().isOk())
            .andExpect(content().string(itemId.toString()));
    }

    private Long createTestItem() throws Exception {
        ResultActions result = performPostRequest("/product", testItemForm);
        return Long.parseLong(result.andReturn().getResponse().getContentAsString());
    }

    private List<OptionDTO> getOptionsInItem(Long itemId) throws Exception {
        String content = mockMvc.perform(get("/option/" + itemId))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(content, new TypeReference<>() {
        });
    }
}