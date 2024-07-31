INSERT INTO MEMBER (EMAIL, PASSWORD)
VALUES ('test@naver.com', '123');

INSERT INTO CATEGORY (ID, COLOR, DESCRIPTION, IMAGE_URL, NAME)
VALUES (91, '#6c95d1', '', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '교환권');
INSERT INTO CATEGORY (ID, COLOR, DESCRIPTION, IMAGE_URL, NAME)
VALUES (92, '#6c95d1', '', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '상품권');
INSERT INTO CATEGORY (ID, COLOR, DESCRIPTION, IMAGE_URL, NAME)
VALUES (93, '#6c95d1', '', 'https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png', '뷰티');

INSERT INTO Product (name, price, image_url, category_id) VALUES
('딥디크 도 손 EDT', 270000, 'https://image.sivillage.com/upload/C00001/goods/org/096/210504001215096.jpg?RS=600&SP=1', 93),
('바닐라 크림 콜드 브루', 5400, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000487]_20210430112319040.jpg', 93),
('아이스 아메리카노', 5000, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000038]_20210430113202458.jpg', 93),
('오트 콜드브루', 6500, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000003285]_20210416154437069.jpg', 93),
('나이트로 콜드 브루', 6400, 'https://image.istarbucks.co.kr/upload/store/skuimg/2021/04/[9200000000479]_20210426091843897.jpg', 93),
('책', 20000, 'https://example.com/book.jpg', 93),
('스마트폰', 800000, 'https://example.com/smartphone.jpg', 91),
('로지텍 코리아 무선 키보드', 42000, 'https://img.danawa.com/prod_img/500000/959/427/img/3427959_1.jpg?_v=20221101171649', 91),
('마우스', 30000, 'https://example.com/mouse.jpg', 91),
('이어폰', 60000, 'https://example.com/earphone.jpg', 91),
('헤드폰', 100000, 'https://example.com/headphone.jpg', 91),
('카메라', 300000, 'https://example.com/camera.jpg', 91),
('그래픽 카드', 800000, 'https://example.com/gpu.jpg', 91),
('메모리', 150000, 'https://example.com/memory.jpg', 91),
('모니터', 400000, 'https://example.com/monitor.jpg', 91),
('마우스 패드', 20000, 'https://example.com/mousepad.jpg', 91),
('키링', 10000, 'https://example.com/keyring.jpg', 93),
('백팩', 70000, 'https://example.com/backpack.jpg', 93),
('의자', 150000, 'https://example.com/chair.jpg', 93),
('커피 머신', 250000, 'https://example.com/coffee-machine.jpg', 92),
('라면', 3000, 'https://example.com/ramen.jpg', 93),
('스피커', 80000, 'https://example.com/speaker.jpg', 91),
('텔레비전', 500000, 'https://example.com/television.jpg', 92),
('게임기', 350000, 'https://example.com/game-console.jpg', 91),
('블루투스 스피커', 90000, 'https://example.com/bluetooth-speaker.jpg', 91),
('선풍기', 40000, 'https://example.com/fan.jpg', 92),
('에어컨', 700000, 'https://example.com/air-conditioner.jpg', 92),
('냉장고', 900000, 'https://example.com/fridge.jpg', 92),
('전기밥솥', 150000, 'https://example.com/rice-cooker.jpg', 92),
('전자레인지', 100000, 'https://example.com/microwave.jpg', 92),
('믹서기', 80000, 'https://example.com/blender.jpg', 92),
('청소기', 200000, 'https://example.com/vacuum.jpg', 92),
('다리미', 30000, 'https://example.com/iron.jpg', 92),
('세탁기', 600000, 'https://example.com/washer.jpg', 92),
('건조기', 500000, 'https://example.com/dryer.jpg', 92),
('전기포트', 25000, 'https://example.com/kettle.jpg', 92),
('토스터기', 30000, 'https://example.com/toaster.jpg', 92),
('헤어드라이기', 40000, 'https://example.com/hair-dryer.jpg', 92),
('헤어스타일러', 70000, 'https://example.com/hair-styler.jpg', 92),
('수세미', 2000, 'https://example.com/scrubber.jpg', 93),
('비타민', 30000, 'https://example.com/vitamin.jpg', 93),
('운동화', 120000, 'https://example.com/sneakers.jpg', 93);

INSERT INTO WISH (MEMBER_ID, PRODUCT_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (1, 4),
       (1, 5),
       (1, 6);

INSERT INTO OPTION (PRODUCT_ID, NAME, QUANTITY)
VALUES (6, 'Java의 정석', 500),
       (6, '모던 자바 인 액션', 500),
       (6, '웹 프로그래머를 위한 데이터베이스를 지탱하는 기술', 400),
       (6, '데미안', 800),
       (6, '불편한 편의점', 500),
       (8, '샌드 색상', 10),
       (8, '라벤더 색상', 15),
       (8, '화이트 색상', 20),
       (8, '핑크 색상', 30);
