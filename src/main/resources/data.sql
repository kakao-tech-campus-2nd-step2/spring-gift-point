Insert Into category(name)
values ('교환권');
Insert Into category(name)
values ('상품권');
Insert Into category(name)
values ('뷰티');
Insert Into category(name)
values ('패션');
Insert Into category(name)
values ('식품');

INSERT INTO users(email, name, password, role)
values ('admin@admin.com', 'admin',
        '{bcrypt}$2a$10$Kd3Tjp96Ox4L6PTNpJ2byuDKRRUdFnJl89a85O8mTFzMQkyTowKFS', 'ADMIN')