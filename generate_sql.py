# -*- coding: utf-8 -*-
import os

customers_data = [
    # id, ma, ten, sdt, email, gioitinh, ngaysinh, hang, diachi, tongtientieu
    (1, "KH-00001", "Nguyễn Hoàng Anh", "0388741039", "hoanganh.duna@gmail.com", "Nam", "1995-05-10", "Bạc", "123 Trương Định, Hai Bà Trưng, Hà Nội", 3500000),
    (2, "KH-00002", "Trần Quốc Bảo", "0335533938", "quocbao.tran@gmail.com", "Nam", "1992-03-15", "Thường", "45 Lý Thường Kiệt, Hoàn Kiếm, Hà Nội", 1200000),
    (3, "KH-00003", "Phạm Minh Tuấn", "0388967119", "tuanpm.invest@gmail.com", "Nam", "1998-07-22", "Thường", "78 Đinh Tiên Hoàng, Quận 1, TP.HCM", 850000),
    (4, "KH-00004", "Đỗ Viết Hùng", "0355203320", "hungdv.fpt@gmail.com", "Nam", "1990-11-08", "Thường", "12 Nguyễn Huệ, Quận Hải Châu, Đà Nẵng", 600000),
    (5, "KH-00005", "Lê Quang Hải", "0912345678", "hai.lequang@gmail.com", "Nam", "1993-08-14", "Thường", "99 Hoàng Diệu, Ba Đình, Hà Nội", 1500000),
    (6, "KH-00006", "Khách lẻ vãng lai", "0987654321", "khachle@duna.vn", "Nam", "1995-01-01", "Thường", "Mua trực tiếp tại showroom DUNA", 0),
    (7, "KH-00007", "Hoàng Mạnh Cường", "0987112233", "cuonghm.tech@gmail.com", "Nam", "1994-02-18", "Vàng", "24 Láng Hạ, Đống Đa, Hà Nội", 6800000),
    (8, "KH-00008", "Bùi Thế Anh", "0976223344", "theanh.bui@gmail.com", "Nam", "1997-09-05", "Bạc", "56 Nguyễn Trãi, Thanh Xuân, Hà Nội", 3200000),
    (9, "KH-00009", "Đặng Minh Quân", "0903334455", "minhquan.dang@gmail.com", "Nam", "1996-12-30", "VIP", "88 Lê Lợi, Bến Nghé, Quận 1, TP.HCM", 14500000),
    (10, "KH-00010", "Phan Văn Đức", "0918445566", "ducpv.arch@gmail.com", "Nam", "1991-04-25", "Thường", "102 Bạch Đằng, Hải Châu, Đà Nẵng", 1450000),
    (11, "KH-00011", "Vũ Thành Long", "0935556677", "longvt.media@gmail.com", "Nam", "1995-10-12", "Vàng", "15 Trần Phú, Ngô Quyền, Hải Phòng", 8200000),
    (12, "KH-00012", "Dương Đình Trọng", "0947667788", "trongdd.duna@gmail.com", "Nam", "1998-01-20", "Thường", "36 Hòa Bình, Ninh Kiều, Cần Thơ", 950000),
    (13, "KH-00013", "Mai Xuân Tiến", "0968778899", "tienmx.lawyer@gmail.com", "Nam", "1989-06-14", "Bạc", "210 Xã Đàn, Đống Đa, Hà Nội", 4100000),
    (14, "KH-00014", "Nguyễn Tiến Dũng", "0979889900", "tiendung.nguyen@gmail.com", "Nam", "1993-03-08", "VIP", "150 Nguyễn Thị Minh Khai, Quận 3, TP.HCM", 18200000),
    (15, "KH-00015", "Trịnh Công Minh", "0981990011", "congminh.trinh@gmail.com", "Nam", "1990-08-27", "Thường", "72 Lý Thái Tổ, TP. Bắc Ninh", 1100000),
    (16, "KH-00016", "Lương Gia Huy", "0902112244", "giahuy.luong@gmail.com", "Nam", "1999-05-19", "Bạc", "65 Phan Châu Trinh, Hải Châu, Đà Nẵng", 2800000),
    (17, "KH-00017", "Hà Đức Chinh", "0913223355", "ducchinh.ha@gmail.com", "Nam", "1997-09-22", "Vàng", "89 Lê Thanh Nghị, TP. Hải Dương", 7500000),
    (18, "KH-00018", "Đoàn Văn Hậu", "0934334466", "vanhau.doan@gmail.com", "Nam", "1999-04-19", "VIP", "25 Liễu Giai, Ba Đình, Hà Nội", 22000000),
    (19, "KH-00019", "Quế Ngọc Hải", "0945445577", "ngochai.que@gmail.com", "Nam", "1993-05-15", "Vàng", "42 Quang Trung, TP. Vinh, Nghệ An", 9100000),
    (20, "KH-00020", "Đỗ Duy Mạnh", "0966556688", "duymanh.do@gmail.com", "Nam", "1996-09-29", "Bạc", "18 Cầu Giấy, Cầu Giấy, Hà Nội", 3900000),
    (21, "KH-00021", "Nguyễn Hồng Duy", "0977667799", "hongduy.nguyen@gmail.com", "Nam", "1996-06-13", "Thường", "15 Cách Mạng Tháng 8, Đồng Xoài, Bình Phước", 1600000),
    (22, "KH-00022", "Vũ Văn Thanh", "0988778800", "vanthanh.vu@gmail.com", "Nam", "1996-04-14", "Vàng", "68 Nguyễn Lương Bằng, TP. Hải Dương", 6400000),
    (23, "KH-00023", "Nguyễn Tuấn Anh", "0909889911", "tuananh.nguyen@gmail.com", "Nam", "1995-05-16", "VIP", "77 Trần Hưng Đạo, TP. Thái Bình", 16800000),
    (24, "KH-00024", "Lương Xuân Trường", "0919990022", "xuantruong.luong@gmail.com", "Nam", "1995-04-28", "Bạc", "12 Bình Thuận, TP. Tuyên Quang", 4500000),
    (25, "KH-00025", "Nguyễn Công Phượng", "0931001133", "congphuong.nguyen@gmail.com", "Nam", "1995-01-21", "VIP", "83 Lê Nin, TP. Vinh, Nghệ An", 25400000),
    (26, "KH-00026", "Trần Đình Trọng", "0942112244", "dinhtrong.tran@gmail.com", "Nam", "1997-04-25", "Thường", "93 Đa Tốn, Gia Lâm, Hà Nội", 800000),
    (27, "KH-00027", "Phạm Đức Huy", "0963223355", "duchuy.pham@gmail.com", "Nam", "1995-01-20", "Thường", "31 Nguyễn Trãi, Thanh Miện, Hải Dương", 1300000),
    (28, "KH-00028", "Nguyễn Huy Hùng", "0974334466", "huyhung.nguyen@gmail.com", "Nam", "1992-03-02", "Bạc", "54 Tây Sơn, Đống Đa, Hà Nội", 2400000),
    (29, "KH-00029", "Nguyễn Anh Đức", "0985445577", "anhduc.nguyen@gmail.com", "Nam", "1985-10-24", "Vàng", "102 Đại Lộ Bình Dương, Thủ Dầu Một", 8900000),
    (30, "KH-00030", "Phan Văn Tài Em", "0906556688", "taiem.phan@gmail.com", "Nam", "1982-04-23", "Thường", "48 Hùng Vương, TP. Tân An, Long An", 1750000),
    (31, "KH-00031", "Lê Công Vinh", "0917667799", "congvinh.le@gmail.com", "Nam", "1985-12-10", "VIP", "90 Nam Kỳ Khởi Nghĩa, Quận 1, TP.HCM", 28000000),
    (32, "KH-00032", "Huỳnh Kesley", "0938778800", "kesley.huynh@gmail.com", "Nam", "1981-11-27", "Bạc", "34 Yersin, TP. Thủ Dầu Một, Bình Dương", 3600000),
    (33, "KH-00033", "Nguyễn Minh Phương", "0949889911", "minhphuong.nguyen@gmail.com", "Nam", "1980-07-05", "Vàng", "61 Đồng Khởi, TP. Biên Hòa, Đồng Nai", 7100000),
    (34, "KH-00034", "Vũ Như Thành", "0961990022", "nhuthanh.vu@gmail.com", "Nam", "1981-08-28", "Thường", "19 Hàng Sắt, TP. Nam Định", 1250000),
    (35, "KH-00035", "Dương Hồng Sơn", "0972001133", "hongson.duong@gmail.com", "Nam", "1982-11-20", "Bạc", "74 Đặng Thái Thân, TP. Vinh, Nghệ An", 4300000),
    (36, "KH-00036", "Nguyễn Việt Thắng", "0983112244", "vietthang.nguyen@gmail.com", "Nam", "1981-09-13", "Vàng", "82 Trương Định, TP. Tân An, Long An", 5800000),
    (37, "KH-00037", "Thạch Bảo Khanh", "0904223355", "baokhanh.thach@gmail.com", "Nam", "1979-04-25", "Thường", "11 Hàng Bông, Hoàn Kiếm, Hà Nội", 900000),
    (38, "KH-00038", "Đặng Phương Nam", "0915334466", "phuongnam.dang@gmail.com", "Nam", "1976-12-15", "Bạc", "45 Mạc Thị Bưởi, TP. Nam Định", 3100000),
    (39, "KH-00039", "Nguyễn Hồng Sơn", "0936445577", "hongson.nguyen@gmail.com", "Nam", "1970-10-09", "VIP", "88 Phố Huế, Hai Bà Trưng, Hà Nội", 19500000),
    (40, "KH-00040", "Trương Việt Hoàng", "0947556688", "viethoang.truong@gmail.com", "Nam", "1975-12-09", "Vàng", "29 Lạch Tray, Ngô Quyền, Hải Phòng", 6200000),
    (41, "KH-00041", "Trần Công Minh", "0968667799", "congminh.tran@gmail.com", "Nam", "1970-09-01", "Thường", "14 Lý Thường Kiệt, TP. Cao Lãnh, Đồng Tháp", 1400000),
    (42, "KH-00042", "Lê Huỳnh Đức", "0979778800", "huynhduc.le@gmail.com", "Nam", "1972-04-20", "VIP", "120 Nguyễn Văn Linh, Thanh Khê, Đà Nẵng", 21000000),
    (43, "KH-00043", "Văn Sỹ Hùng", "0980889911", "syhung.van@gmail.com", "Nam", "1970-08-14", "Thường", "38 Lê Mao, TP. Vinh, Nghệ An", 750000),
    (44, "KH-00044", "Nguyễn Hữu Đang", "0901990022", "huudang.nguyen@gmail.com", "Nam", "1969-03-05", "Bạc", "52 Trần Phú, TP. Nha Trang, Khánh Hòa", 2900000),
    (45, "KH-00045", "Trần Minh Chiến", "0912001133", "minhchien.tran@gmail.com", "Nam", "1974-10-27", "Vàng", "180 Ba Tháng Hai, Quận 10, TP.HCM", 8500000),
    (46, "KH-00046", "Đỗ Khải", "0933112244", "dokhai.fpt@gmail.com", "Nam", "1974-04-01", "Thường", "95 Nguyễn Trãi, Quận 5, TP.HCM", 1150000),
    (47, "KH-00047", "Nguyễn Đức Thắng", "0944223355", "ducthang.nguyen@gmail.com", "Nam", "1976-05-28", "Bạc", "76 Đội Cấn, Ba Đình, Hà Nội", 3700000),
    (48, "KH-00048", "Triệu Quang Hà", "0965334466", "quangha.trieu@gmail.com", "Nam", "1975-09-03", "Thường", "60 Quang Trung, TP. Thanh Hóa", 1650000),
    (49, "KH-00049", "Võ Hoàng Bửu", "0976445577", "hoangbuu.vo@gmail.com", "Nam", "1968-07-10", "Vàng", "230 Hai Bà Trưng, Quận 1, TP.HCM", 5900000),
    (50, "KH-00050", "Nguyễn Liêm Thanh", "0987556688", "liemthanh.nguyen@gmail.com", "Nam", "1972-03-25", "Thường", "41 Võ Văn Tần, Quận 3, TP.HCM", 1050000)
]

