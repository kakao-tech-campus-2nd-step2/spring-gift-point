insert into category (name, created_at, last_modified_at, color, description, image_url)
values ('SPORT', '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', '#6c95d1', '스포츠 관련 상품',
        'https://rapidapi.com/hub/_next/image?url=https%3A%2F%2Frapidapi-prod-collections.s3.amazonaws.com%2Fcollection%2FiStock-1355687112.jpg.jpg&w=750&q=75');
insert into category (name, created_at, last_modified_at, color, description, image_url)
values ('CLOTH', '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', '#39ed6f', '옷 관련 상품',
        'https://collectiv-image-prod-bxh3gve5dvfragea.z03.azurefd.net/20240527/w1000q85_77RWKrxbBiyRvVEkrGVu2t.jpeg');
insert into category (name, created_at, last_modified_at, color, description, image_url)
values ('BEAUTY', '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', '#db9b97', '뷰티 관련 상품',
        'https://www.chanel.com/images//t_one//w_0.38,h_0.38,c_crop/q_auto:good,f_autoplus,fl_lossy,dpr_1.1/w_1240/rouge-allure-luminous-intense-lip-colour-136-melodieuse-0-12oz--packshot-default-160136-8800038027294.jpg');
insert into category (name, created_at, last_modified_at, color, description, image_url)
values ('FOOD', '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', '#e2ed8c', '#음식 관련 상품',
        'https://webudding.com/_next/image/?url=https%3A%2F%2Fd29hudvzbgrxww.cloudfront.net%2Fpublic%2Fproduct%2F20231129005319-8526fb19-f808-4080-88f6-b040f1621bbd.JPG&w=3840&q=80');
insert into category (name, created_at, last_modified_at, color, description, image_url)
values ('DRINK', '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', '#e3afc5', '음료 관련 상품',
        'https://cdn.ownerclan.com/LKE9FjjoIK6hWHoRyV~2DOduqYcZb7tCavl9kOITUzc/marketize/640/as/v1.jpg');

