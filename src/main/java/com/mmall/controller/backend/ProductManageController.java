package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by wangshufu on 2017/8/2.
 */
@Controller
@RequestMapping("/manage/product")
public class ProductManageController {

    @Autowired
    private IUserService iUserService;
    @Autowired
    private IProductService iProductService;

    /**
     * 添加或者更新商品接口
     * @param session
     * @param product 如果id为null则表示更新商品,如果id不为null,则表示添加商品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "saveOrUpdateProduct.do",method = RequestMethod.POST)
    public ServerResponse<String> saveOrUpdateProduct(HttpSession session, Product product){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加或更新商品信息
            return iProductService.saveOrUpdateProduct(product);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    /**
     * 修改商品的销售状态
     * @param session
     * @param productId
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "setSaleStatus.do",method = RequestMethod.POST)
    public ServerResponse setSaleStatus(HttpSession session,Integer productId,Integer state){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加或更新商品信息
            return iProductService.setSaleStatus(productId, state);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }
}