# 64 new products: (id, ma, ten, cat_id, chatlieu, formdang, hinhanh, gianhap, giaban, mota)
products_new = [
    # Category 1: Áo Sơ Mi Nam (ID_DanhMuc = 1) - SP007 to SP020 (14 SP)
    (7, "SP007", "Áo Sơ Mi Nam Poplin Trắng Cổ Điển DUNA", 1, "100% Poplin Cotton dệt mịn", "Regular Fit", "img/sp-somi-poplin.jpg", 195000, 420000, "Vải poplin dệt mịn màng, bề mặt bóng nhẹ sang trọng, dễ ủi và luôn phẳng phiu."),
    (8, "SP008", "Áo Sơ Mi Nam Kẻ Sọc Xanh Slimfit DUNA", 1, "60% Cotton, 40% Bamboo kháng khuẩn", "Slim Fit", "img/sp-somi-kesoc.jpg", 215000, 460000, "Họa tiết sọc kẻ thanh lịch tạo cảm giác tôn dáng và cao ráo, chất liệu sợi tre mát lạnh."),
    (9, "SP009", "Áo Sơ Mi Nam Sợi Tre Bamboo DUNA Trắng", 1, "50% Bamboo, 50% Spun Poly mềm mại", "Slim Fit", "img/sp-somi-bamboo.jpg", 225000, 480000, "Sợi tre tự nhiên kháng khuẩn, hút ẩm vượt trội và chống tia cực tím hiệu quả."),
    (10, "SP010", "Áo Sơ Mi Nam Cổ Trụ Grandad DUNA Linen", 1, "100% Linen Pháp dệt thưa mát mẻ", "Relaxed Fit", "img/sp-somi-cotru.jpg", 240000, 520000, "Cổ trụ phong cách tối giản thanh lịch, chất đũi tự nhiên mát lịm ngày hè."),
    (11, "SP011", "Áo Sơ Mi Denim Nam DUNA Vintage Indigo", 1, "100% Cotton Denim wash mềm", "Regular Fit", "img/sp-somi-denim.jpg", 230000, 490000, "Vải denim wash xanh chàm bụi bặm nhưng vẫn giữ được nét lịch thiệp khi phối quần âu/khaki."),
    (12, "SP012", "Áo Sơ Mi Nam Ngắn Tay Họa Tiết DUNA Summer", 1, "100% Cotton Rayon thoáng mát", "Regular Fit", "img/sp-somi-ngantay-hoatiet.jpg", 185000, 390000, "Sơ mi cộc tay họa tiết hoa văn chìm tinh tế, thích hợp cho chuyến du lịch hoặc dạo phố cuối tuần."),
    (13, "SP013", "Áo Sơ Mi Nam Flannel Kẻ Caro DUNA Warm", 1, "100% Flannel Cotton chải lông ấm áp", "Regular Fit", "img/sp-somi-flannel.jpg", 220000, 470000, "Sơ mi flannel dày dặn mùa thu đông, giữ nhiệt tốt, phối khoác ngoài áo thun cực kỳ năng động."),
    (14, "SP014", "Áo Sơ Mi Nam Cổ Bẻ Spread Collar DUNA Silk", 1, "85% Cotton, 15% Silk lụa tự nhiên", "Tailored Fit", "img/sp-somi-silk.jpg", 260000, 560000, "Dòng sơ mi may đo cao cấp cổ bẻ rộng chuyên thắt cà vạt cho các cuộc họp và sự kiện quan trọng."),
    (15, "SP015", "Áo Sơ Mi Nam Xanh Baby Slimfit DUNA", 1, "100% Compact Cotton chống nhăn", "Slim Fit", "img/sp-somi-xanhbaby.jpg", 205000, 440000, "Gam màu xanh baby nhã nhặn, tôn sáng làn da nam giới, form ôm nhẹ hiện đại."),
    (16, "SP016", "Áo Sơ Mi Nam Kẻ Windowpane DUNA Smart", 1, "100% Cotton dệt ô cửa sổ mảnh", "Slim Fit", "img/sp-somi-windowpane.jpg", 210000, 450000, "Họa tiết kẻ ô mảnh thời thượng, phong thái đĩnh đạc cho quý ông hiện đại."),
    (17, "SP017", "Áo Sơ Mi Nam Trắng Cổ Vát Cutaway DUNA", 1, "100% Cotton Ai Cập sợi dài", "Tailored Fit", "img/sp-somi-cutaway.jpg", 235000, 500000, "Thiết kế cổ vát góc rộng sang trọng, điểm nhấn tinh tế khi mặc kèm suit jacket."),
    (18, "SP018", "Áo Sơ Mi Nam Dài Tay Màu Xám Khói DUNA", 1, "65% Cotton, 35% Polyester chống nhăn", "Regular Fit", "img/sp-somi-xamkhoi.jpg", 200000, 430000, "Tông màu xám khói trung tính thanh lịch, dễ phối cùng mọi loại quần âu và khaki."),
    (19, "SP019", "Áo Sơ Mi Nam Màu Đen Tuyết DUNA Night", 1, "100% Cotton chải kỹ nhuộm hoạt tính", "Slim Fit", "img/sp-somi-dentuyet.jpg", 215000, 460000, "Sắc đen tuyền huyền bí, không phai màu sau nhiều lần giặt, tôn dáng nam tính."),
    (20, "SP020", "Áo Sơ Mi Nam Ngắn Tay Trắng DUNA Casual", 1, "100% Cotton Poplin xử lý chống nhăn", "Regular Fit", "img/sp-somi-ngantay-trang.jpg", 175000, 380000, "Áo sơ mi cộc tay công sở mát nhẹ ngày hè, tiện dụng thoải mái vận động."),

    # Category 2: Áo Polo Nam (ID_DanhMuc = 2) - SP021 to SP034 (14 SP)
    (21, "SP021", "Áo Polo Nam DUNA CVC Cá Sấu Co Giãn 4 Chiều", 2, "65% Cotton, 35% Poly cá sấu 4 chiều", "Regular Fit", "img/sp-polo-cvc.jpg", 160000, 350000, "Vải mắt chim cá sấu thoáng khí, thấm hút mồ hôi, co giãn 4 chiều vận động thoải mái."),
    (22, "SP022", "Áo Polo Nam DUNA Phối Bo Cổ Viền Màu", 2, "95% Cotton compact, 5% Spandex", "Slim Fit", "img/sp-polo-bo-vien.jpg", 170000, 370000, "Điểm nhấn đường viền tương phản ở cổ và tay áo, phong cách thể thao trẻ trung."),
    (23, "SP023", "Áo Polo Nam Coolmax DUNA Thoáng Khí Kháng Khuẩn", 2, "100% Sợi Coolmax công nghệ làm mát", "Regular Fit", "img/sp-polo-coolmax.jpg", 190000, 420000, "Công nghệ sợi rãnh thoát nhiệt cực nhanh, giữ cơ thể luôn khô ráo trong những ngày nắng nóng."),
    (24, "SP024", "Áo Polo Nam DUNA Cổ Khóa Kéo Zip Polo Hiện Đại", 2, "95% Pima Cotton, 5% Elastane", "Slim Fit", "img/sp-polo-zip.jpg", 205000, 450000, "Khóa kéo kim loại sáng bóng thay cho khuy cài truyền thống, thiết kế phá cách hiện đại."),
    (25, "SP025", "Áo Polo Nam DUNA Dệt Tổ Ong Waffle Knit", 2, "100% Cotton dệt vân tổ ong 3D", "Regular Fit", "img/sp-polo-waffle.jpg", 180000, 390000, "Cấu trúc dệt tổ ong thoáng khí và có chiều sâu bề mặt vải, mang lại vẻ ngoài lịch lãm."),
    (26, "SP026", "Áo Polo Nam DUNA Kẻ Ngang Thủy Thủ Hè", 2, "100% Cotton chải kỹ dệt sọc yarn-dyed", "Regular Fit", "img/sp-polo-sockey.jpg", 175000, 380000, "Sọc kẻ ngang phong cách nautical trẻ trung, năng động cho các dịp đi chơi cuối tuần."),
    (27, "SP027", "Áo Polo Nam DUNA Mercerized Cotton Ánh Kim", 2, "100% Mercerized Cotton cao cấp", "Tailored Fit", "img/sp-polo-mercerized.jpg", 220000, 480000, "Sợi cotton được kiềm hóa bóng mượt như lụa, độ bền màu tuyệt đối và không xù lông."),
    (28, "SP028", "Áo Polo Nam DUNA Cổ Trụ Tàu Trẻ Trung", 2, "95% Cotton, 5% Spandex", "Slim Fit", "img/sp-polo-cotru.jpg", 165000, 360000, "Cổ trụ tàu gọn gàng cá tính, dễ phối đồ dạo phố và đi làm thường ngày."),
    (29, "SP029", "Áo Polo Nam DUNA Thể Thao Quick-Dry Golf", 2, "88% Polyester, 12% Spandex Quick-Dry", "Athletic Fit", "img/sp-polo-golf.jpg", 195000, 430000, "Vải thể thao chuyên dụng chơi golf, co giãn tối đa và chống nhăn tuyệt đối."),
    (30, "SP030", "Áo Polo Nam DUNA Màu Xanh Rêu Olive Vintage", 2, "95% Cotton chải mềm, 5% Spandex", "Regular Fit", "img/sp-polo-olive.jpg", 170000, 370000, "Sắc xanh rêu phong trần, nam tính, dễ kết hợp với quần khaki màu cát hoặc quần âu đen."),
    (31, "SP031", "Áo Polo Nam DUNA Màu Nâu Be Phong Cách Hàn Quốc", 2, "95% Cotton Organic, 5% Spandex", "Relaxed Fit", "img/sp-polo-naube.jpg", 175000, 380000, "Gam màu nâu be ấm áp phong cách Hàn Quốc, phom rộng rãi phóng khoáng."),
    (32, "SP032", "Áo Polo Nam DUNA Đen Trơn Minimalist Form Chuẩn", 2, "100% Pima Cotton dệt mịn", "Slim Fit", "img/sp-polo-den.jpg", 165000, 360000, "Thiết kế đen tuyền cơ bản không bao giờ lỗi mốt, item bắt buộc trong tủ đồ phái mạnh."),
    (33, "SP033", "Áo Polo Nam DUNA Dệt Kim Dài Tay Thu Đông", 2, "80% Cotton, 20% Wool mỏng nhẹ", "Regular Fit", "img/sp-polo-daitay.jpg", 210000, 460000, "Polo tay dài len dệt kim mỏng ấm áp thời tiết giao mùa, lịch sự khi đi làm."),
    (34, "SP034", "Áo Polo Nam DUNA Vải Modal Siêu Mềm Mịn", 2, "50% Modal gỗ sồi, 50% Cotton", "Regular Fit", "img/sp-polo-modal.jpg", 185000, 410000, "Sợi modal chiết xuất từ gỗ sồi tự nhiên cho bề mặt êm dịu vượt trội, làm mát tức thì."),

    # Category 3: Quần Âu Nam (ID_DanhMuc = 3) - SP035 to SP045 (11 SP)
    (35, "SP035", "Quần Âu Nam DUNA Regular Cạp Tăng Giảm Thông Minh", 3, "70% Polyester, 28% Rayon, 2% Spandex", "Regular Fit", "img/sp-quan-au-captang.jpg", 250000, 550000, "Cạp quần có nút trượt thông minh co giãn linh hoạt 3cm, cực kỳ thoải mái khi ngồi làm việc lâu."),
    (36, "SP036", "Quần Âu Nam Xếp Ly 1 Ly Hàn Quốc DUNA Classic", 3, "Polyester Rayon chải sợi kỹ", "Relaxed Tapered", "img/sp-quan-au-xepli.jpg", 265000, 580000, "Đường xếp ly 1 ly tinh xảo phong cách retro thanh lịch, tôn dáng đĩnh đạc."),
    (37, "SP037", "Quần Âu Nam Vải Tuytsi Cao Cấp Đứng Dáng DUNA", 3, "Vải Tuytsi biên vàng chống nhăn", "Slim Fit", "img/sp-quan-au-tuytsi.jpg", 280000, 620000, "Chất liệu tuytsi cao cấp mặt vải đanh chắc, nếp ly vĩnh viễn không cần ủi lại sau khi giặt."),
    (38, "SP038", "Quần Âu Nam Màu Xanh Navy Sọc Chìm DUNA Smart", 3, "Poly Rayon Spandex vân sọc nhỏ", "Slim Fit", "img/sp-quan-au-navysoc.jpg", 255000, 560000, "Họa tiết sọc chìm tôn chiều dài đôi chân, màu xanh navy chuẩn công sở chuyên nghiệp."),
    (39, "SP039", "Quần Âu Nam DUNA Màu Xám Khói Ống Đứng Lịch Lãm", 3, "Poly Viscose pha Spandex co giãn", "Straight Fit", "img/sp-quan-au-xamkhoi.jpg", 240000, 530000, "Ống đứng suông nhẹ giấu khuyết điểm chân, màu xám khói dễ phối cùng sơ mi và polo."),
    (40, "SP040", "Quần Âu Nam DUNA Co Giãn 4 Chiều Công Sở", 3, "80% Poly, 20% Spandex siêu đàn hồi", "Slim Fit", "img/sp-quan-au-cogian4c.jpg", 220000, 490000, "Độ đàn hồi cực cao, đứng lên ngồi xuống thoải mái không hề bị gò bó."),
    (41, "SP041", "Quần Âu Nam DUNA Wool Blend Giữ Phom Chuẩn Ý", 3, "50% Wool, 50% Poly dệt thoi cao cấp", "Tailored Fit", "img/sp-quan-au-wool.jpg", 310000, 680000, "Pha len tự nhiên tạo độ bóng và rủ vải sang trọng, phù hợp dự tiệc và lễ tân ngoại giao."),
    (42, "SP042", "Quần Âu Nam DUNA Màu Nâu Tây Dáng Tapered", 3, "Poly Rayon Spandex bề mặt mịn", "Tapered Fit", "img/sp-quan-au-nautay.jpg", 245000, 540000, "Tông màu nâu tây thời thượng, phần đùi thoải mái và ôm thon dần xuống cổ chân."),
    (43, "SP043", "Quần Âu Nam DUNA Cạp Chun Ẩn Thoải Mái Cả Ngày", 3, "Cotton pha Poly Spandex co giãn", "Regular Fit", "img/sp-quan-au-capchun.jpg", 230000, 510000, "Chun co giãn 2 bên sườn được giấu khéo léo bên trong cạp, vừa vặn mà lịch thiệp."),
    (44, "SP044", "Quần Âu Nam DUNA Dáng Cropped Trên Mắt Cá", 3, "Poly Rayon Spandex xước nhẹ", "Cropped Slim", "img/sp-quan-au-cropped.jpg", 225000, 500000, "Chiều dài lửng trên mắt cá chân hiện đại, phối cực đẹp cùng giày lười Loafer hoặc Sneaker trắng."),
    (45, "SP045", "Quần Âu Nam DUNA Màu Trắng Kem Dự Tiệc Sang Trọng", 3, "Tuytsi lót chống lộ dày dặn", "Slim Fit", "img/sp-quan-au-trangkem.jpg", 295000, 650000, "Màu trắng kem quý phái với lớp lót trong tinh tế, nổi bật trong các buổi tiệc tối và dạ hội."),

    # Category 4: Quần Khaki Nam (ID_DanhMuc = 4) - SP046 to SP054 (9 SP)
    (46, "SP046", "Quần Khaki Chinos Nam DUNA Slimfit Túi Xéo", 4, "98% Cotton Khaki chải kỹ, 2% Spandex", "Slim Fit", "img/sp-khaki-chinos.jpg", 205000, 450000, "Thiết kế túi xéo Chinos kinh điển, vải khaki chải kỹ bề mặt mịn không bám bụi."),
    (47, "SP047", "Quần Khaki Nam DUNA Màu Vàng Be Dạo Phố Trẻ Trung", 4, "100% Cotton twill dệt chéo", "Regular Fit", "img/sp-khaki-vangbe.jpg", 195000, 430000, "Tông màu be sáng năng động trẻ trung, dễ phối cùng áo thun và áo polo."),
    (48, "SP048", "Quần Khaki Nam DUNA Ống Suông Vải Dệt Chéo Dày Dặn", 4, "Cotton dệt chéo mật độ cao", "Straight Fit", "img/sp-khaki-ongsuong.jpg", 200000, 440000, "Ống suông rộng rãi phóng khoáng, chất vải dày dặn giữ form bền bỉ theo năm tháng."),
    (49, "SP049", "Quần Khaki Nam DUNA Màu Xanh Rêu Quân Đội Cá Tính", 4, "98% Cotton, 2% Spandex co giãn", "Slim Fit", "img/sp-khaki-xanhreu.jpg", 210000, 460000, "Sắc xanh rêu lính bụi bặm nam tính, phù hợp cho chàng trai yêu phong cách street casual."),
    (50, "SP050", "Quần Khaki Nam DUNA Cạp Co Giãn Chống Nhăn Mềm Mại", 4, "Cotton Spandex wash enzyme mềm", "Regular Fit", "img/sp-khaki-capchun.jpg", 215000, 470000, "Được xử lý giặt enzyme sinh học cho sợi vải siêu êm mềm ngay từ lần chạm đầu tiên."),
    (51, "SP051", "Quần Khaki Nam DUNA Màu Đen Tuyền Bền Màu Giặt Máy", 4, "Cotton pha Poly Spandex nhuộm bền màu", "Slim Fit", "img/sp-khaki-den.jpg", 195000, 430000, "Công nghệ nhuộm lưu huỳnh giữ màu đen tuyền lâu phai, giặt máy thoải mái không lo bạc màu."),
    (52, "SP052", "Quần Khaki Nam DUNA Màu Xám Tro Phối Đồ Hiện Đại", 4, "98% Cotton chải, 2% Elastane", "Slim Fit", "img/sp-khaki-xamtro.jpg", 205000, 450000, "Màu xám tro thanh lịch trung tính, phù hợp cả đi làm lẫn đi cà phê gặp gỡ bạn bè."),
    (53, "SP053", "Quần Khaki Chinos Nam DUNA Túi Hộp Nhẹ Phượt Dạo", 4, "Cotton Canvas mỏng nhẹ", "Relaxed Fit", "img/sp-khaki-tuihop.jpg", 220000, 480000, "Túi hộp sườn tiện lợi để điện thoại ví tiền, phong cách dã ngoại năng động."),
    (54, "SP054", "Quần Khaki Nam DUNA Vải Sợi Bông Tự Nhiên Thoáng Khí", 4, "100% Bông tự nhiên trồng sinh thái", "Regular Fit", "img/sp-khaki-soibong.jpg", 210000, 460000, "Thân thiện với làn da, thoáng khí và thấm mồ hôi tối đa trong ngày dài hoạt động."),

    # Category 5: Áo Blazer & Suit (ID_DanhMuc = 5) - SP055 to SP063 (9 SP)
    (55, "SP055", "Áo Blazer Nam DUNA Vải Linen Thoáng Mát Mùa Hè", 5, "100% Linen dệt thoáng khí, không lót đệm", "Unstructured Fit", "img/sp-blazer-linen.jpg", 530000, 1150000, "Áo khoác blazer đũi không đệm vai, siêu nhẹ và mát mẻ khi diện cùng áo thun mùa hè."),
    (56, "SP056", "Áo Blazer Nam Kẻ Ô Glen Plaid Phong Cách Anh Quốc DUNA", 5, "70% Wool pha Poly dệt kẻ ô", "Tailored Fit", "img/sp-blazer-glenplaid.jpg", 720000, 1550000, "Họa tiết kẻ Glen Plaid phong cách quý tộc Anh Quốc, ve áo cổ điển tinh tế."),
    (57, "SP057", "Áo Blazer Nam Màu Xanh Navy Đệm Vai Mỏng Tự Nhiên DUNA", 5, "Poly Rayon lót lụa cao cấp", "Modern Fit", "img/sp-blazer-navymong.jpg", 620000, 1350000, "Màu xanh navy chuẩn mực, đệm vai mỏng tạo đường nét bờ vai tự nhiên vững chãi."),
    (58, "SP058", "Áo Blazer Nam Màu Đen Ve Nhọn Single-Breasted DUNA", 5, "Wool Poly pha Spandex chống nhăn", "Slim Tailored", "img/sp-blazer-denvenhon.jpg", 650000, 1400000, "Ve nhọn quyền lực, khuy sừng sang trọng, hoàn hảo cho các buổi ký kết hợp đồng."),
    (59, "SP059", "Áo Blazer Nam Casual Dệt Tổ Ong Không Nhăn DUNA", 5, "Jersey Knit dệt kim co giãn 4 chiều", "Casual Fit", "img/sp-blazer-toong.jpg", 450000, 980000, "Chất liệu dệt kim không nhăn gập gọn bỏ balo công tác thoải mái, phong cách trẻ trung."),
    (60, "SP060", "Áo Blazer Nam Màu Nâu Camel Dạ Ép Mùa Đông DUNA", 5, "Dạ lông cừu ép mịn giữ ấm", "Classic Fit", "img/sp-blazer-nau-camel.jpg", 780000, 1650000, "Sắc nâu lạc đà Camel ấm áp thượng lưu, chất dạ ép giữ nhiệt tuyệt vời."),
    (61, "SP061", "Áo Khoác Suit Nam DUNA 2 Hàng Khuy Double-Breasted", 5, "Wool Blend chải kỹ bóng nhẹ", "Tailored Fit", "img/sp-suit-double.jpg", 740000, 1580000, "Kiểu dáng 6 khuy cài đúp phong cách Ý cổ điển, khẳng định đẳng cấp thủ lĩnh."),
    (62, "SP062", "Áo Blazer Nam Màu Xám Ghi Họa Tiết Houndstooth DUNA", 5, "Poly Wool dệt họa tiết nanh sói nhỏ", "Modern Fit", "img/sp-blazer-houndstooth.jpg", 690000, 1480000, "Họa tiết nanh sói Houndstooth cổ điển bất hủ, tạo điểm nhấn cá tính thanh lịch."),
    (63, "SP063", "Áo Blazer Nam DUNA Dáng Suông Phong Cách Minimalist", 5, "Cotton Rayon phủ bóng nhẹ", "Loose Relaxed", "img/sp-blazer-minimalist.jpg", 580000, 1250000, "Phom suông hiện đại xu hướng thời trang Hàn Quốc, kết hợp tuyệt vời với quần âu suông."),

    # Category 6: Phụ Kiện Nam (ID_DanhMuc = 6) - SP064 to SP070 (7 SP)
    (64, "SP064", "Ví Da Nam Bifold DUNA Da Bò Thật Sáp Ngựa Điên", 6, "100% Da bò sáp Crazy Horse cao cấp", "Bản gập đôi Bifold", "img/sp-vi-da.jpg", 175000, 390000, "Chất da sáp càng dùng càng bóng đẹp, nhiều ngăn đựng thẻ ATM và tiền mặt gọn gàng."),
    (65, "SP065", "Cà Vạt Lụa Nam DUNA Bản 7cm Họa Tiết Kẻ Chìm", 6, "100% Lụa tơ tằm Mulberry dệt jacquard", "Bản 7cm chuẩn hiện đại", "img/sp-cavat-lua.jpg", 95000, 220000, "Lụa dệt bóng mờ sang trọng, độ rủ chuẩn mực, không nhăn gãy khi thắt nút."),
    (66, "SP066", "Nơ Đeo Cổ Tuxedo Nam Lụa DUNA Lịch Lãm", 6, "Lụa bóng cao cấp may hai lớp", "Dây tăng giảm cổ", "img/sp-no-tuxedo.jpg", 65000, 150000, "Phụ kiện trang trọng cho tiệc cưới và sự kiện thảm đỏ, dây đeo tùy chỉnh tiện lợi."),
    (67, "SP067", "Set 5 Đôi Tất Khử Mùi Nano Bạc DUNA Cotton", 6, "85% Cotton chải, 15% Sợi Nano bạc", "Cổ trung qua mắt cá", "img/sp-set-tat.jpg", 50000, 120000, "Phủ hạt nano bạc kháng khuẩn khử mùi hôi chân hiệu quả, thun dệt êm ái không hằn da."),
    (68, "SP068", "Kẹp Cà Vạt Kim Loại Mạ Bạc Cao Cấp DUNA", 6, "Hợp kim mạ Rhodium chống oxy hóa", "Chiều dài 5.5cm", "img/sp-kep-cavat.jpg", 45000, 110000, "Thiết kế tối giản mạ bạc sáng bóng, lò xo kẹp chắc chắn giữ cà vạt ngay ngắn."),
    (69, "SP069", "Khuy Cài Măng Sét Áo Sơ Mi DUNA Cufflinks Tinh Tế", 6, "Đá mã não đen bọc viền mạ bạc", "Khuy xoay tiêu chuẩn", "img/sp-cufflinks.jpg", 80000, 180000, "Điểm nhấn quý ông ở cổ tay áo sơ mi, đá đen sang trọng thu hút ánh nhìn."),
    (70, "SP070", "Túi Đeo Chéo Da Nam DUNA Compact Đi Làm Dạo Phố", 6, "Da PU vân hạt cao cấp chống nước", "Kích thước 26x18x7cm", "img/sp-tui-cheo.jpg", 310000, 650000, "Thiết kế nhỏ gọn chứa vừa iPad mini, ví tiền, chìa khóa; chống thấm nước đi mưa nhẹ.")
]

