package gift.controller;

//@WebMvcTest(CategoryController.class)
class CategoryControllerTest {
    /*
    @Autowired
    CategoryController categoryController;

    @InjectMocks
    CategoryService categoryService;

    @Test
    @DisplayName("카테고리 정상 조회 확인")
    void getAllCategories(){
        //given

        BDDMockito.given(categoryService.readAll()).willReturn(
            List.of(getCategoryResponse(0), getCategoryResponse(1), getCategoryResponse(2)));

        //when
        List<CategoryResponse> actual = categoryController.getCategories().getBody();

        //then
        assertThat(actual).hasSize(3);
        assertThat(actual.get(0)).isEqualTo(getCategoryResponse(0));
        assertThat(actual.get(1)).isEqualTo(getCategoryResponse(1));
        assertThat(actual.get(2)).isEqualTo(getCategoryResponse(2));

    }

    private static CategoryResponse getCategoryResponse(long id) {
        return new CategoryResponse(id, "happy"+id, "#123"+id, "hah.url"+id, "");
    }

     */


}