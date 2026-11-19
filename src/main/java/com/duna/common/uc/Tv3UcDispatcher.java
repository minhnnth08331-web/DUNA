package com.duna.common.uc;

import com.duna.tv3_le_minh_bach.dao.PaymentDAO;
import com.duna.tv3_le_minh_bach.service.LoyaltyEarnService;
import com.duna.tv3_le_minh_bach.service.PaymentRefundService;
import com.duna.tv3_le_minh_bach.service.PromotionService;
import com.duna.tv3_le_minh_bach.service.ShippingGeoService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.duna.common.uc.UcDispatchSupport.*;

/**
 * Dieu phoi day du UC152 - UC218 (Thanh vien 3: Le Minh Bach - TH08442).
 */
public final class Tv3UcDispatcher {
    private static final PaymentRefundService paySvc = new PaymentRefundService();
    private static final ShippingGeoService shipSvc = new ShippingGeoService();
    private static final PromotionService promoSvc = new PromotionService();
    private static final LoyaltyEarnService loySvc = new LoyaltyEarnService();
    private static final PaymentDAO payDao = new PaymentDAO();

    private Tv3UcDispatcher() {
    }

    public static List<Map<String, Object>> execute(String uc, HttpServletRequest req) {
        String kw = str(req, "keyword", "");
        int uid = sessionUser(req);
        switch (uc) {
            case "UC152": {
                long id = paySvc.createRefund(numLong(req, "orderId", 7L), null, null, numDouble(req, "amount", 390000), str(req, "method", "BANK"));
                return wrapAffected(uc, id > 0 ? 1 : 0, "Đã tạo phiếu hoàn tiền #" + id, "Không thể tạo phiếu hoàn tiền");
            }
            case "UC153":
                return wrapAffected(uc, paySvc.completeRefund(numInt(req, "refundId", 1)), "Đã hoàn tất hoàn tiền", "Không tìm thấy phiếu hoàn tiền đang chờ");
            case "UC154":
                return wrapAffected(uc, paySvc.processSizeExchange(numInt(req, "returnId", 1), numInt(req, "warehouseId", 1)), "Đã xử lý đổi size & đồng bộ tồn kho", "Chưa đủ điều kiện đổi size");
            case "UC155":
                return wrapAffected(uc, paySvc.autoCancelUnpaidOrders(), "Đã quét & hủy đơn online quá hạn thanh toán", "Không có đơn online quá hạn");
            case "UC156":
                return wrapAffected(uc, paySvc.autoCompleteDeliveredOrders(), "Đã hoàn tất đơn đã giao quá 7 ngày", "Không có đơn cần chốt tự động");
            case "UC157":
                return paySvc.getOrderStatsByStatus();
            case "UC158":
                return paySvc.getOrdersForExport(safeDate(req, "fromDate", "2026-01-01"), safeDate(req, "toDate", "2027-12-31"));
            case "UC159":
                return paySvc.listPaymentMethods(kw);
            case "UC160":
                return wrapAffected(uc, paySvc.addPaymentMethod(str(req, "methodCode", "VIETQR"), str(req, "methodName", "Chuyển khoản VietQR Napas"), bool(req, "isOnline", true), numInt(req, "displayOrder", 4)), "Đã thêm phương thức thanh toán", "Mã phương thức đã tồn tại");
            case "UC161":
                return wrapAffected(uc, paySvc.togglePaymentMethod(numInt(req, "methodId", 3)), "Đã bật/tắt phương thức thanh toán", "Không tìm thấy phương thức thanh toán");
            case "UC162": {
                long pid = paySvc.createCodPayment(numLong(req, "orderId", 7L), numDouble(req, "amount", 500000));
                return wrapAffected(uc, pid > 0 ? 1 : 0, "Đã tạo bản ghi thanh toán COD #" + pid, "Không thể tạo bản ghi COD");
            }
            case "UC163": {
                long pid = paySvc.createOnlinePayment(numLong(req, "orderId", 7L), "VNPAY", numDouble(req, "amount", 500000), "VNP-" + System.currentTimeMillis());
                return wrapAffected(uc, pid > 0 ? 1 : 0, "Đã tạo yêu cầu thanh toán VNPay #" + pid, "Không thể tạo giao dịch VNPay");
            }
            case "UC164":
                return wrapAffected(uc, paySvc.handleIpnSuccess(str(req, "txnRef", "TXN-DN20261106"), str(req, "gateway", "VNPAY"), str(req, "gatewayTxnNo", "VNP999888"), numDouble(req, "amount", 500000)), "Đã xử lý callback IPN thành công", "Không tìm thấy giao dịch khớp txnRef");
            case "UC165":
                return wrapAffected(uc, paySvc.markPaymentFailed(numLong(req, "paymentId", 1L), str(req, "gateway", "VNPAY"), str(req, "responseCode", "24"), numDouble(req, "amount", 500000)), "Đã đánh dấu giao dịch thất bại", "Không tìm thấy giao dịch thanh toán");
            case "UC166": {
                long pid = payDao.uc166_createMomoPayment(numLong(req, "orderId", 7L), numDouble(req, "amount", 500000), "MOMO-" + System.currentTimeMillis());
                return wrapAffected(uc, pid > 0 ? 1 : 0, "Đã tạo yêu cầu ví MoMo #" + pid, "Không thể tạo giao dịch MoMo");
            }
            case "UC167":
                return paySvc.listTransactions(numLong(req, "orderId", 0L), kw);
            case "UC168":
                return wrapAffected(uc, paySvc.createCodReconciliation(numInt(req, "carrierId", 1), safeDate(req, "periodFrom", "2026-11-01"), safeDate(req, "periodTo", "2026-11-30"), uid), "Đã tạo phiên đối soát COD", "Không thể tạo phiên đối soát");
            case "UC169":
                return wrapAffected(uc, paySvc.completeCodReconciliation(numInt(req, "reconciliationId", 1)), "Đã xác nhận hoàn tất đối soát COD", "Không tìm thấy phiên đối soát");
            case "UC170":
                return wrapAffected(uc, paySvc.refundOnlinePayment(numLong(req, "paymentId", 1L), str(req, "gateway", "VNPAY"), "REF-" + System.currentTimeMillis(), numDouble(req, "amount", 100000)), "Đã hoàn tiền qua cổng thanh toán", "Không tìm thấy giao dịch đã thanh toán");

            case "UC171":
                return shipSvc.listProvinces(kw);
            case "UC172":
                return wrapAffected(uc, shipSvc.addProvince(str(req, "provinceCode", "99"), str(req, "provinceName", "Khu đô thị mở rộng"), numInt(req, "zoneId", 1)), "Đã thêm tỉnh/thành mới", "Mã tỉnh đã tồn tại");
            case "UC173":
                return wrapAffected(uc, shipSvc.assignZone(numInt(req, "provinceId", 1), numInt(req, "zoneId", 1)), "Đã gán vùng giao hàng cho tỉnh", "Không tìm thấy tỉnh/thành");
            case "UC174":
                return shipSvc.listWards(numInt(req, "provinceId", 1), kw);
            case "UC175":
                return shipSvc.searchWards(kw);
            case "UC176":
                return wrapAffected(uc, shipSvc.addWard(numInt(req, "provinceId", 1), str(req, "wardCode", "00999"), str(req, "wardName", "Phường Mới"), str(req, "wardType", "WARD")), "Đã thêm phường/xã/đặc khu mới", "Mã phường/xã đã tồn tại");
            case "UC177":
                return wrapAffected(uc, shipSvc.deleteWard(numInt(req, "wardId", 99999)), "Đã xóa phường/xã chưa phát sinh giao dịch", "Phường/xã đang được tham chiếu hoặc không tồn tại");
            case "UC178":
                return shipSvc.listZones(kw);
            case "UC179":
                return wrapAffected(uc, shipSvc.addZone(str(req, "zoneName", "Vùng Đặc Khu"), str(req, "description", "Khu vực hải đảo & đặc khu")), "Đã thêm vùng giao hàng mới", "Tên vùng đã tồn tại");
            case "UC180":
                return shipSvc.listCarriers(kw);
            case "UC181":
                return wrapAffected(uc, shipSvc.addCarrier(str(req, "carrierCode", "JNT"), str(req, "carrierName", "J&T Express"), str(req, "apiEndpoint", "https://api.jtexpress.vn"), str(req, "phone", "19001088")), "Đã thêm đơn vị vận chuyển mới", "Mã ĐVVC đã tồn tại");
            case "UC182":
                return shipSvc.listMethods(kw);
            case "UC183":
                return wrapAffected(uc, shipSvc.addMethod(numInt(req, "carrierId", 1), str(req, "methodName", "Giao hỏa tốc 4h"), numInt(req, "minDays", 0), numInt(req, "maxDays", 1)), "Đã thêm dịch vụ vận chuyển mới", "Không thể thêm dịch vụ");
            case "UC184":
                return shipSvc.listFees();
            case "UC185":
                return wrapAffected(uc, shipSvc.updateFee(numInt(req, "feeId", 1), numDouble(req, "baseFee", 25000), numDouble(req, "freeShipThreshold", 500000)), "Đã cập nhật biểu phí vận chuyển", "Không tìm thấy dòng biểu phí");
            case "UC186": {
                double fee = shipSvc.calculateFee(numInt(req, "provinceId", 1), numInt(req, "shippingMethodId", 1), numInt(req, "weightGram", 500), numDouble(req, "orderAmount", 390000));
                return wrapScalar(uc, fee, "Phí vận chuyển dự kiến (VNĐ)");
            }
            case "UC187":
                return wrapAffected(uc, shipSvc.createShipment(numLong(req, "orderId", 7L), numInt(req, "carrierId", 1), numInt(req, "shippingMethodId", 1), str(req, "trackingCode", "VD-DN202601"), numDouble(req, "shippingFee", 25000), numDouble(req, "codAmount", 0), numInt(req, "weightGram", 500)), "Đã tạo vận đơn gửi ĐVVC", "Mã vận đơn đã tồn tại hoặc lỗi dữ liệu");
            case "UC188":
                return wrapAffected(uc, shipSvc.updateTracking(str(req, "trackingCode", "VD-DN202601"), str(req, "newStatus", "IN_TRANSIT"), str(req, "location", "Kho trung chuyển Hà Nội"), str(req, "note", "Đang giao")), "Đã cập nhật hành trình vận đơn", "Không tìm thấy mã vận đơn");
            case "UC189":
                return shipSvc.trackShipment(str(req, "trackingCode", "VD"));
            case "UC190":
                return shipSvc.listDelayedOrFailed(kw);

            case "UC191":
                return promoSvc.listVouchers(kw);
            case "UC192":
                return wrapAffected(uc, promoSvc.createVoucher(str(req, "voucherCode", "DUNA15"), str(req, "voucherName", "Ưu đãi DUNA 15%"), str(req, "discountType", "PERCENT"), numDouble(req, "discountValue", 15), numDouble(req, "maxDiscountAmount", 200000), numDouble(req, "minOrderValue", 500000), numInt(req, "usageLimit", 100), numInt(req, "perUserLimit", 1), safeDate(req, "startDate", "2026-01-01"), safeDate(req, "endDate", "2027-06-30")), "Đã thêm mới Voucher", "Mã Voucher đã tồn tại");
            case "UC193":
                return wrapAffected(uc, promoSvc.updateVoucher(numInt(req, "voucherId", 1), numDouble(req, "minOrderValue", 500000), safeDate(req, "endDate", "2027-06-30"), numInt(req, "usageLimit", 100)), "Đã cập nhật điều kiện Voucher", "Không tìm thấy Voucher");
            case "UC194":
                return wrapAffected(uc, promoSvc.toggleVoucher(numInt(req, "voucherId", 1)), "Đã bật/tắt trạng thái Voucher", "Không tìm thấy Voucher");
            case "UC195":
                return wrapAffected(uc, promoSvc.deleteVoucher(numInt(req, "voucherId", 999)), "Đã xóa Voucher chưa có lượt dùng", "Voucher đã sử dụng hoặc không tồn tại");
            case "UC196":
                return wrapMap(promoSvc.validateVoucher(str(req, "keyword", "DUNA100"), numDouble(req, "orderAmount", 600000), uid), uc, "Voucher không hợp lệ hoặc chưa đạt ngưỡng tối thiểu");
            case "UC197":
                return wrapAffected(uc, promoSvc.recordVoucherUsage(numInt(req, "voucherId", 1), numInt(req, "userId", 4), numLong(req, "orderId", 7L), numDouble(req, "discountAmount", 100000)), "Đã ghi nhận lượt sử dụng Voucher", "Không thể ghi nhận lượt sử dụng");
            case "UC198":
                return promoSvc.getVoucherAnalytics();
            case "UC199":
                return promoSvc.listPromotions(kw);
            case "UC200":
                return wrapAffected(uc, promoSvc.createPromotion(str(req, "promotionName", "DUNA Smart Casual Week"), str(req, "promoType", "PERCENT"), numDouble(req, "discountValue", 15), safeDate(req, "startDate", "2026-11-01"), safeDate(req, "endDate", "2027-01-31"), "img/duna-logo.png"), "Đã tạo chương trình khuyến mãi", "Không thể tạo chương trình");
            case "UC201":
                return wrapAffected(uc, promoSvc.assignProduct(numInt(req, "promotionId", 1), numInt(req, "productId", 1)), "Đã gán sản phẩm vào chương trình KM", "Sản phẩm đã có trong chương trình");
            case "UC202":
                return wrapAffected(uc, promoSvc.removeProduct(numInt(req, "promotionId", 1), numInt(req, "productId", 999)), "Đã gỡ sản phẩm khỏi chương trình KM", "Không tìm thấy liên kết KM");
            case "UC203":
                return wrapMap(promoSvc.getPromoPrice(numInt(req, "productId", 1)), uc, "Không tìm thấy sản phẩm");
            case "UC204":
                return wrapAffected(uc, promoSvc.endPromotionEarly(numInt(req, "promotionId", 999)), "Đã kết thúc sớm chương trình KM", "Không tìm thấy chương trình KM");
            case "UC205":
                return promoSvc.listFlashSales(kw);
            case "UC206":
                return wrapAffected(uc, promoSvc.createFlashSale(str(req, "title", "Flash Sale Cuối Tuần DUNA"), str(req, "startTime", "2026-11-27 20:00"), str(req, "endTime", "2026-11-27 22:00")), "Đã tạo đợt Flash Sale mới", "Không thể tạo đợt Flash Sale");
            case "UC207":
                return wrapAffected(uc, promoSvc.addFlashSaleItem(numInt(req, "flashSaleId", 1), numInt(req, "variantId", 1), numDouble(req, "salePrice", 299000), numInt(req, "qtyLimit", 20)), "Đã thêm biến thể vào Flash Sale", "Biến thể đã có trong Flash Sale");
            case "UC208":
                return wrapAffected(uc, promoSvc.syncFlashSaleStatuses(), "Đã đồng bộ trạng thái Flash Sale theo khung giờ", "Không có đợt Flash Sale nào cần đổi trạng thái");
            case "UC209":
                return wrapAffected(uc, promoSvc.incrementFlashSaleSold(numInt(req, "flashItemId", 1)), "Đã ghi nhận lượt bán Flash Sale", "Đã hết suất Flash Sale hoặc ID không hợp lệ");
            case "UC210":
                return promoSvc.listRunningFlashItems();

            case "UC211":
                return wrapAffected(uc, loySvc.createAccount(numInt(req, "customerId", 1)), "Đã khởi tạo tài khoản tích điểm", "Khách hàng đã có tài khoản tích điểm");
            case "UC212":
                return wrapMap(loySvc.getBalance(numInt(req, "customerId", 2)), uc, "Không tìm thấy ví điểm của khách hàng");
            case "UC213":
                return wrapAffected(uc, loySvc.earnPoints(numInt(req, "accountId", 1), numLong(req, "orderId", 7L), numInt(req, "points", 50), str(req, "description", "Tích điểm đơn hoàn tất")), "Đã cộng điểm thưởng", "Không tìm thấy ví điểm");
            case "UC214":
                return wrapAffected(uc, loySvc.redeemPoints(numInt(req, "accountId", 2), numLong(req, "orderId", 7L), numInt(req, "points", 10), str(req, "description", "Đổi điểm giảm giá đơn")), "Đã trừ điểm thanh toán đơn", "Số dư điểm không đủ");
            case "UC215":
                return wrapAffected(uc, loySvc.adjustPoints(numInt(req, "accountId", 1), numInt(req, "deltaPoints", 20), str(req, "reason", "Thưởng sinh nhật hội viên")), "Đã điều chỉnh điểm thủ công", "Không tìm thấy ví điểm");
            case "UC216":
                return loySvc.listTransactions(numInt(req, "customerId", 0), str(req, "txnType", ""));
            case "UC217":
                return wrapAffected(uc, loySvc.expireOldPoints(), "Đã hết hạn điểm quá 12 tháng", "Không có điểm thưởng quá hạn");
            case "UC218":
                return loySvc.listRewards(kw);
            default:
                return wrapScalar(uc, "UNMAPPED", "Chưa ánh xạ Use Case TV3: " + uc);
        }
    }
}
