-- categories
INSERT INTO categories (name, color, image_url, description) VALUES ('교환권', 'RED', 'http://example.com/coupon.jpg', '교환권 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('상품권', 'INDIGO', 'http://example.com/voucher.jpg', '상품권 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('뷰티', 'YELLOW', 'http://example.com/beauty.jpg', '뷰티 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('패션', 'GREEN', 'http://example.com/fashion.jpg', '패션 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('식품', 'BLUE', 'http://example.com/food.jpg', '식품 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('와인/양주/전통주', 'NAVY', 'http://example.com/alchol.jpg', '와인/양주/전통주 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('리빙/도서', 'PURPLE', 'http://example.com/book.jpg', '리빙/도서 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('레저/스포츠', 'BROWN', 'http://example.com/sports.jpg', '레저/스포츠 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('아티스트/캐릭터', 'PINK', 'http://example.com/artist.jpg', '아티스트/캐릭터 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('유아동/반려', 'WHITE', 'http://example.com/pet.jpg', '유아동/반려 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('디지털/가전', 'BLACK', 'http://example.com/digital.jpg', '디지털/가전 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('카카오프렌즈', 'ORANGE', 'http://example.com/friends.jpg', '카카오프렌즈 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('트렌드 선물', 'CYAN', 'http://example.com/trend.jpg', '트렌드 선물 관련 카테고리');
INSERT INTO categories (name, color, image_url, description) VALUES ('백화점', 'GRAY', 'http://example.com/department.jpg', '백화점 관련 카테고리');


-- products
INSERT INTO products (name, price, image_url, category_id) VALUES
('스타벅스 아메리카노 교환권', 4500, 'http://example.com/starbucks.jpg', 1),
('파리바게트 케이크 교환권', 30000, 'http://example.com/parisbaguette.jpg', 1),
('CU 편의점 상품권 5,000원권', 5000, 'http://example.com/cu.jpg', 1),
('GS25 편의점 상품권 10,000원권', 10000, 'http://example.com/gs25.jpg', 1),
('베스킨라빈스 싱글레귤러 교환권', 3200, 'http://example.com/baskin.jpg', 1),
('던킨도너츠 먼치킨 교환권', 6000, 'http://example.com/dunkin.jpg', 1),
('CGV 영화관람권', 12000, 'http://example.com/cgv.jpg', 1),
('롯데시네마 영화관람권', 11000, 'http://example.com/lottecinema.jpg', 1),
('메가박스 영화관람권', 11000, 'http://example.com/megabox.jpg', 1),
('이디야 커피 아메리카노 교환권', 3000, 'http://example.com/ediya.jpg', 1),
('교촌치킨 허니콤보 교환권', 20000, 'http://example.com/kyochon.jpg', 1),
('피자헛 레귤러피자 교환권', 25000, 'http://example.com/pizzahut.jpg', 1),
('미스터피자 오리지널피자 교환권', 23000, 'http://example.com/mrpizza.jpg', 1),
('굽네치킨 고추바사삭 교환권', 19000, 'http://example.com/goobne.jpg', 1),
('BHC치킨 후라이드 교환권', 18000, 'http://example.com/bhc.jpg', 1),
('도미노피자 프리미엄피자 교환권', 28000, 'http://example.com/domino.jpg', 1),
('요기요 5,000원 할인권', 5000, 'http://example.com/yogiyo.jpg', 1),
('배달의민족 5,000원 할인권', 5000, 'http://example.com/baemin.jpg', 1),
('11번가 10,000원 상품권', 10000, 'http://example.com/11st.jpg', 1),
('G마켓 10,000원 상품권', 10000, 'http://example.com/gmarket.jpg', 1),
('옥션 10,000원 상품권', 10000, 'http://example.com/auction.jpg', 1),
('쿠팡 10,000원 상품권', 10000, 'http://example.com/coupang.jpg', 1),
('인터파크 10,000원 상품권', 10000, 'http://example.com/interpark.jpg', 1),
('위메프 10,000원 상품권', 10000, 'http://example.com/wemakeprice.jpg', 1),
('티몬 10,000원 상품권', 10000, 'http://example.com/tmon.jpg', 1),
('CJ몰 10,000원 상품권', 10000, 'http://example.com/cjmall.jpg', 1),
('신세계몰 10,000원 상품권', 10000, 'http://example.com/shinsegaemall.jpg', 1),
('롯데몰 10,000원 상품권', 10000, 'http://example.com/lottemall.jpg', 1),
('현대몰 10,000원 상품권', 10000, 'http://example.com/hyundaimall.jpg', 1),
('홈플러스 상품권 10,000원권', 10000, 'http://example.com/homeplus.jpg', 1),
('이마트 상품권 10,000원권', 10000, 'http://example.com/emart.jpg', 1),
('코스트코 상품권 10,000원권', 10000, 'http://example.com/costco.jpg', 1),
('스타벅스 기프티콘 10,000원권', 10000, 'http://example.com/starbucksgift.jpg', 1),
('파리바게트 기프티콘 10,000원권', 10000, 'http://example.com/parisbaguettegift.jpg', 1),
('배스킨라빈스 기프티콘 10,000원권', 10000, 'http://example.com/baskingift.jpg', 1),
('던킨도너츠 기프티콘 10,000원권', 10000, 'http://example.com/dunkingift.jpg', 1),
('이디야 커피 기프티콘 10,000원권', 10000, 'http://example.com/ediyagift.jpg', 1),
('교촌치킨 기프티콘 10,000원권', 10000, 'http://example.com/kyochongift.jpg', 1),
('피자헛 기프티콘 10,000원권', 10000, 'http://example.com/pizzahutgift.jpg', 1),
('미스터피자 기프티콘 10,000원권', 10000, 'http://example.com/mrpizzagift.jpg', 1),
('굽네치킨 기프티콘 10,000원권', 10000, 'http://example.com/goobnegift.jpg', 1),
('BHC치킨 기프티콘 10,000원권', 10000, 'http://example.com/bhcgift.jpg', 1),
('도미노피자 기프티콘 10,000원권', 10000, 'http://example.com/dominogift.jpg', 1);

INSERT INTO products (name, price, image_url, category_id) VALUES
('문화상품권 5,000원권', 5000, 'http://example.com/culture.jpg', 2),
('도서상품권 10,000원권', 10000, 'http://example.com/book.jpg', 2),
('백화점상품권 50,000원권', 50000, 'http://example.com/department.jpg', 2),
('영화관람권 2인', 20000, 'http://example.com/movie.jpg', 2),
('외식상품권 30,000원권', 30000, 'http://example.com/restaurant.jpg', 2),
('커피전문점 상품권 10,000원권', 10000, 'http://example.com/coffee.jpg', 2),
('편의점 상품권 10,000원권', 10000, 'http://example.com/convenience.jpg', 2),
('마트 상품권 20,000원권', 20000, 'http://example.com/mart.jpg', 2),
('주유상품권 30,000원권', 30000, 'http://example.com/fuel.jpg', 2),
('온라인쇼핑몰 상품권 10,000원권', 10000, 'http://example.com/onlineshop.jpg', 2),
('스타벅스 상품권 10,000원권', 10000, 'http://example.com/starbucks.jpg', 2),
('영화상품권 10,000원권', 10000, 'http://example.com/movie2.jpg', 2),
('패스트푸드 상품권 10,000원권', 10000, 'http://example.com/fastfood.jpg', 2),
('베이커리 상품권 10,000원권', 10000, 'http://example.com/bakery.jpg', 2),
('치킨 상품권 20,000원권', 20000, 'http://example.com/chicken.jpg', 2),
('피자 상품권 20,000원권', 20000, 'http://example.com/pizza.jpg', 2),
('아이스크림 상품권 10,000원권', 10000, 'http://example.com/icecream.jpg', 2),
('커피 상품권 5,000원권', 5000, 'http://example.com/coffee2.jpg', 2),
('헬스장 이용권 1개월', 100000, 'http://example.com/gym.jpg', 2),
('수영장 이용권 1개월', 80000, 'http://example.com/swimming.jpg', 2),
('독서실 이용권 1개월', 70000, 'http://example.com/library.jpg', 2),
('피트니스 클럽 이용권 1개월', 120000, 'http://example.com/fitness.jpg', 2),
('요가 클래스 이용권 1개월', 90000, 'http://example.com/yoga.jpg', 2),
('골프장 이용권 1회', 150000, 'http://example.com/golf.jpg', 2),
('테니스장 이용권 1회', 50000, 'http://example.com/tennis.jpg', 2),
('스키장 이용권 1일', 80000, 'http://example.com/ski.jpg', 2),
('볼링장 이용권 1회', 20000, 'http://example.com/bowling.jpg', 2),
('노래방 이용권 1시간', 30000, 'http://example.com/karaoke.jpg', 2),
('PC방 이용권 10시간', 20000, 'http://example.com/pcbang.jpg', 2),
('인터넷 카페 이용권 1개월', 30000, 'http://example.com/internetcafe.jpg', 2),
('미용실 이용권 1회', 50000, 'http://example.com/hairsalon.jpg', 2),
('네일샵 이용권 1회', 40000, 'http://example.com/nailshop.jpg', 2),
('마사지샵 이용권 1회', 60000, 'http://example.com/massageshop.jpg', 2),
('영어학원 수강권 1개월', 150000, 'http://example.com/englishacademy.jpg', 2),
('요리학원 수강권 1개월', 130000, 'http://example.com/cookingacademy.jpg', 2),
('피아노학원 수강권 1개월', 120000, 'http://example.com/pianoacademy.jpg', 2),
('드럼학원 수강권 1개월', 110000, 'http://example.com/drumacademy.jpg', 2),
('기타학원 수강권 1개월', 100000, 'http://example.com/guitaracademy.jpg', 2),
('바이올린학원 수강권 1개월', 140000, 'http://example.com/violinacademy.jpg', 2),
('플룻학원 수강권 1개월', 130000, 'http://example.com/fluteacademy.jpg', 2),
('미술학원 수강권 1개월', 120000, 'http://example.com/artacademy.jpg', 2),
('댄스학원 수강권 1개월', 110000, 'http://example.com/danceacademy.jpg', 2),
('연기학원 수강권 1개월', 150000, 'http://example.com/actingacademy.jpg', 2);



-- options
INSERT INTO options (name, quantity, product_id) VALUES ('추가 샷', 1, 1);
INSERT INTO options (name, quantity, product_id) VALUES ('초코 케이크', 1, 2);
INSERT INTO options (name, quantity, product_id) VALUES ('초콜릿', 1, 3);
INSERT INTO options (name, quantity, product_id) VALUES ('스낵 교환', 1, 4);
INSERT INTO options (name, quantity, product_id) VALUES ('초코콘', 1, 5);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 도넛', 1, 6);
INSERT INTO options (name, quantity, product_id) VALUES ('3D 안경', 1, 7);
INSERT INTO options (name, quantity, product_id) VALUES ('팝콘 세트', 1, 8);
INSERT INTO options (name, quantity, product_id) VALUES ('음료 세트', 1, 9);
INSERT INTO options (name, quantity, product_id) VALUES ('바닐라 시럽', 1, 10);
INSERT INTO options (name, quantity, product_id) VALUES ('치즈볼', 1, 11);
INSERT INTO options (name, quantity, product_id) VALUES ('음료 추가', 1, 12);
INSERT INTO options (name, quantity, product_id) VALUES ('핫소스 추가', 1, 13);
INSERT INTO options (name, quantity, product_id) VALUES ('콜라', 1, 14);
INSERT INTO options (name, quantity, product_id) VALUES ('갈릭 디핑 소스', 1, 15);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 토핑', 1, 16);
INSERT INTO options (name, quantity, product_id) VALUES ('배달료 할인', 1, 17);
INSERT INTO options (name, quantity, product_id) VALUES ('쿠폰 추가', 1, 18);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 적립금', 1, 19);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 할인', 1, 20);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 적립', 1, 21);
INSERT INTO options (name, quantity, product_id) VALUES ('할인 코드', 1, 22);
INSERT INTO options (name, quantity, product_id) VALUES ('무료 배송', 1, 23);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 포인트', 1, 24);
INSERT INTO options (name, quantity, product_id) VALUES ('첫 구매 할인', 1, 25);
INSERT INTO options (name, quantity, product_id) VALUES ('쿠폰 코드', 1, 26);
INSERT INTO options (name, quantity, product_id) VALUES ('보너스 포인트', 1, 27);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 포인트', 1, 28);
INSERT INTO options (name, quantity, product_id) VALUES ('회원 전용 할인', 1, 29);
INSERT INTO options (name, quantity, product_id) VALUES ('특별 할인', 1, 30);
INSERT INTO options (name, quantity, product_id) VALUES ('프로모션 코드', 1, 31);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 할인 코드', 1, 32);
INSERT INTO options (name, quantity, product_id) VALUES ('프로모션 할인', 1, 33);
INSERT INTO options (name, quantity, product_id) VALUES ('무료 쿠폰', 1, 34);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 할인 쿠폰', 1, 35);
INSERT INTO options (name, quantity, product_id) VALUES ('보너스 할인', 1, 36);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 보너스', 1, 37);
INSERT INTO options (name, quantity, product_id) VALUES ('프로모션 포인트', 1, 38);
INSERT INTO options (name, quantity, product_id) VALUES ('프로모션 쿠폰', 1, 39);
INSERT INTO options (name, quantity, product_id) VALUES ('무료 상품권', 1, 40);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 상품권', 1, 41);
INSERT INTO options (name, quantity, product_id) VALUES ('추가 쿠폰', 1, 42);