--SPORT
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('야구공', 4750,
        'https://i.namu.wiki/i/8SywmJ9jhxtIx3sGkBFIJZ4K48Fc1L5Os8xyQw7petJ-CQMNyqVn4RZNhLMVKtSOymcFcnLMJmAPkyZUbZk1QKbbq46HDX-BhpgyQ2hcz-qFXJsu7Yijj8hwXgBWpW_wqRtNTaC_2Qetu-8QZW-now.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('방망이', 548,
        'https://i.namu.wiki/i/Ocqip2Yfvt-FPGoQa29qm-xj4lCh69vKE-w1i2-2rcsMIjvByrgTrkQiFqBnndMQe0mVUJ6LDSjvtPo5xtk7hVttk78LMmknj_S7dHm9XHWZ-x4g1oVC3UHaC5g043u2uRT75Z1ug1OQAk2DtQPbyg.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('농구공', 83663,
        'https://i.namu.wiki/i/buCrpZpZ0qYv_Us0ZQ951go2IWkbDmIOfQ-pawF1jZeY4E-8CHBB36k_mTjYdpUm8fUzECurI3zTFwdZ2RiZoRDOEYdFI29P9dnzvoAMx1vt-b0y3BVVhwfQWkeporoQhsE9zWfYJbHiTqFi5bJO3w.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('축구공', 54900,
        'https://i.namu.wiki/i/k54GOIiyyEeEoJ_hI0Z5dFHq2hyAmwoxaWVbUXl58IrA-mkdomte9SyTtnooT8FvM7Z_3OXl6-DfRZ-DysCNfKgdSUfLyDGj-qudU8V2VRYaDzy8yATqU28sI1LiBwGxuiQNkdDsNQp2_7Xks4F17Q.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('당구채', 78201,
        'https://cdn.011st.com/11dims/resize/600x600/quality/75/11src/product/2136748422/B.jpg?954000000',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('스키', 55280,
        'http://www.drspark.net/files/attach/images/112/128/599/002/da68e8789f6761dd3c34d077ab7a2fea.jpg',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 1);
--CLOTH
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('후드집업', 98380, 'https://img.shoppingntmall.com/goods/237/11521237_h.jpg',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('바지', 87688,
        'https://i.namu.wiki/i/97CU2lrzklOx7UVJBgQHzwbdiLOvMnfYOT4tg6kwTFhv_71wRc-dCL5GzKuXFdpOiuQ7iFbUHMoDfBV_NhW_t76xuL986ss6mAxiyIUTc3tvYkkbz5GQWAtVvzubCclNfiMmJVMKBfkK1QeEf15evw.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('치마', 61291,
        'https://i.namu.wiki/i/wcflCMfrqUZ8eZBCNwcs857-iWxN-f1ehUYZAkNuGxoHwo-QL78BD1bDVme__aOF4A5-7DlwUSYzdwtvWOJLvlGlMFC15tG-JxuZv4RT6j2v_h3eVBbdQ_e6dE8PYEyCSU5ziBuljwsVSlsBHXw0aQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('모자', 61006,
        'https://i.namu.wiki/i/P9gDoHykQiVFm5Cz66QskF5NLe9Cz1CcLiV4vVbgRUOhPo5DRWW_eoh3rCjggJ4KpVd0WY2r2FVV9TLMGKZDlWxd9t4WEKkxPehtj_PgDrU1R8Ean-bVTKK5nzEaKcQCU7P5fee1DG9rOf3mjrj7bA.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('장갑', 33492,
        'https://i.namu.wiki/i/4HUwyXi8M30pbwkh9_qOQG5sPKBneuczbTIsIkkl4zSgCkq7XOzGLeF9RkEA5IvK97rvkUQzRZQBRaW328jKnE5lqNyeOAfvYB8WEPRl5ip23Wevec2Ylo5rkRk3PFu3qQXuBrVOU3aDz3z_zGrZYQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('신발', 16626,
        'https://i.namu.wiki/i/2Xq18JI7cmDET2HH3CUc3BMGdAV56HjXYiX48om7Z_KTcnrTF62-fuV2eqoP9e1nfulVnjIUqASjNO1jutLoyWLPkkPjjdCr17fz4Qs-yJ5PyD6BbP7Bg93M6o87BZaBVGuw4iEn1Ma1lzwg6r-cqQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 2);

--BEAUTY
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('립스틱', 34480,
        'https://i.namu.wiki/i/GcvvOTvOJR2_8aFGPQJWw_CgHIk_Um1rO6KA8QEjcgF7C_y3zfmoMlEifxgXvyHpkhj_qICuhn6HUWaAHkr0gyVDfYbz8dBdFL9-aW-2eoxCwemtFn_C5m3sgwdU8Oopx6nsWykgHjSwbvPMZME8bQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('뷰러', 24837,
        'https://i.namu.wiki/i/Ko2SJjF14EKKByNAVqsVAGSH85v7ULyeR6PqVRH9OHV-rpLXJV2G6q7ioSBTIZ3sad1MFZhNA1VpWS9L8QIjCyB6Jzi-WS-UjNl1asnfrZT31RwYxAIVVU7X7BBC3oLu4hQMrBHv3AHQWKw7ZQ4_Tw.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('컨실러', 18980,
        'https://i.namu.wiki/i/aN6jblJXIyS2bO5xoGKgZfVmNiOkeYZ8Bm3Tuh4RyLmmaOeNX6Z0ZR6I4FgHc3f1a2ImBcyRZjVJXNCOz-KqWKc8aXiZgRuLSLrLjyezHXN41q1LEwllfi0Ykli0DICwL3MdHnbl1U1arojowzgh6A.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('마스카라', 10113,
        'https://i.namu.wiki/i/0lx4PP8croK659ReBb9MGDLBq8P3DEqdU0Y7-oxPyC0znTv6IaBgK-yb6yp83Gia4xPR4ehvfLhmOXuYo8TFXe3Tc3B0u6hRTg4RzpmlHDXUz_QcIcVEnHM-r6PmnL8a2sU4jlv1eENztsR8DRSU6g.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('붓', 86388,
        'https://i.namu.wiki/i/GScq6ffwN1i2hI0MhB6U3IorxutJAyqO6eGAZVZdSQAtb5sBn9bEsB-CKEQZkC7iKd0dqoezX8MsgKsj8DgIeuRk8ZZR-OQG67-U7N5XSpIPHl1K0RifqExPkxYEL5-0GROQbl0E9le-XOK0xsuV8A.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('파운데이션', 7423, 'https://sdcdn.io/mac/kr/mac_sku_SYP847_1x1_0.png?width=1080&height=1080',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 3);
--FOOD
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('치킨', 6880,
        'https://i.namu.wiki/i/fczlzyxBhQHw2puTyMrV9mXPHzPOm57HMNvUQlhjPdTDj_JdnInOU0pAWLZ5anwTOek5u0rLwyFOh-IR_MJM0dbFnARr6-3OrLrO3tMzNWkpWlKpGqis2K31f0uxAbe3uI5bTrAKO8Z4lDzdTtMJig.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('밥', 6880,
        'https://i.namu.wiki/i/hCDvn4ID8h_L7qDp35Eyu_kH_Vz0sYznjLJTCclDiEcsFyZpEST2okW-vPvUx7JS8fGBaMIVMsS0grMd3ysc_Anx__IXLxxMXGYJzFKiypKngMiHf2IRcvxCnWGOjAegWynXYLMG5qdiuZK5ItJ_Jw.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('냉면', 6880,
        'https://i.namu.wiki/i/3Bs0MgoD7ADggUKd-azGoxJtTi0psnn7YyW9avV2WRnd221w0iurrbMsWaoBfFfemog99_nu0bdX84NUMNeiSNM-KEuOUuTFA5aoH5sstYhhJQZeuiR0ofJKSaUoIfnKcI7_3dUBc5UcpEOXHk4tfQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('짜장면', 6880,
        'https://i.namu.wiki/i/SaCq1IIzuXQRAbeGLvkL30S79Z9vu6o25Ny58DbNLkRhF-0WrmIkqOpTpHfFHuOijxt1QTNmkRkhfaHoCxpwV6qbw42Xcye0SZrjB8GCv8CPz-iv5Z6hfnRvTYYVAIRA2hJPyv_kfNGa9PqGDee06A.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('돈까스', 6880,
        'https://i.namu.wiki/i/rXE_sEr54QZ6BMalcmGjdZ-r7AqE8aJR7o_Y4pxnODPFUx6aQbEpsZj_apDlxR_Q_SWdMSHLvKRxh3W2g-nHVjU848jW3e7PYzI2bsKCAej-hkeMcNcuo29LRcp-aIlNMhwPsItQm0J8KyHjLKkKMA.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('삼겹살', 6880,
        'https://i.namu.wiki/i/YZTjqwr3YEHtvxa96AZaRObcM6XTxuWUcoZhEvGtjceN9z10hmE71MiUPNWRaYzl52-FBqFGkE6Dx6D_bRS6OlIE7HTgkKCzmGn_jtIooigDxeiMOuhOG9SpIdKVoZ4Uc7jhGipZhgHb0FfBcYR0VQ.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 4);
--DRINK
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('물', 6880,
        'https://i.namu.wiki/i/ZWXPczCdsXMXGqxtiYeyjljwu5X_kAhVl7JBPXy8f-MoVfzoqMqwB_7d1Df9PfibPut6spz3v-1MbTPl9XamLroJNidWNWoUsYnnMuXxrb172Ledy7er8GbC-RuANeQpIDq8lZXTwg_oPMai0zF7ag.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('커피', 7880,
        'https://i.namu.wiki/i/PvY50I1mh55qRjpNcuV5Yc2w8R50kg30F2fTuC1lZG16tIgXEp7D--FnDosg-079NFIYzpXn6c8nnZ-vrkoP7stDGLYUj8JGiPENeywP6DKe-B340BjZL1tW4Q7Y2aJ0hBVoihqGjMSVfL6D23-kXw.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('파워에이드', 6880,
        'https://i.namu.wiki/i/HY1hx76d8zu7-NrusJfyQTyNsgBQDU-KfVP2rTGLXrC-XaonG4uGAXPLECjzYCZMNFE3kq3ZwaxSs9Js9o5K6x8AUVz9TFQz4xRgMZ7FvPy3XVAHBQv9j3SubY4mI8RzBYpmcxKugNM2o3FVS_d7Jg.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('게토레이', 6880,
        'https://i.namu.wiki/i/KHro7OU0_u3USuGWcjnFgagMfBHFauit-jQAo8Rzo-b82sU0Brtmh4qrI5Mc2TZIo7vO6EUzw_omDJPPmMuUuSFd3UyKBhlq2dh7m_d-oDF9afh62LhNUedsAmcp4vBKqtVxSXmbxwHL2wUHm4vCjw.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('차', 6880,
        'https://i.namu.wiki/i/-8pi8lLoEH3DOtqwfntBHrwMgqTxbJxIyfXKT_wV66f5JKWfFFiMnUx23UOjAm50HfRuOJkc9u_RcNan3xgZvaO0_qlj33lSf9PO2qI-JceYvWWbMWKBY6zM_y9Qsm6UaqpZgz5yF11i-IrUALSLFA.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);
insert into product (name, price, image_url, created_at, last_modified_at, category_id)
values ('아이스티', 6880,
        'https://i.namu.wiki/i/Yxf8sL_5-6BvKE2NNvu-rZuNxGe76Vl4-VYvbSgpVJPq0XuoT3AwW9Ius-SV021Wu1NHYjTxHEUpbJaY8MFVCkxVdJOHTZ-mPQOTYx4_T5IjzVLUGSRasZokNZwyM5mw10oj-pqT1IZyX1Hhpp7VpA.webp',
        '2023-05-31 10:00:00',
        '2023-05-31 12:00:00', 5);


insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 1, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 1, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 1, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 2, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 2, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 2, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 3, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 3, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 3, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 4, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 4, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 4, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 6, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 6, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 6, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);

insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 7, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 7, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 7, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 8, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 8, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 8, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 9, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 9, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 9, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 10, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 10, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 10, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 11, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 11, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 11, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 12, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 12, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 12, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);

insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 14, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 14, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 14, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 15, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 15, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 15, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 16, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 16, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 16, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 17, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 17, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 17, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 18, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 18, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 18, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);

insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 19, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 19, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 19, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 20, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 20, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 20, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 21, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 21, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 21, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 22, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 22, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 22, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 23, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 23, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 23, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 24, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 24, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 24, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);

insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 25, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 25, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 25, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 26, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 26, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 26, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 27, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 27, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 27, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 28, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 28, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 28, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 29, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 29, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 29, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option1', 1000, 30, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option2', 1000, 30, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);
insert into option (name, quantity, product_id, created_at, last_modified_at, version)
values ('option3', 1000, 30, '2023-05-31 10:00:00', '2023-05-31 12:00:00', 1);

insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('Jarred', 'admin@test.com',
        '390d4757bf1b75e305984c99cdedfb1e7c201a2d143a53cfbc35075fa5f9a56f', 'ADMIN',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'ORIGIN', 100000);
insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('Jarred', 'test@test.com',
        'f9aac0519f30824bb09b2dbae6eb62633255d765c2272e8392d737332554926c', 'USER',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'ORIGIN', 0);
insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('Jarred1', 'test1@test.com',
        'f9aac0519f30824bb09b2dbae6eb62633255d765c2272e8392d737332554926c', 'ADMIN',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'ORIGIN', 0);
insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('Jarred2', 'test2@test.com',
        'f9aac0519f30824bb09b2dbae6eb62633255d765c2272e8392d737332554926c', 'USER',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'KAKAO', 0);
insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('Jarred3', 'test3@test.com',
        'f9aac0519f30824bb09b2dbae6eb62633255d765c2272e8392d737332554926c', 'ADMIN',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'ORIGIN', 0);
insert into member (name, email, password, role, created_at, last_modified_at, provider, point)
values ('down', 'test123@test.com',
        'f9aac0519f30824bb09b2dbae6eb62633255d765c2272e8392d737332554926c', 'ADMIN',
        '2023-05-31 10:00:00', '2023-05-31 12:00:00', 'KAKAO', 0);

insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 2, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00');
insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 4, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00');
insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 3, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00');
insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 14, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00');
insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 12, 5, '2023-05-31 10:00:00', '2023-05-31 12:00:00');
insert into wish (member_id, product_id, count, created_at, last_modified_at)
values (1, 13, 13, '2023-05-31 10:00:00', '2023-05-31 12:00:00');


