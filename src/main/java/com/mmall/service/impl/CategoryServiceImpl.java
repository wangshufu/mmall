package com.mmall.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.pojo.Category;
import com.mmall.service.ICategoryService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * Created by wangshufu on 2017/8/1.
 */
@Service("iCategoryService")
public class CategoryServiceImpl implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<String> addCategory(String categoryName, Integer parentId) {
        if (StringUtils.isNotBlank(categoryName) && parentId != null) {
            Category category = new Category();
            category.setParentId(parentId);
            category.setName(categoryName);
            category.setStatus(true);
            int insertResult = categoryMapper.insert(category);
            if (insertResult > 0) {
                return ServerResponse.createBySuccessMessage("添加成功");
            } else {
                return ServerResponse.createByError("添加失败");
            }
        }
        return ServerResponse.createByError("您的参数有问题");
    }

    @Override
    public ServerResponse<String> updateCategoryInfo(String categoryName, Integer categoryId) {
        if (StringUtils.isNotBlank(categoryName) && categoryId != null) {
            Category category = new Category();
            category.setId(categoryId);
            category.setName(categoryName);
            int updateResult = categoryMapper.updateByPrimaryKeySelective(category);
            if (updateResult > 0) {
                return ServerResponse.createBySuccessMessage("更新类品成功");
            } else {
                return ServerResponse.createByError("更新类品失败");
            }
        }
        return ServerResponse.createByError("您的参数有问题");
    }

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        if (parentId != null) {
            List<Category> categories = categoryMapper.selectByParentId(parentId);
            if (CollectionUtils.isEmpty(categories)) {
                logger.info("未找到当前分类的子分类");
            } else {
                return ServerResponse.createBySuccess(categories);
            }
        }
        return ServerResponse.createByError("您的参数有问题");
    }

    /**
     * 递归查询本节点的id及孩子节点的id
     * @param categoryId
     * @return
     */
    public ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId){
        Set<Category> categorySet = Sets.newHashSet();
        findChildCategory(categorySet,categoryId);


        List<Integer> categoryIdList = Lists.newArrayList();
        if(categoryId != null){
            for(Category categoryItem : categorySet){
                categoryIdList.add(categoryItem.getId());
            }
        }
        return ServerResponse.createBySuccess(categoryIdList);
    }


    //递归算法,算出子节点
    private Set<Category> findChildCategory(Set<Category> categorySet ,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category != null){
            categorySet.add(category);
        }
        //查找子节点,递归算法一定要有一个退出的条件
        List<Category> categoryList = categoryMapper.selectByParentId(categoryId);
        for(Category categoryItem : categoryList){
            findChildCategory(categorySet,categoryItem.getId());
        }
        return categorySet;
    }
}
