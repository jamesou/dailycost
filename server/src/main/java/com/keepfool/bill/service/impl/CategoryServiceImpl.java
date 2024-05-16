package com.keepfool.bill.service.impl;

import com.keepfool.bill.bean.Category;
import com.keepfool.bill.mapper.CategoryMapper;
import com.keepfool.bill.service.CategoryService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    final
    CategoryMapper categoryMapper;

    public CategoryServiceImpl(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }

    @Override
    public Map<String, List<Category>> selectCategoryByState() {
        List<Category> expend = categoryMapper.selectCategoryByState(0);
        List<Category> income = categoryMapper.selectCategoryByState(1);
        Map<String, List<Category>> map = new HashMap<>();
        map.put("expend", expend);
        map.put("income", income);
        return map;
    }
}
