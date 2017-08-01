package com.mmall.controller.backend;

import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Category;
import com.mmall.pojo.User;
import com.mmall.service.ICategoryService;
import com.mmall.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by wangshufu on 2017/8/1.
 */
@Controller
@RequestMapping("/manager/category")
public class CategoryManagerController {

    @Autowired
    private IUserService iUserService;

    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 添加类品接口
     * @param session
     * @param categoryName
     * @param parentId
     * @return
     */
    @RequestMapping("addCategory.do")
    @ResponseBody
    public ServerResponse addCategory(HttpSession session,String categoryName,
                                              @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加类品
            return iCategoryService.addCategory(categoryName, parentId);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    /**
     * 更新类品接口
     * @param session
     * @param categoryName
     * @param categoryId
     * @return
     */
    @RequestMapping("updateCategoryInfo.do")
    @ResponseBody
    public ServerResponse updateCategoryInfo(HttpSession session,String categoryName,
                                                     @RequestParam(value = "categoryId",defaultValue = "0") Integer categoryId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加类品
            return iCategoryService.updateCategoryInfo(categoryName, categoryId);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    /**
     * 获取平级的parentId的类品
     * @param session
     * @param parentId
     * @return
     */
    @RequestMapping("getChildrenParallelCategory.do")
    @ResponseBody
    public ServerResponse<List<Category>> getChildrenParallelCategory(HttpSession session,
                                                            @RequestParam(value = "parentId",defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加类品
            return iCategoryService.getChildrenParallelCategory(parentId);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    @RequestMapping("get_deep_category.do")
    @ResponseBody
    public ServerResponse getCategoryAndDeepChildrenCategory(HttpSession session,@RequestParam(value = "categoryId" ,defaultValue = "0") Integer categoryId){
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录,请登录");
        }
        if(iUserService.checkUserRole(user).isSuccess()){
            //查询当前节点的id和递归子节点的id
//            0->10000->100000
            return iCategoryService.selectCategoryAndChildrenById(categoryId);

        }else{
            return ServerResponse.createByError("无权限操作,需要管理员权限");
        }
    }
}
