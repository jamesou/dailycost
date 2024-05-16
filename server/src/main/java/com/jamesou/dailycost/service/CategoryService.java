package com.keepfool.bill.service;

import com.keepfool.bill.bean.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, List<Category>> selectCategoryByState();
}
