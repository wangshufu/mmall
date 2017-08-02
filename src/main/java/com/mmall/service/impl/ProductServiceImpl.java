package com.mmall.service.impl;

import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.dao.ProductMapper;
import com.mmall.pojo.Product;
import com.mmall.service.IProductService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by wangshufu on 2017/8/2.
 */
@Service("iProductService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

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
}
