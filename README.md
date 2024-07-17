# spring-gift-enhancement

## 기능 요구 사항
1. 상품 정보에 카테고리를 추가한다.
   1. 상품 카테고리는 수정할 수 있다.
   2. 관리자 화면에서 상품을 추가할 때 카테고리를 지정할 수 있다.
2. 카테고리는 1차 카테고리만 있으며 2차 카테고리는 고려하지 않는다.
3. 카테고리의 예시는 아래와 같아.
   1. 교환권, 상품권, 뷰티, 패션, 식품, 리빙/도서, 레저/스포츠, 아티스트/캐릭터, 유아동/반려, 디지털/가전, 카카오프렌즈, 트렌드 선물, 백화점 ...
아래 예시와 같이 HTTP 메시지를 주고 받도록 구현한다.
**Request**
```angular2html
GET /api/categories HTTP/1.1
```
**Response**
```angular2html
HTTP/1.1 200 
Content-Type: application/json

[
  {
    "id": 91,
    "name": "교환권",
    "color": "#6c95d1",
    "imageUrl": "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png",
    "description": ""
  }
]
```

### 힌트
아래의 DDL을 보고 유추한다.
```angular2html
create table category
(
    id   bigint       not null auto_increment,
    name varchar(255) not null,
    primary key (id)
) engine=InnoDB

create table product
(
    price       integer      not null,
    category_id bigint       not null,
    id          bigint       not null auto_increment,
    name        varchar(15)  not null,
    image_url   varchar(255) not null,
    primary key (id)
) engine=InnoDB

alter table category
    add constraint uk_category unique (name)

alter table product
    add constraint fk_product_category_id_ref_category_id
        foreign key (category_id)
            references category (id)

```