# Generate SQL script
sql_lines = []
sql_lines.append("-- ============================================================================")
sql_lines.append("-- SEED DATA: 50 KHÁCH HÀNG & 70 SẢN PHẨM THỜI TRANG NAM DUNA")
sql_lines.append("-- CƠ SỞ DỮ LIỆU: QUANLYBANHANGQUANAONAMDUNA")
sql_lines.append("-- ============================================================================")
sql_lines.append("USE [QUANLYBANHANGQUANAONAMDUNA];")
sql_lines.append("GO\n")

# 1. Update Customers 1 to 6
sql_lines.append("-- 1. CẬP NHẬT 6 KHÁCH HÀNG BAN ĐẦU (ĐẢM BẢO CHUẨN THÔNG TIN)")
for c in customers_data[:6]:
    cid, ma, ten, sdt, email, gioitinh, ngaysinh, hang, diachi, tien = c
    sql_lines.append(f"UPDATE KhachHang SET MaKH = '{ma}', HoTen = N'{ten}', SoDienThoai = '{sdt}', Email = '{email}', GioiTinh = N'{gioitinh}', NgaySinh = '{ngaysinh}', HangKhachHang = N'{hang}', DiaChi = N'{diachi}', TrangThai = 'active', TongTienChiTieu = {tien}, NgayCapNhat = GETDATE() WHERE ID_KhachHang = {cid};")
