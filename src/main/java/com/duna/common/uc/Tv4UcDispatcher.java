package com.duna.common.uc;

import com.duna.tv4_nguyen_kieu_an.dao.ReviewSupportDAO;
import com.duna.tv4_nguyen_kieu_an.service.FinanceReportService;
import com.duna.tv4_nguyen_kieu_an.service.HumanResourceService;
import com.duna.tv4_nguyen_kieu_an.service.MarketingService;
import com.duna.tv4_nguyen_kieu_an.service.NotificationService;
import com.duna.tv4_nguyen_kieu_an.service.RewardSupportService;
import com.duna.tv4_nguyen_kieu_an.service.StorePosService;
import com.duna.tv4_nguyen_kieu_an.service.SystemConfigService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.duna.common.uc.UcDispatchSupport.*;

/**
 * Dieu phoi day du UC219 - UC300 (Thanh vien 4: Nguyen Kieu An - TH08490).
 */
public final class Tv4UcDispatcher {
    private static final RewardSupportService rsSvc = new RewardSupportService();
    private static final ReviewSupportDAO revDao = new ReviewSupportDAO();
    private static final MarketingService mktSvc = new MarketingService();
    private static final NotificationService notiSvc = new NotificationService();
    private static final StorePosService posSvc = new StorePosService();
    private static final HumanResourceService hrSvc = new HumanResourceService();
    private static final FinanceReportService finSvc = new FinanceReportService();
    private static final SystemConfigService sysSvc = new SystemConfigService();

    private Tv4UcDispatcher() {
    }

