package com.jamesou.dailycost.service;

import com.jamesou.dailycost.bean.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    Map<String, List<Category>> selectCategoryByState();
}
