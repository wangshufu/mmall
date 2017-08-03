package com.mmall.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.CategoryMapper;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Category;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import com.mmall.util.DateTimeUtil;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import com.mmall.vo.ProductListVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Created by wangshufu on 2017/8/2.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    /**
     * 如果id为null则表示更新商品,如果id不为null,则表示添加商品
     *
     * @param product
     * @return
     */
    public ServerResponse saveOrUpdateProduct(Product product) {
        if (product != null) {
            if (StringUtils.isNotBlank(product.getSubImages())) {
                String[] imageArray = product.getSubImages().split(",");
                if (imageArray.length > 0) {
                    product.setMainImage(imageArray[0]);
                }
            }
            if (product.getId() == null) {
                //添加商品
                int insert = productMapper.insert(product);
                if (insert > 0) {
                    return ServerResponse.createBySuccessMessage("添加成功");
                } else {
                    return ServerResponse.createByError("添加失败");
                }
            } else {
                //更新商品
                int updateReslut = productMapper.updateByPrimaryKeySelective(product);
                if (updateReslut > 0) {
                    return ServerResponse.createBySuccessMessage("更新成功");
                } else {
                    return ServerResponse.createByError("更新失败");
                }
            }
        } else {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        }
    }

    /**
     * 修改商品的销售状态
     * @param productId
     * @param state
     * @return
     */
    public ServerResponse setSaleStatus(Integer productId, Integer state) {
        if (productId == null || state == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        } else {
            Product product = new Product();
            product.setId(productId);
            product.setStatus(state);
            int updateResult = productMapper.updateByPrimaryKeySelective(product);
            if (updateResult > 0){
                return ServerResponse.createBySuccessMessage("修改产品销售状态成功");
            }else {
                return ServerResponse.createByError("修改产品销售状态失败");
            }
        }
    }

    /**
     * 通过商品Id得到商品详情,数据已组装为vo返回
     * @param productId
     * @return
     */
    public ServerResponse<ProductDetailVo> manageProductDetail(Integer productId){
        if (productId == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(), ResponseCode.ILLEGAL_ARGUMENT.getDecs());
        } else {
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product == null){
                return ServerResponse.createByError("商品已删除或下架");
            }else {
                return ServerResponse.createBySuccess(assembleProductDetailVo(product));
            }
        }
    }

    /**
     * 获取商品集合
     * @param pageNum 第几页
     * @param pageSize 每页多少个
     * @return
     */
    public ServerResponse<PageInfo> manageProductList(Integer pageNum,Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectList();
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product productItem : products){
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }

    /**
     * 根据商品名或者id来搜索商品,返回商品集合
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public ServerResponse<PageInfo> productSearch(String productName,int productId,int pageNum,int pageSize){
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.selectByNameAndProductId(productName,productId);
        List<ProductListVo> productListVos = Lists.newArrayList();
        for (Product productItem : products){
            productListVos.add(assembleProductListVo(productItem));
        }
        PageInfo pageInfo = new PageInfo(products);
        pageInfo.setList(productListVos);
        return ServerResponse.createBySuccess(pageInfo);
    }
    /**
     * 将Product组合成ProductDetailVo
     * @param product
     * @return
     */
    public ProductDetailVo assembleProductDetailVo(Product product){
        ProductDetailVo productDetailVo = new ProductDetailVo();
        productDetailVo.setId(product.getId());
        productDetailVo.setCategoryId(product.getCategoryId());
        productDetailVo.setName(product.getName());
        productDetailVo.setSubtitle(product.getSubtitle());
        productDetailVo.setDetail(product.getDetail());
        productDetailVo.setMainImage(product.getMainImage());
        productDetailVo.setSubImages(product.getSubImages());
        productDetailVo.setPrice(product.getPrice());
        //查询商品的分类
        Category category = categoryMapper.selectByPrimaryKey(product.getCategoryId());
        if (category == null || category.getParentId() == null) {
            productDetailVo.setParentCategoryId(0);
        } else {
            productDetailVo.setParentCategoryId(category.getParentId());
        }
        //设置图片host
        productDetailVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));

        //设置时间,因为我们用mybatis的db来查询数据库的时间值为毫秒值,不利于观看,所以需格式化
        productDetailVo.setCreateTime(DateTimeUtil.dateToStr(product.getCreateTime()));
        productDetailVo.setUpdateTime(DateTimeUtil.dateToStr(product.getUpdateTime()));
        return productDetailVo;
    }

    /**
     * 将Product组合成ProductListVo
     * @param product
     * @return
     */
    public ProductListVo assembleProductListVo(Product product){
        ProductListVo productListVo = new ProductListVo();
        productListVo.setId(product.getId());
        productListVo.setName(product.getName());
        productListVo.setPrice(product.getPrice());
        productListVo.setMainImage(product.getMainImage());
        productListVo.setCategoryId(product.getCategoryId());
        productListVo.setSubtitle(product.getSubtitle());
        productListVo.setStatus(product.getStatus());
        productListVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/"));
        return productListVo;
    }

}
