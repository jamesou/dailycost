package com.jamesou.dailycost.service;

import com.jamesou.dailycost.bean.Bill;

import java.util.List;
import java.util.Map;

public interface BillService {
    int bookkeeping(Bill bill);
    int modifyBills(Bill bill);
    List<Map<String, Object>> getBillDetails(int userId, String billTime);
    Map<String, Float> getCategoryStateAmount(int userId, String billTime);
    Bill getBillDetailByBillId(int billId);
    int deleteBillDetail(int billId);
    Map<String, Object> getBillTime(int userId);
    Map<String, Object> getBalance(int userId, String year);
}
