package com.keepfool.bill.controller;

import com.keepfool.bill.bean.Category;
import com.keepfool.bill.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {
    final
    CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @ResponseBody
    @GetMapping("/category")
    public Map<String, List<Category>> selectCategoryByState() {
        return categoryService.selectCategoryByState();
    }
}
