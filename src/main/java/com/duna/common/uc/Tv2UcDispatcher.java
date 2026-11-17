package com.duna.common.uc;

import com.duna.tv2_nguyen_duc_anh.service.CartWishlistService;
import com.duna.tv2_nguyen_duc_anh.service.InventoryProcurementService;
import com.duna.tv2_nguyen_duc_anh.service.OrderReturnService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.duna.common.uc.UcDispatchSupport.*;

/**
 * Dieu phoi day du UC087 - UC151 (Thanh vien 2: Nguyen Duc Anh - TH08308).
 */
public final class Tv2UcDispatcher {
    private static final InventoryProcurementService invSvc = new InventoryProcurementService();
    private static final CartWishlistService cartSvc = new CartWishlistService();
    private static final OrderReturnService ordSvc = new OrderReturnService();

    private Tv2UcDispatcher() {}

    public static List<Map<String, Object>> execute(String uc, HttpServletRequest req) {
        String kw = str(req, "keyword", "");
        int uid = sessionUser(req);
        switch (uc) {
            case "UC087": return invSvc.listPriceHistories(numInt(req, "productId", 0), kw);
            case "UC088": return invSvc.listWarehouses(kw);
            case "UC089": return wrapAffected(uc, invSvc.createWarehouse(str(req, "warehouseCode", "KHO-TEST01"), str(req, "warehouseName", "Kho Chi Nhánh"), str(req, "address", "Hà Nội"), str(req, "phone", "0243888999")), "Đã tạo kho hàng mới", "Mã kho đã tồn tại");
            case "UC090": return wrapAffected(uc, invSvc.updateWarehouse(numInt(req, "warehouseId", 1), str(req, "address", "Kho Tổng DUNA Hà Nội"), bool(req, "active", true)), "Đã cập nhật thông tin kho", "Không tìm thấy kho");
            case "UC091": return invSvc.listInventories(numInt(req, "warehouseId", 0));
            case "UC092": return invSvc.searchInventoryBySku(kw);
            case "UC093": return invSvc.listLowStock();
            case "UC094": return wrapAffected(uc, invSvc.updateThreshold(numInt(req, "variantId", 1), numInt(req, "warehouseId", 1), numInt(req, "threshold", 5)), "Đã cập nhật ngưỡng cảnh báo", "Không tìm thấy dòng tồn kho");
            case "UC095": return wrapAffected(uc, invSvc.reserveStock(numInt(req, "variantId", 1), numInt(req, "warehouseId", 1), numInt(req, "qty", 1)), "Đã giữ chỗ tồn kho", "Tồn khả dụng không đủ");
            case "UC096": return wrapAffected(uc, invSvc.releaseStock(numInt(req, "variantId", 1), numInt(req, "warehouseId", 1), numInt(req, "qty", 1)), "Đã giải phóng giữ chỗ tồn kho", "Không có số lượng đang giữ");
            case "UC097": return invSvc.listInventoryLogs(kw);
            case "UC098": return invSvc.filterLogs(str(req, "changeType", ""), str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"));
            case "UC099": return wrapAffected(uc, invSvc.adjustStock(numInt(req, "variantId", 1), numInt(req, "warehouseId", 1), numInt(req, "newQty", 50), numInt(req, "delta", 0), str(req, "reason", "Kiểm kê định kỳ"), uid), "Đã điều chỉnh tồn kho & ghi log", "Không tìm thấy biến thể trong kho");
            case "UC100": return invSvc.listSuppliers(kw);
            case "UC101": return wrapAffected(uc, invSvc.createSupplier(str(req, "supplierCode", "NCC-HN05"), str(req, "supplierName", "Xưởng Dệt Kim Thăng Long"), str(req, "contactPerson", "Anh Hùng"), str(req, "phone", "0911223344"), "", "Hà Nội", str(req, "taxCode", "0109988776"), "Công nợ 30 ngày"), "Đã thêm nhà cung cấp", "Mã NCC đã tồn tại");
            case "UC102": return wrapAffected(uc, invSvc.updateSupplier(numInt(req, "supplierId", 1), str(req, "phone", "0912000111"), str(req, "paymentTerms", "Công nợ 30 ngày")), "Đã cập nhật nhà cung cấp", "Không tìm thấy NCC");
            case "UC103": return wrapAffected(uc, invSvc.deactivateSupplier(numInt(req, "supplierId", 999)), "Đã ngừng hợp tác NCC", "NCC đang có đơn PO mở hoặc không tồn tại");
            case "UC104": {
                String po = "PO-" + (System.currentTimeMillis() % 100000);
                return wrapAffected(uc, invSvc.createPurchaseOrder(po, numInt(req, "supplierId", 1), numInt(req, "warehouseId", 1), "2026-12-15", "Nhập bổ sung kho", uid, numInt(req, "variantId", 1), numInt(req, "qty", 20), numDouble(req, "costPrice", 190000)), "Đã tạo đơn đặt hàng NCC " + po, "Không tạo được PO");
            }
            case "UC105": return invSvc.listPurchaseOrders(str(req, "status", ""), kw);
            case "UC106": return wrapAffected(uc, invSvc.confirmPurchaseOrder(numInt(req, "poId", 1)), "Đã xác nhận đơn đặt hàng PO", "Đơn không ở trạng thái nháp");
            case "UC107": return wrapAffected(uc, invSvc.cancelPurchaseOrder(numInt(req, "poId", 999)), "Đã hủy đơn PO", "Không thể hủy đơn PO này");
            case "UC108": {
                String rc = "PN-" + (System.currentTimeMillis() % 100000);
                return wrapAffected(uc, invSvc.receiveGoodsAndRestock(rc, numInt(req, "poId", 1), numInt(req, "warehouseId", 1), uid, numInt(req, "variantId", 1), numInt(req, "receivedQty", 10), numDouble(req, "costPrice", 190000), "Nhập hàng theo PO"), "Đã lập phiếu nhập kho " + rc, "Không lập được phiếu nhập");
            }
            case "UC109": return wrapAffected(uc, new com.duna.tv2_nguyen_duc_anh.dao.ProcurementDAO().uc109_updatePurchaseOrderReceiptProgress(numInt(req, "poId", 1), numInt(req, "variantId", 1), numInt(req, "receivedQty", 5)), "Đã cập nhật tiến độ nhận hàng PO", "Không tìm thấy dòng PO");
            case "UC110": {
                String tc = "TF-" + (System.currentTimeMillis() % 100000);
                return wrapAffected(uc, invSvc.createTransfer(tc, numInt(req, "fromWh", 1), numInt(req, "toWh", 2), uid, numInt(req, "variantId", 1), numInt(req, "qty", 2)), "Đã tạo phiếu điều chuyển kho " + tc, "Không tạo được phiếu điều chuyển");
            }
            case "UC111": return wrapAffected(uc, invSvc.completeTransfer(numInt(req, "transferId", 1)), "Đã hoàn tất điều chuyển & cập nhật tồn hai kho", "Phiếu điều chuyển đã hoàn tất hoặc không tồn tại");
            case "UC112": {
                String sc = "KK-" + (System.currentTimeMillis() % 100000);
                return wrapAffected(uc, invSvc.createStocktake(sc, numInt(req, "warehouseId", 1), uid), "Đã mở phiếu kiểm kê " + sc, "Không mở được kiểm kê");
            }
            case "UC113": return wrapAffected(uc, invSvc.completeStocktake(numInt(req, "stocktakeId", 1), numInt(req, "warehouseId", 1)), "Đã chốt kiểm kê & cân bằng tồn", "Phiếu kiểm kê đã chốt hoặc không tồn tại");

            case "UC114": return cartSvc.getCartItems(numInt(req, "userId", 4));
            case "UC115": return wrapAffected(uc, cartSvc.addToCart(numInt(req, "cartId", 1), numInt(req, "variantId", 1), numInt(req, "qty", 1)), "Đã thêm vào giỏ hàng", "Không tìm thấy giỏ hàng/biến thể");
            case "UC116": return wrapAffected(uc, cartSvc.updateCartItem(numInt(req, "cartId", 1), numInt(req, "variantId", 1), numInt(req, "qty", 2)), "Đã cập nhật số lượng giỏ", "Không tìm thấy sản phẩm trong giỏ");
            case "UC117": return wrapAffected(uc, cartSvc.removeCartItem(numInt(req, "cartId", 1), numInt(req, "variantId", 1)), "Đã xóa sản phẩm khỏi giỏ", "Không tìm thấy dòng trong giỏ");
            case "UC118": return wrapAffected(uc, cartSvc.applyVoucher(numInt(req, "cartId", 1), str(req, "voucherCode", "DUNA100")), "Đã áp mã voucher vào giỏ", "Voucher không hợp lệ hoặc không tìm thấy giỏ");
            case "UC119": return wrapMap(cartSvc.getSummary(numInt(req, "cartId", 1)), uc, "Tạm tính giỏ hàng");
            case "UC120": return wrapAffected(uc, cartSvc.mergeGuestCart(numInt(req, "userCartId", 1), str(req, "sessionToken", "GUEST_TOKEN")), "Đã hợp nhất giỏ vãng lai", "Không có giỏ vãng lai cần hợp nhất");
            case "UC121": return wrapAffected(uc, cartSvc.markAbandonedCarts(), "Đã quét đánh dấu giỏ bỏ quên >24h", "Không có giỏ quá 24h");
            case "UC122": return cartSvc.listAbandonedCarts(numDouble(req, "minValue", 0));
            case "UC123": return cartSvc.listWishlist(numInt(req, "userId", 4), kw);
            case "UC124": return wrapAffected(uc, cartSvc.addToWishlist(numInt(req, "userId", 4), numInt(req, "productId", 1)), "Đã thêm vào yêu thích", "Sản phẩm đã có trong yêu thích");
            case "UC125": return wrapAffected(uc, cartSvc.removeFromWishlist(numInt(req, "userId", 4), numInt(req, "productId", 1)), "Đã bỏ khỏi yêu thích", "Sản phẩm chưa có trong yêu thích");

            case "UC126": return ordSvc.listOrders(kw, 0, 40);
            case "UC127": return ordSvc.searchOrders(kw);
            case "UC128": return ordSvc.filterOrders(str(req, "status", ""), str(req, "channel", ""), str(req, "fromDate", "2026-01-01"), str(req, "toDate", "2027-12-31"), kw);
            case "UC129": {
                List<Map<String, Object>> items = ordSvc.getOrderItems(numLong(req, "orderId", 1));
                return items.isEmpty() ? wrapMap(ordSvc.getOrderHeader(numLong(req, "orderId", 1)), uc, "Chi tiết đơn hàng") : items;
            }
            case "UC130": return ordSvc.listCustomerOrders(numInt(req, "customerId", 1), kw);
            case "UC131": return wrapScalar(uc, ordSvc.nextOrderCode(), "Mã đơn hàng tự động tiếp theo");
            case "UC132": return wrapAffected(uc, ordSvc.placeOrderFromCart(numInt(req, "customerId", 1), uid, str(req, "receiverName", "Trần Văn Nam"), str(req, "receiverPhone", "0912345678"), str(req, "shippingAddress", "Hà Nội"), 390000, 30000, numInt(req, "cartId", 1)), "Đã đặt đơn hàng online từ giỏ", "Không tạo được đơn (kiểm tra giỏ hàng)");
            case "UC133": return wrapAffected(uc, ordSvc.createPhoneOrder(numInt(req, "customerId", 1), uid, str(req, "receiverName", "Khách gọi hotline"), str(req, "receiverPhone", "0912345678"), str(req, "shippingAddress", "12 Nguyễn Trãi, Hà Nội"), numDouble(req, "subtotal", 390000), numDouble(req, "shipFee", 30000), "Tạo đơn qua điện thoại"), "Đã tạo đơn hàng qua điện thoại", "Không tạo được đơn");
            case "UC134": return wrapAffected(uc, ordSvc.confirmOrder(numLong(req, "orderId", 1), uid, "Xác nhận đơn"), "Đã xác nhận đơn hàng", "Đơn không ở trạng thái PENDING");
            case "UC135": return wrapAffected(uc, ordSvc.packOrder(numLong(req, "orderId", 1), uid), "Đã chuyển trạng thái đóng gói", "Đơn không ở trạng thái CONFIRMED");
            case "UC136": return wrapAffected(uc, ordSvc.shipOrder(numLong(req, "orderId", 1), uid), "Đã chuyển đang giao ĐVVC", "Đơn không ở trạng thái PACKING");
            case "UC137": return wrapAffected(uc, ordSvc.deliverOrder(numLong(req, "orderId", 1), uid), "Đã xác nhận giao thành công & thu COD", "Đơn không ở trạng thái SHIPPING");
            case "UC138": return wrapAffected(uc, ordSvc.completeOrder(numLong(req, "orderId", 1)), "Đã hoàn tất đơn & cộng chi tiêu khách", "Đơn không ở trạng thái DELIVERED");
            case "UC139": return wrapAffected(uc, ordSvc.cancelOrder(numLong(req, "orderId", 999), str(req, "reasonCode", "CUSTOMER_REQUEST"), str(req, "reasonDetail", "Khách đổi ý"), uid), "Đã hủy đơn & trả giữ chỗ kho", "Đơn không ở trạng thái cho phép hủy");
            case "UC140": return wrapAffected(uc, ordSvc.addNote(numLong(req, "orderId", 1), uid, str(req, "content", "Giao giờ hành chính"), bool(req, "internal", true)), "Đã thêm ghi chú đơn hàng", "Không thêm được ghi chú");
            case "UC141": return ordSvc.listNotes(numLong(req, "orderId", 1), kw);
            case "UC142": return wrapAffected(uc, ordSvc.updateShippingAddress(numLong(req, "orderId", 1), str(req, "receiverName", "Trần Văn Nam"), str(req, "phone", "0912345678"), str(req, "address", "Số 88 Cầu Giấy")), "Đã cập nhật địa chỉ giao", "Chỉ sửa được đơn PENDING/CONFIRMED");
            case "UC143": return ordSvc.listStatusHistory(numLong(req, "orderId", 1), kw);
            case "UC144": return wrapAffected(uc, ordSvc.issueInvoice("INV-" + (System.currentTimeMillis() % 100000), numLong(req, "orderId", 1), str(req, "buyerName", "Trần Văn Nam"), str(req, "companyName", "DUNA Client"), str(req, "taxCode", "0101234567"), "Hà Nội"), "Đã xuất hóa đơn VAT", "Đơn đã xuất hóa đơn hoặc chưa hoàn tất");
            case "UC145": return ordSvc.findInvoices(kw);
            case "UC146": return wrapAffected(uc, ordSvc.cancelInvoice(numInt(req, "invoiceId", 999)), "Đã hủy hóa đơn", "Không tìm thấy hóa đơn");
            case "UC147": {
                String rc = "RET-" + (System.currentTimeMillis() % 100000);
                return wrapAffected(uc, ordSvc.createReturnRequest(rc, numLong(req, "orderId", 1), numInt(req, "customerId", 1), str(req, "requestType", "EXCHANGE"), str(req, "reason", "Đổi size"), 1L, 1, null, "Khách yêu cầu"), "Đã tạo yêu cầu đổi trả " + rc, "Không tạo được yêu cầu");
            }
            case "UC148": return ordSvc.listReturns(str(req, "status", ""), kw);
            case "UC149": return wrapAffected(uc, ordSvc.approveReturn(numInt(req, "returnId", 1), uid), "Đã duyệt yêu cầu đổi trả", "Không ở trạng thái PENDING");
            case "UC150": return wrapAffected(uc, ordSvc.rejectReturn(numInt(req, "returnId", 999), uid, str(req, "reasonNote", "Quá hạn đổi trả")), "Đã từ chối yêu cầu đổi trả", "Không tìm thấy yêu cầu chờ duyệt");
            case "UC151": return wrapAffected(uc, ordSvc.receiveReturn(numInt(req, "returnId", 1), numInt(req, "warehouseId", 1), numInt(req, "variantId", 1), numInt(req, "qty", 1)), "Đã nhận hàng trả & nhập lại kho", "Không xử lý được yêu cầu");
            default: return wrapScalar(uc, "N/A", "Chưa hỗ trợ");
        }
    }
}
