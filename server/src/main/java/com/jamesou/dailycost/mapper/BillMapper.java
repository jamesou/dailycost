package com.jamesou.dailycost.mapper;

import com.jamesou.dailycost.bean.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface BillMapper {
    int bookkeeping(Bill bill);
    int modifyBills(Bill bill);
    List<Bill> getBillDetails(int userId, String billTime);
    List<Map<String, Object>> getBillCategoryAmount(int userId, String billTime);
    List<Bill> getCategoryStateAmount(int userId, String billTime);
    Bill getBillDetailByBillId(int billId);
    int deleteBillDetail(int billId);
    String getFirstBillTime(int userId);
    List<String> getAllBillTime(int userId);
    List<Map<String, Object>> getBalance(int userId, String year);
}
