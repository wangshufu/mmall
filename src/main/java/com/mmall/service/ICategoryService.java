package com.mmall.service;

import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;

import java.util.List;

/**
 * Created by wangshufu on 2017/8/1.
 */
public interface ICategoryService {

    ServerResponse<String> addCategory(String categoryName,Integer parentId);

    ServerResponse<String> updateCategoryInfo(String categoryName, Integer categoryId);

    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);

    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