sql_lines.append("GO\n")

# 2. Insert Customers 7 to 50
sql_lines.append("-- 2. THÊM 44 KHÁCH HÀNG MỚI (TỔNG CỘNG 50 KHÁCH HÀNG)")
sql_lines.append("SET IDENTITY_INSERT KhachHang ON;")
sql_lines.append("GO")

for c in customers_data[6:]:
    cid, ma, ten, sdt, email, gioitinh, ngaysinh, hang, diachi, tien = c
    sql_lines.append(f"IF NOT EXISTS (SELECT 1 FROM KhachHang WHERE ID_KhachHang = {cid})")
    sql_lines.append(f"    INSERT INTO KhachHang (ID_KhachHang, MaKH, HoTen, SoDienThoai, Email, GioiTinh, NgaySinh, HangKhachHang, DiaChi, TrangThai, NgayBatDauChuKy, NgayTao, NgayCapNhat, TongTienChiTieu) VALUES ({cid}, '{ma}', N'{ten}', '{sdt}', '{email}', N'{gioitinh}', '{ngaysinh}', N'{hang}', N'{diachi}', 'active', DATEADD(day, -30, GETDATE()), GETDATE(), GETDATE(), {tien});")
    sql_lines.append(f"ELSE")
    sql_lines.append(f"    UPDATE KhachHang SET MaKH = '{ma}', HoTen = N'{ten}', SoDienThoai = '{sdt}', Email = '{email}', GioiTinh = N'{gioitinh}', NgaySinh = '{ngaysinh}', HangKhachHang = N'{hang}', DiaChi = N'{diachi}', TrangThai = 'active', TongTienChiTieu = {tien}, NgayCapNhat = GETDATE() WHERE ID_KhachHang = {cid};")

