package com.jamesou.dailycost.controller;

import com.jamesou.dailycost.bean.Bill;
import com.jamesou.dailycost.service.BillService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @ResponseBody
    @PostMapping("/bill")
    public int bookkeeping(@RequestBody Bill bill) {
        return billService.bookkeeping(bill);
    }

    @ResponseBody
    @PostMapping("/modifyBill")
    public int modifyBills(@RequestBody Bill bill) {
        return billService.modifyBills(bill);
    }

    @ResponseBody
    @GetMapping("/bill/{userId}/{billTime}")
    public Map<String, Object> getBillDetails(@PathVariable("userId") int userId, @PathVariable("billTime") Date billTime) {
        Map<String, Object> map = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        map.put("amount", billService.getCategoryStateAmount(userId, dateFormat.format(billTime)));
        map.put("list", billService.getBillDetails(userId, dateFormat.format(billTime)));
        return map;
    }

    @ResponseBody
    @GetMapping("/bill/{billId}")
    public Map<String, Object> getBillDetailByBillId(@PathVariable("billId") int billId) {
        Bill bill = billService.getBillDetailByBillId(billId);
        Map<String, Object> map = new HashMap<>();
        map.put("billId", bill.getBillId());
        map.put("categoryId", bill.getCategoryId());
        map.put("categoryIcon", bill.getCategory().getCategoryIcon());
        map.put("categoryName", bill.getCategory().getCategoryName());
        map.put("categoryState", bill.getCategory().getCategoryState());
        map.put("billAmount", bill.getBillAmount());
        map.put("billTime", bill.getBillTime());
        map.put("billRemark", bill.getBillRemark());
        return map;
    }

    @ResponseBody
    @DeleteMapping("/bill/{billId}")
    public int deleteBillDetail(@PathVariable("billId") int billId) {
        return billService.deleteBillDetail(billId);
    }

    @ResponseBody
    @GetMapping("/getBillTime/{userId}")
    public Map<String, Object> getBillTime(@PathVariable("userId") int userId) {
        return billService.getBillTime(userId);
    }

    @ResponseBody
    @GetMapping("/getBalance")
    public Map<String, Object> getBalance(@RequestParam("userId") int userId, @RequestParam("year") String year) {
        return billService.getBalance(userId, year);
    }
}