    public static List<Map<String, Object>> execute(String uc, HttpServletRequest req) {
        String kw = str(req, "keyword", "");
        int uid = sessionUser(req);
        switch (uc) {
            case "UC219":
                return wrapAffected(uc, rsSvc.addReward(str(req, "rewardName", "Voucher DUNA 100K"), numInt(req, "pointsRequired", 200), null, numInt(req, "stockQuantity", 50)), "Đã thêm quà đổi điểm mới", "Không thể thêm quà");
            case "UC220":
                return wrapAffected(uc, rsSvc.redeemReward(numInt(req, "accountId", 3), numInt(req, "rewardId", 1), numInt(req, "pointsCost", 100)), "Đã đổi quà bằng điểm thưởng", "Điểm hoặc tồn quà không đủ");

            case "UC221":
                return rsSvc.listApprovedReviews(numInt(req, "productId", 1));
            case "UC222": {
                long rid = rsSvc.submitReview(numInt(req, "productId", 1), uid, null, numInt(req, "rating", 5), "Đánh giá DUNA", str(req, "content", "Vải mát, đứng form rất đẹp"), str(req, "fitFeedback", "TRUE_TO_SIZE"));
                return wrapAffected(uc, rid > 0 ? 1 : 0, "Đã gửi đánh giá sản phẩm #" + rid, "Không thể gửi đánh giá");
            }
            case "UC223":
                return wrapAffected(uc, rsSvc.addReviewMedia(numInt(req, "reviewId", 1), str(req, "mediaUrl", "img/duna-logo.png")), "Đã tải ảnh lên đánh giá", "Không thể thêm ảnh đánh giá");
            case "UC224":
                return rsSvc.listPendingReviews(kw);
            case "UC225":
                return wrapAffected(uc, rsSvc.approveReview(numInt(req, "reviewId", 1)), "Đã duyệt đánh giá hiển thị công khai", "Không tìm thấy đánh giá");
            case "UC226":
                return wrapAffected(uc, revDao.uc226_rejectReview(numInt(req, "reviewId", 999)), "Đã từ chối đánh giá vi phạm", "Không tìm thấy đánh giá");
            case "UC227":
                return wrapAffected(uc, rsSvc.replyReview(numInt(req, "reviewId", 1), uid, str(req, "content", "DUNA cảm ơn quý khách đã tin tưởng!")), "Đã gửi phản hồi của Shop", "Không thể phản hồi đánh giá");
            case "UC228":
                return rsSvc.getReviewSummary();

            case "UC229": {
                String code = "TK-" + (System.currentTimeMillis() % 1000000L);
                String subj = "[" + str(req, "category", "SIZE_ADVICE") + "] " + str(req, "subject", "Hỗ trợ chọn size sơ mi");
                long tid = rsSvc.createTicket(code, numInt(req, "customerId", 1), numLong(req, "orderId", 1L), null, subj, str(req, "priority", "MEDIUM"), uid, "Khách hàng gửi yêu cầu hỗ trợ từ hệ thống DUNA.");
                return wrapAffected(uc, tid > 0 ? 1 : 0, "Đã tạo ticket hỗ trợ " + code, "Không thể tạo ticket");
            }
            case "UC230":
                return rsSvc.listTickets(kw);
            case "UC231":
                return wrapAffected(uc, rsSvc.assignTicket(numInt(req, "ticketId", 1), numInt(req, "staffUserId", 3)), "Đã phân công nhân viên xử lý ticket", "Không tìm thấy ticket hoặc nhân viên");
            case "UC232":
                return wrapAffected(uc, rsSvc.addTicketMessage(numInt(req, "ticketId", 1), uid, str(req, "content", "DUNA đã tiếp nhận và tư vấn size phù hợp cho anh."), true), "Đã gửi tin nhắn vào hội thoại ticket", "Không thể gửi tin nhắn");
            case "UC233":
                return rsSvc.listTicketMessages(numInt(req, "ticketId", 1), kw);
            case "UC234":
                return wrapAffected(uc, rsSvc.closeTicket(numInt(req, "ticketId", 999)), "Đã đóng yêu cầu hỗ trợ", "Không tìm thấy ticket");
            case "UC235":
                return rsSvc.listFaqs(kw);
            case "UC236":
                return wrapAffected(uc, rsSvc.addFaq(str(req, "faqGroup", "Đổi trả"), str(req, "question", "DUNA hỗ trợ đổi size trong bao lâu?"), str(req, "answer", "Hỗ trợ đổi size trong 15 ngày trên toàn hệ thống."), 1), "Đã thêm câu hỏi thường gặp FAQ", "Không thể thêm FAQ");

            case "UC237":
                return mktSvc.listBanners(kw);
            case "UC238":
                return wrapAffected(uc, mktSvc.addBanner(str(req, "title", "BST Công Sở DUNA 2026"), str(req, "imageUrl", "img/duna-logo.png"), str(req, "linkUrl", "/ProductServlet"), str(req, "position", "HOME_HERO"), 1, str(req, "startDate", "2026-11-01"), str(req, "endDate", "2027-03-31")), "Đã thêm mới Banner quảng cáo", "Không thể thêm Banner");
            case "UC239":
                return wrapAffected(uc, mktSvc.updateBanner(numInt(req, "bannerId", 1), numInt(req, "displayOrder", 1), str(req, "endDate", "2027-06-30")), "Đã cập nhật thứ tự & thời hạn Banner", "Không tìm thấy Banner");
            case "UC240":
                return wrapAffected(uc, mktSvc.hideExpiredBanners(), "Đã tự động ẩn các Banner hết hạn", "Không có Banner hết hạn");
            case "UC241":
                return mktSvc.listBlogPosts(kw);
            case "UC242":
                return wrapAffected(uc, mktSvc.createDraftPost(null, str(req, "title", "Cách phối Áo Polo & Quần Âu"), str(req, "slug", "cach-phoi-ao-polo-quan-au"), str(req, "summary", "Gợi ý phong cách lịch lãm mỗi ngày"), str(req, "content", "Chọn màu trung tính kết hợp thắt lưng da."), "img/duna-logo.png", uid), "Đã lưu bài viết nháp", "Slug bài viết đã tồn tại");
            case "UC243":
                return wrapAffected(uc, mktSvc.publishPost(numInt(req, "postId", 1)), "Đã xuất bản bài viết lên website", "Không tìm thấy bài viết");
            case "UC244":
                return wrapMap(mktSvc.viewPost(str(req, "slug", "cach-phoi-ao-polo-quan-au")), uc, "Không tìm thấy bài viết theo slug");
            case "UC245":
                return wrapAffected(uc, mktSvc.addBlogCategory(str(req, "name", "Phối đồ công sở"), str(req, "slug", "phoi-do-cong-so")), "Đã thêm chuyên mục bài viết", "Slug chuyên mục đã tồn tại");
            case "UC246":
                return wrapAffected(uc, mktSvc.subscribeNewsletter(str(req, "email", "subscriber@duna.vn")), "Đã đăng ký nhận bản tin DUNA", "Email đã đăng ký trước đó");

            case "UC247":
                return notiSvc.listTemplates(str(req, "channel", ""), kw);
            case "UC248":
                return wrapAffected(uc, notiSvc.addTemplate(str(req, "templateCode", "ORDER_CONFIRMED_V2"), str(req, "channel", "EMAIL"), str(req, "subject", "DUNA xác nhận đơn hàng"), str(req, "body", "Cảm ơn quý khách đã mua sắm tại DUNA.")), "Đã thêm mới mẫu thông báo", "Mã mẫu đã tồn tại");
            case "UC249":
                return wrapAffected(uc, notiSvc.updateTemplate(numInt(req, "templateId", 1), str(req, "subject", "DUNA - Cập nhật đơn hàng"), str(req, "body", "Đơn hàng của bạn đang được xử lý.")), "Đã cập nhật mẫu thông báo", "Không tìm thấy mẫu thông báo");
            case "UC250":
                return wrapAffected(uc, notiSvc.notifyOrderStatus(numLong(req, "orderId", 1L), numInt(req, "templateId", 1), str(req, "title", "Đơn hàng đã xác nhận")), "Đã tạo thông báo đổi trạng thái đơn hàng", "Không thể gửi thông báo đơn hàng");
            case "UC251":
                return notiSvc.listUnread(numInt(req, "userId", 1), kw);
            case "UC252":
                return wrapAffected(uc, notiSvc.markRead(uid, numLong(req, "notificationId", 1L)), "Đã đánh dấu thông báo đã đọc", "Không tìm thấy thông báo");
            case "UC253":
                return wrapAffected(uc, notiSvc.purgeOldNotifications(), "Đã dọn dẹp thông báo cũ hơn 90 ngày", "Không có thông báo quá hạn 90 ngày");
            case "UC254":
                return wrapAffected(uc, notiSvc.createEmailCampaign(str(req, "campaignName", "Tri ân Hội viên VIP Tháng 11"), numInt(req, "templateId", 1), numInt(req, "targetGroupId", 1), uid), "Đã tạo chiến dịch Email Marketing", "Không thể tạo chiến dịch");
            case "UC255":
                return wrapAffected(uc, notiSvc.scheduleAndSendCampaign(numInt(req, "campaignId", 1), str(req, "scheduledAt", "2026-11-20 09:00")), "Đã lên lịch & phát hành chiến dịch Email", "Không tìm thấy chiến dịch");
            case "UC256":
                return notiSvc.listFailedMessages(kw);

            case "UC257":
                return posSvc.listStores(kw);
            case "UC258":
                return wrapAffected(uc, posSvc.addStore(str(req, "storeCode", "DUNA-HP01"), str(req, "storeName", "DUNA Hải Phòng"), str(req, "address", "Lê Lợi, Hải Phòng"), 1, str(req, "phone", "02253888999"), "08:30", "22:00", 1), "Đã thêm cửa hàng bán lẻ mới", "Mã cửa hàng đã tồn tại");
            case "UC259":
                return wrapAffected(uc, posSvc.updateStore(numInt(req, "storeId", 1), str(req, "openTime", "08:30"), str(req, "closeTime", "22:00"), numInt(req, "warehouseId", 1)), "Đã cập nhật giờ mở cửa & kho xuất", "Không tìm thấy cửa hàng");
            case "UC260": {
                long sid = posSvc.openSession(numInt(req, "storeId", 1), uid, numDouble(req, "openingCash", 2000000));
                return wrapAffected(uc, sid > 0 ? 1 : 0, "Đã mở ca bán hàng POS #" + sid, "Không thể mở ca bán hàng");
            }
            case "UC261":
                return posSvc.scanBarcode(kw, numInt(req, "warehouseId", 1));
            case "UC262": {
                String code = "POS-" + (System.currentTimeMillis() % 1000000L);
                long oid = posSvc.createPosOrder(code, 1, numInt(req, "storeId", 1), numInt(req, "warehouseId", 1), uid, numInt(req, "sessionId", 1), numInt(req, "variantId", 1), numInt(req, "quantity", 1));
                return wrapAffected(uc, oid > 0 ? 1 : 0, "Đã xuất đơn tại quầy " + code, "Tồn kho không đủ hoặc lỗi ca POS");
            }
            case "UC263":
                return wrapAffected(uc, posSvc.recordCashTxn(numInt(req, "posSessionId", 1), str(req, "txnType", "OUT"), numDouble(req, "amount", 50000), str(req, "note", "Mua túi giấy gói hàng")), "Đã ghi nhận thu/chi két tiền mặt", "Không tìm thấy ca POS đang mở");
            case "UC264":
                return wrapAffected(uc, posSvc.closeSession(numInt(req, "sessionId", 999), numDouble(req, "closingCash", 2000000)), "Đã đóng ca & chốt quỹ két tiền mặt", "Không tìm thấy ca POS đang mở");
            case "UC265":
                return posSvc.reportBySession(numInt(req, "storeId", 0));
            case "UC266":
                return posSvc.listCashierSessions(numInt(req, "cashierId", 0), str(req, "statusFilter", ""));

            case "UC267":
                return hrSvc.listEmployees(kw);
            case "UC268":
                return wrapAffected(uc, hrSvc.addEmployee(str(req, "employeeCode", "NV-TEST01"), null, str(req, "fullName", "Phạm Minh Tuấn"), str(req, "phone", "0977665544"), str(req, "email", "tuan.nv@duna.vn"), str(req, "birthDate", "1999-04-12"), "2026-11-17", 1, 1, numInt(req, "storeId", 1)), "Đã thêm mới hồ sơ nhân viên", "Mã nhân viên đã tồn tại");
            case "UC269":
                return wrapAffected(uc, hrSvc.updatePositionAndStore(numInt(req, "employeeId", 1), numInt(req, "positionId", 1), numInt(req, "storeId", 1)), "Đã cập nhật chức vụ & điều chuyển cửa hàng", "Không tìm thấy nhân viên");
            case "UC270":
                return wrapAffected(uc, hrSvc.resignEmployee(numInt(req, "employeeId", 999)), "Đã cho nhân viên nghỉ việc & khóa tài khoản", "Không tìm thấy nhân viên");
            case "UC271":
                return hrSvc.listDepartmentsAndPositions(kw);
            case "UC272":
                return wrapAffected(uc, hrSvc.addPosition(str(req, "positionName", "Trưởng ca POS"), numDouble(req, "baseSalary", 10500000)), "Đã thêm mới chức vụ & mức lương", "Tên chức vụ đã tồn tại");
            case "UC273":
                return wrapAffected(uc, hrSvc.addWorkShift(str(req, "shiftName", "Ca Tối Cuối Tuần"), str(req, "startTime", "18:00"), str(req, "endTime", "22:00")), "Đã thêm ca làm việc mới", "Tên ca đã tồn tại");
            case "UC274":
                return wrapAffected(uc, hrSvc.assignShift(numInt(req, "employeeId", 1), numInt(req, "shiftId", 1), numInt(req, "storeId", 1), str(req, "workDate", "2026-11-18")), "Đã phân ca làm việc cho nhân viên", "Ca làm việc bị trùng hoặc lỗi");
            case "UC275":
                return hrSvc.getWeeklySchedule(numInt(req, "storeId", 0), str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"));
            case "UC276":
                return wrapAffected(uc, hrSvc.checkIn(numInt(req, "employeeId", 1), str(req, "workDate", "2026-11-17")), "Đã chấm công vào ca (Check-in)", "Không tìm thấy phân ca hoặc đã chấm công");
            case "UC277":
                return wrapAffected(uc, hrSvc.checkOut(numInt(req, "employeeId", 1), str(req, "workDate", "2026-11-17")), "Đã chấm công ra ca (Check-out)", "Chưa check-in hoặc không tìm thấy ca");
            case "UC278":
                return hrSvc.monthlyAttendance(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"));

            case "UC279":
                return finSvc.listExpenses(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"), kw);
            case "UC280":
                return wrapAffected(uc, finSvc.addExpenseCategory(str(req, "categoryName", "Chi phí quảng cáo TikTok")), "Đã thêm loại chi phí vận hành", "Tên loại chi phí đã tồn tại");
            case "UC281":
                return wrapAffected(uc, finSvc.addExpense(numInt(req, "categoryId", 1), numInt(req, "storeId", 1), numDouble(req, "amount", 1500000), str(req, "expenseDate", "2026-11-17"), str(req, "description", "Chi phí điện nước cửa hàng"), uid), "Đã ghi nhận khoản chi phí phát sinh", "Không thể ghi nhận chi phí");
            case "UC282":
                return wrapAffected(uc, finSvc.updateExpense(numInt(req, "expenseId", 1), numDouble(req, "amount", 1500000), str(req, "description", "Đã điều chỉnh chứng từ")), "Đã cập nhật chứng từ chi phí", "Không tìm thấy khoản chi phí");
            case "UC283":
                return wrapAffected(uc, finSvc.deleteExpense(numInt(req, "expenseId", 9999)), "Đã xóa khoản chi phí nhập sai", "Không tìm thấy khoản chi phí");
            case "UC284":
                return wrapAffected(uc, finSvc.summarizeYesterday(), "Đã tổng hợp số liệu doanh thu ngày hôm trước", "Đã tổng hợp trước đó hoặc không có đơn mới");
            case "UC285":
                return finSvc.listDailySummaries(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"));
            case "UC286":
                return finSvc.monthlyRevenue(numInt(req, "year", 2026));
            case "UC287":
                return finSvc.revenueByCategory();
            case "UC288":
                return finSvc.revenueByChannelAndStore();
            case "UC289":
                return finSvc.topProductsByRevenue(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"));
            case "UC290":
                return finSvc.grossProfitByProduct();
            case "UC291":
                return finSvc.inventoryValuation();
            case "UC292":
                return finSvc.topVipCustomers();
            case "UC293":
                return finSvc.cancellationReturnRate(numInt(req, "year", 2026));
            case "UC294":
                return finSvc.newCustomersByMonth();
            case "UC295":
                return wrapMap(finSvc.monthlyNetProfit(str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31")), uc, "Chưa có dữ liệu lợi nhuận ròng trong kỳ");
            case "UC296":
                return wrapMap(finSvc.getDashboard(), uc, "Chưa có số liệu Dashboard điều hành");

            case "UC297":
                return sysSvc.listSettings(kw);
            case "UC298":
                return wrapAffected(uc, sysSvc.updateSetting(str(req, "settingKey", "VAT_RATE"), str(req, "settingValue", "8")), "Đã cập nhật cấu hình hệ thống", "Không tìm thấy khóa cấu hình");
            case "UC299":
                return sysSvc.listScheduledJobs(kw);
            case "UC300":
                return wrapAffected(uc, sysSvc.updateScheduledJob(str(req, "jobName", "DAILY_REVENUE_JOB"), bool(req, "enabled", true), str(req, "cronExpression", "0 0 2 * * ?")), "Đã cập nhật lịch chạy công việc ngầm", "Không tìm thấy Scheduled Job");
            default:
                return wrapScalar(uc, "UNMAPPED", "Chưa ánh xạ Use Case TV4: " + uc);
        }
    }
}
