INSERT INTO zinzin.tag
VALUES (1, "ENFP"), (2, "ENTP"), (3, "ENFJ"), (4, "ENTJ"),
       (5, "ESTP"), (6, "ESFP"), (7, "ESTJ"), (8, "ESFJ"),
       (9, "INFJ"), (10, "INTJ"), (11, "INFP"), (12, "INTP"),
       (13, "ISTJ"), (14, "ISFJ"), (15, "ISTP"), (16, "ISFP"),
       (17, "융통성 있는"), (18, "단순한"), (19, "침착한"), (20, "의지가 강한"),
       (21, "아이디어가 많은"), (22, "꼼꼼한"), (23, "변덕스러운"), (24, "열정적인"),
       (25, "전략적인"), (26, "대인관계가 원만한"), (27, "변화를 즐기는"), (28, "모임을 즐기는"),
       (29, "유연한"), (30, "독창적인"), (31, "신뢰할 수 있는"), (32, "체계적인"),
       (33, "설득력 있는"), (34, "느긋한"), (35, "겸손한"), (36, "동정심이 많은"),
       (37, "호기심 많은"), (38, "생기발랄한"), (39, "미래지향적인"), (40, "활동적인"),
       (41, "완벽을 추구하는"), (42, "계획적인"), (43, "집중력 있는"), (44, "대인관계가 넓은"),
       (45, "신중한"), (46, "적응력이 뛰어난"), (47, "수용적인"), (48, "끈기 있는"),
       (49, "소신 있는"), (50, "지도력 있는"), (51, "지혜로운"), (52, "결단력 있는"),
       (53, "충동적인"), (54, "혁신적인"), (55, "엄격한"), (56, "현재에 충실한"),
       (57, "단호한"), (58, "분석적인"), (59, "유머러스한"), (60, "경청하는"),
       (61, "언변이 뛰어난"), (62, "우유부단한"), (63, "인심 좋은"), (64, "절제력 있는"),
       (65, "사교적인"), (66, "이성적인"), (67, "이해가 빠른"), (68, "창의적인"),
       (69, "성실한"), (70, "현실적인"), (71, "협조적인"), (72, "안정적인"),
       (73, "실행력 있는"), (74, "통찰력 있는"), (75, "독립적인"), (76, "온화한"),
       (77, "도전적인"), (78, "개척적인"), (79, "주도적인"), (80, "인내심 있는"),
       (81, "논리적인"), (82, "감성적인"), (83, "즐거움을 찾는"), (84, "에너지 있는"),
       (85, "이해심 많은"), (86, "인기 있는"), (87, "친절한"), (88, "활발한"),
       (89, "조용한"), (90, "외향적인"), (91, "내향적인"), (92, "모험적인"),
       (93, "이상적인"), (94, "대담한"), (95, "진취적인"), (96, "차분한"),
       (97, "직설적인"), (98, "정직한"), (99, "긍정적인"), (100, "책임감 있는")
       ON DUPLICATE KEY UPDATE content=VALUES(content);