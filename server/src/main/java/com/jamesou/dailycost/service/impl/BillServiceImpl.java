package com.jamesou.dailycost.service.impl;

import com.jamesou.dailycost.bean.Bill;
import com.jamesou.dailycost.mapper.BillMapper;
import com.jamesou.dailycost.service.BillService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BillServiceImpl implements BillService {
    final BillMapper billMapper;

    public BillServiceImpl(BillMapper billMapper) {
        this.billMapper = billMapper;
    }

    @Override
    public int bookkeeping(Bill bill) {
        billMapper.bookkeeping(bill);
        return bill.getBillId();
    }

    @Override
    public int modifyBills(Bill bill) {
        billMapper.modifyBills(bill);
        return bill.getBillId();
    }

    @Override
    public List<Map<String, Object>> getBillDetails(int userId, String billTime) {
        List<Map<String, Object>> billCategoryAmount = billMapper.getBillCategoryAmount(userId, billTime);
        List<Bill> billDetails = billMapper.getBillDetails(userId, billTime);
        List<Map<String, Object>> lists = new ArrayList<>();

        billCategoryAmount.forEach(item -> {
            Map<String, Object> map = new HashMap<>();
            map.put("date", item.get("billTime"));
            map.put("expend", item.get("expend"));
            map.put("income", item.get("income"));

            List<Map<String, Object>> list = new ArrayList<>();
            billDetails.forEach(detail -> {
                System.out.println(detail);
                if (detail.getBillTime().equals(item.get("billTime"))) {
                    Map<String, Object> detailsMap = new HashMap<>();
                    detailsMap.put("billId", detail.getBillId());
                    detailsMap.put("icon", detail.getCategory().getCategoryIcon());
                    if(detail.getBillRemark().equals("")){
                        detailsMap.put("remark", detail.getCategory().getCategoryName());
                    } else {
                        detailsMap.put("remark", detail.getBillRemark());
                    }
                    detailsMap.put("money", detail.getBillAmount());
                    list.add(detailsMap);
                }
            });
            map.put("details", list);
            lists.add(map);
        });
        return lists;
    }

    @Override
    public Map<String, Float> getCategoryStateAmount(int userId, String billTime) {
        List<Bill> categoryStateAmount = billMapper.getCategoryStateAmount(userId, billTime);
        Map<String, Float> map = new HashMap<>();
        categoryStateAmount.forEach(item -> {
            String state = item.getCategory().getCategoryState() == 0 ? "expend" : "income";
            map.put(state, item.getBillAmount());
        });
        return map;
    }

    @Override
    public Bill getBillDetailByBillId(int billId) {
        return billMapper.getBillDetailByBillId(billId);
    }

    @Override
    public int deleteBillDetail(int billId) {
        return billMapper.deleteBillDetail(billId);
    }

    @Override
    public Map<String, Object> getBillTime(int userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("first", billMapper.getFirstBillTime(userId));
        map.put("years", billMapper.getAllBillTime(userId));
        return map;
    }

    @Override
    public Map<String, Object> getBalance(int userId, String year) {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> list = billMapper.getBalance(userId, year);
        float balanceTotal = 0, expend = 0, income = 0;
        for (Map m : list) {
            balanceTotal += (double) m.get("balance");
            expend += (double) m.get("expend");
            income += (double) m.get("income");
        }
        map.put("balanceTotal", balanceTotal);
        map.put("expend", expend);
        map.put("income", income);
        map.put("balance", list);
        return map;
    }
}
