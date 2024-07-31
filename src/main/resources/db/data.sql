INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('교환권', '#FF5733', 'https://example.com/images/exchange_voucher.jpg', '다양한 상품으로 교환 가능한 교환권');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('상품권', '#33FF57', 'https://example.com/images/gift_card.jpg', '쇼핑에 사용할 수 있는 상품권');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('뷰티', '#FF33A1', 'https://example.com/images/beauty.jpg', '화장품 및 뷰티 제품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('패션', '#3357FF', 'https://example.com/images/fashion.jpg', '최신 유행 패션 아이템');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('식품', '#FF8C33', 'https://example.com/images/food.jpg', '맛있는 음식과 식품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('리빙/도서', '#33D1FF', 'https://example.com/images/living_books.jpg', '생활용품과 도서');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('레저/스포츠', '#FF3333', 'https://example.com/images/leisure_sports.jpg', '레저 및 스포츠 용품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('아티스트/캐릭터', '#33FFDA', 'https://example.com/images/artist_character.jpg', '아티스트 및 캐릭터 상품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('유아동/반려', '#FFC733', 'https://example.com/images/kids_pets.jpg', '유아 및 반려동물 용품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('디지털/가전', '#6A33FF', 'https://example.com/images/digital_electronics.jpg', '디지털 및 가전 제품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('카카오프렌즈', '#FF9533', 'https://example.com/images/kakao_friends.jpg', '카카오프렌즈 관련 상품');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('트렌드 선물', '#33FF6A', 'https://example.com/images/trend_gift.jpg', '최신 트렌드 선물');
INSERT INTO CATEGORIES (NAME, COLOR, IMAGE_URL, DESCRIPTION) VALUES ('백화점', '#3336FF', 'https://example.com/images/department_store.jpg', '백화점에서 구매 가능한 상품');

INSERT INTO PRODUCTS (NAME, PRICE, IMAGE_URL, CATEGORY_ID) VALUES ('케이크', 15000, 'http://example.com/cake', (SELECT ID FROM CATEGORIES WHERE NAME = '식품'));
INSERT INTO PRODUCTS (NAME, PRICE, IMAGE_URL, CATEGORY_ID) VALUES ('커피', 1500, 'http://example.com/coffee', (SELECT ID FROM CATEGORIES WHERE NAME = '식품'));

INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('초코 케이크', 10, (SELECT ID FROM PRODUCTS WHERE NAME = '케이크'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('생크림 케이크', 8, (SELECT ID FROM PRODUCTS WHERE NAME = '케이크'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('치즈 케이크', 5, (SELECT ID FROM PRODUCTS WHERE NAME = '케이크'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('딸기 케이크', 7, (SELECT ID FROM PRODUCTS WHERE NAME = '케이크'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('레드벨벳 케이크', 3, (SELECT ID FROM PRODUCTS WHERE NAME = '케이크'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('아메리카노', 10, (SELECT ID FROM PRODUCTS WHERE NAME = '커피'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('에스프레소', 8, (SELECT ID FROM PRODUCTS WHERE NAME = '커피'));
INSERT INTO OPTIONS (NAME, QUANTITY, PRODUCT_ID) VALUES ('라떼', 5, (SELECT ID FROM PRODUCTS WHERE NAME = '커피'));