sql_lines.append("SET IDENTITY_INSERT KhachHang OFF;")
sql_lines.append("GO\n")

# 3. Insert 64 new products (SP007 to SP070)
sql_lines.append("-- 3. THÊM 64 SẢN PHẨM MỚI (TỔNG CỘNG 70 SẢN PHẨM: SP001 -> SP070)")
sql_lines.append("SET IDENTITY_INSERT SanPham ON;")
sql_lines.append("GO")

for p in products_new:
    pid, ma, ten, cat_id, chatlieu, formdang, hinhanh, gianhap, giaban, mota = p
    sql_lines.append(f"IF NOT EXISTS (SELECT 1 FROM SanPham WHERE ID_SanPham = {pid})")
    sql_lines.append(f"    INSERT INTO SanPham (ID_SanPham, MaSP, TenSP, ID_DanhMuc, ThuongHieu, ChatLieu, FormDang, HinhAnh, GiaNhap, GiaBan, SoLuong, TongSoLuong, TyLeVat, TrangThai, MoTa, NgayTao, NgayCapNhat) VALUES ({pid}, '{ma}', N'{ten}', {cat_id}, N'DUNA', N'{chatlieu}', N'{formdang}', N'{hinhanh}', {gianhap}, {giaban}, 0, 0, 8, N'Còn hàng', N'{mota}', GETDATE(), GETDATE());")
    sql_lines.append(f"ELSE")
    sql_lines.append(f"    UPDATE SanPham SET MaSP = '{ma}', TenSP = N'{ten}', ID_DanhMuc = {cat_id}, ChatLieu = N'{chatlieu}', FormDang = N'{formdang}', HinhAnh = N'{hinhanh}', GiaNhap = {gianhap}, GiaBan = {giaban}, TrangThai = N'Còn hàng', MoTa = N'{mota}', NgayCapNhat = GETDATE() WHERE ID_SanPham = {pid};")

