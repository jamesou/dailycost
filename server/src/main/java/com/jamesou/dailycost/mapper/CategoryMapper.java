package com.jamesou.dailycost.mapper;

import com.jamesou.dailycost.bean.Category;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CategoryMapper {
    List<Category> selectCategoryByState(int categoryState);
}