-- 상품권 카테고리 옵션
INSERT INTO options (name, quantity, product_id) VALUES ('문화 상품권 추가', 1, 43);
INSERT INTO options (name, quantity, product_id) VALUES ('도서 상품권 추가', 1, 44);
INSERT INTO options (name, quantity, product_id) VALUES ('백화점 상품권 추가', 1, 45);
INSERT INTO options (name, quantity, product_id) VALUES ('영화 관람 추가', 1, 46);
INSERT INTO options (name, quantity, product_id) VALUES ('외식 상품권 추가', 1, 47);
INSERT INTO options (name, quantity, product_id) VALUES ('커피 상품권 추가', 1, 48);
INSERT INTO options (name, quantity, product_id) VALUES ('편의점 상품권 추가', 1, 49);
INSERT INTO options (name, quantity, product_id) VALUES ('마트 상품권 추가', 1, 50);
INSERT INTO options (name, quantity, product_id) VALUES ('주유 상품권 추가', 1, 51);
INSERT INTO options (name, quantity, product_id) VALUES ('온라인 쇼핑몰 상품권 추가', 1, 52);
INSERT INTO options (name, quantity, product_id) VALUES ('스타벅스 상품권 추가', 1, 53);
INSERT INTO options (name, quantity, product_id) VALUES ('영화 상품권 추가', 1, 54);
INSERT INTO options (name, quantity, product_id) VALUES ('패스트푸드 상품권 추가', 1, 55);
INSERT INTO options (name, quantity, product_id) VALUES ('베이커리 상품권 추가', 1, 56);
INSERT INTO options (name, quantity, product_id) VALUES ('치킨 상품권 추가', 1, 57);
INSERT INTO options (name, quantity, product_id) VALUES ('피자 상품권 추가', 1, 58);
INSERT INTO options (name, quantity, product_id) VALUES ('아이스크림 상품권 추가', 1, 59);
INSERT INTO options (name, quantity, product_id) VALUES ('헬스장 이용권 추가', 1, 60);
INSERT INTO options (name, quantity, product_id) VALUES ('수영장 이용권 추가', 1, 61);
INSERT INTO options (name, quantity, product_id) VALUES ('독서실 이용권 추가', 1, 62);
INSERT INTO options (name, quantity, product_id) VALUES ('피트니스 클럽 이용권 추가', 1, 63);
INSERT INTO options (name, quantity, product_id) VALUES ('요가 클래스 이용권 추가', 1, 64);
INSERT INTO options (name, quantity, product_id) VALUES ('골프장 이용권 추가', 1, 65);
INSERT INTO options (name, quantity, product_id) VALUES ('테니스장 이용권 추가', 1, 66);
INSERT INTO options (name, quantity, product_id) VALUES ('스키장 이용권 추가', 1, 67);
INSERT INTO options (name, quantity, product_id) VALUES ('볼링장 이용권 추가', 1, 68);
INSERT INTO options (name, quantity, product_id) VALUES ('노래방 이용권 추가', 1, 69);
INSERT INTO options (name, quantity, product_id) VALUES ('PC방 이용권 추가', 1, 70);
INSERT INTO options (name, quantity, product_id) VALUES ('인터넷 카페 이용권 추가', 1, 71);
INSERT INTO options (name, quantity, product_id) VALUES ('미용실 이용권 추가', 1, 72);
INSERT INTO options (name, quantity, product_id) VALUES ('네일샵 이용권 추가', 1, 73);
INSERT INTO options (name, quantity, product_id) VALUES ('마사지샵 이용권 추가', 1, 74);
INSERT INTO options (name, quantity, product_id) VALUES ('영어학원 수강권 추가', 1, 75);
INSERT INTO options (name, quantity, product_id) VALUES ('요리학원 수강권 추가', 1, 76);
INSERT INTO options (name, quantity, product_id) VALUES ('피아노학원 수강권 추가', 1, 77);
INSERT INTO options (name, quantity, product_id) VALUES ('드럼학원 수강권 추가', 1, 78);
INSERT INTO options (name, quantity, product_id) VALUES ('기타학원 수강권 추가', 1, 79);
INSERT INTO options (name, quantity, product_id) VALUES ('바이올린학원 수강권 추가', 1, 80);
INSERT INTO options (name, quantity, product_id) VALUES ('플룻학원 수강권 추가', 1, 81);
INSERT INTO options (name, quantity, product_id) VALUES ('미술학원 수강권 추가', 1, 82);
INSERT INTO options (name, quantity, product_id) VALUES ('댄스학원 수강권 추가', 1, 83);
INSERT INTO options (name, quantity, product_id) VALUES ('연기학원 수강권 추가', 1, 84);


-- members
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email', 'password', 'nickname');
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email1', 'password1', 'nickname1');
INSERT INTO members (member_type, email, password, nickname) VALUES ('USER', 'email2', 'password2', 'nickname2');

-- wishes