sql_lines.append("SET IDENTITY_INSERT SanPham OFF;")
sql_lines.append("GO\n")

# 4. Generate variants for SP007 to SP070
sql_lines.append("-- 4. THÊM BIẾN THỂ (SIZE / MÀU / TỒN KHO / BARCODE) CHO SẢN PHẨM TỪ SP007 ĐẾN SP070")
sql_lines.append("SET IDENTITY_INSERT BienTheSanPham ON;")
sql_lines.append("GO")

variant_id = 19
for p in products_new:
    pid, ma, ten, cat_id, chatlieu, formdang, hinhanh, gianhap, giaban, mota = p
    
    # Depending on category, choose sizes & colors
    if cat_id in (1, 2):  # Sơ mi & Polo: sizes S, M, L, XL
        v_configs = [
            ("Trắng", "S", "WHT", 20),
            ("Trắng", "M", "WHT", 30),
            ("Trắng", "L", "WHT", 25),
            ("Xanh Baby" if cat_id == 1 else "Xanh Navy", "M", "BLU" if cat_id == 1 else "NVY", 15),
        ]
    elif cat_id in (3, 4):  # Quần Âu & Khaki: sizes 29, 30, 31, 32
        v_configs = [
            ("Đen Than" if cat_id == 3 else "Vàng Khaki Cát", "29", "BLK" if cat_id == 3 else "KHK", 15),
            ("Đen Than" if cat_id == 3 else "Vàng Khaki Cát", "30", "BLK" if cat_id == 3 else "KHK", 25),
            ("Đen Than" if cat_id == 3 else "Vàng Khaki Cát", "31", "BLK" if cat_id == 3 else "KHK", 20),
            ("Xám Ghi" if cat_id == 3 else "Xanh Rêu", "30", "GRY" if cat_id == 3 else "OLV", 15),
        ]
    elif cat_id == 5:  # Blazer: sizes 48 (M), 50 (L), 52 (XL)
        v_configs = [
            ("Xanh Midnight Navy", "48 (M)", "NVY-48", 12),
            ("Xanh Midnight Navy", "50 (L)", "NVY-50", 15),
            ("Đen", "50 (L)", "BLK-50", 10),
        ]
    else:  # Phụ Kiện: Freesize
        v_configs = [
            ("Đen", "Freesize", "BLK-FS", 25),
            ("Nâu Cà Phê", "Freesize", "BRN-FS", 20),
        ]

    for color, size, code_suffix, qty in v_configs:
        v_code = f"BT{variant_id:03d}"
        barcode = f"{ma}-{code_suffix}" if "-" in code_suffix else f"{ma}-{code_suffix}-{size}"
        
        sql_lines.append(f"IF NOT EXISTS (SELECT 1 FROM BienTheSanPham WHERE ID_BienThe = {variant_id})")
        sql_lines.append(f"    INSERT INTO BienTheSanPham (ID_BienThe, MaBienThe, ID_SanPham, MauSac, TrongLuong, TrongLuongGram, TrongLuongU, CoCan, HinhAnhBienThe, GiaNhap, GiaBan, SoLuong, Barcode) VALUES ({variant_id}, '{v_code}', {pid}, N'{color}', '{size}', 250, 'g', '1', NULL, {gianhap}, {giaban}, {qty}, '{barcode}');")
        sql_lines.append(f"ELSE")
        sql_lines.append(f"    UPDATE BienTheSanPham SET MaBienThe = '{v_code}', ID_SanPham = {pid}, MauSac = N'{color}', TrongLuong = '{size}', GiaNhap = {gianhap}, GiaBan = {giaban}, SoLuong = {qty}, Barcode = '{barcode}' WHERE ID_BienThe = {variant_id};")
        variant_id += 1

sql_lines.append("SET IDENTITY_INSERT BienTheSanPham OFF;")
sql_lines.append("GO\n")

# 5. Sync stock in SanPham
sql_lines.append("-- 5. ĐỒNG BỘ TỔNG TỒN KHO CHO TOÀN BỘ SẢN PHẨM SANPHAM")
sql_lines.append("UPDATE SanPham SET")
sql_lines.append("    TongSoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE BienTheSanPham.ID_SanPham = SanPham.ID_SanPham), 50),")
sql_lines.append("    SoLuong = ISNULL((SELECT SUM(SoLuong) FROM BienTheSanPham WHERE BienTheSanPham.ID_SanPham = SanPham.ID_SanPham), 50);")
sql_lines.append("GO\n")

# Write out the sql file
sql_content = "\n".join(sql_lines)
with open("d:\\fpt\\DUNA\\seed_duna_50kh_70sp.sql", "w", encoding="utf-8") as f:
    f.write(sql_content)

print(f"Generated seed_duna_50kh_70sp.sql successfully! Total variants planned: {variant_id - 1}")
