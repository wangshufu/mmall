package com.mmall.controller.backend;

import com.google.common.collect.Maps;
import com.mmall.common.Const;
import com.mmall.common.ResponseCode;
import com.mmall.common.ServerResponse;
import com.mmall.pojo.Product;
import com.mmall.pojo.User;
import com.mmall.service.IFileService;
import com.mmall.service.IProductService;
import com.mmall.service.IUserService;
import com.mmall.util.PropertiesUtil;
import com.mmall.vo.ProductDetailVo;
import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private IFileService iFileService;

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

    /**
     * 获取商品详情
     * @param session
     * @param productId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getDetail.do",method = RequestMethod.POST)
    public ServerResponse<ProductDetailVo> getDetail(HttpSession session,Integer productId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加或更新商品信息
            return iProductService.manageProductDetail(productId);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    /**
     * 获取商品集合
     * @param session
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "getList.do",method = RequestMethod.POST)
    public ServerResponse getList(HttpSession session,
                                  @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加或更新商品信息
            return iProductService.manageProductList(pageNum,pageSize);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    /**
     * 根据商品名或者id来搜索商品,返回商品集合
     * @param session
     * @param productName
     * @param productId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "productSearch.do",method = RequestMethod.POST)
    public ServerResponse productSearch(HttpSession session,String productName,int productId,
                                  @RequestParam(value = "pageNum",defaultValue = "1") int pageNum,
                                  @RequestParam(value = "pageSize",defaultValue = "10")int pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //添加或更新商品信息
            return iProductService.productSearch(productName, productId, pageNum, pageSize);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    @ResponseBody
    @RequestMapping(value = "upload.do",method = RequestMethod.POST)
    public ServerResponse upload(HttpSession session,@RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request){
        // TODO: 2017/8/4 这个required = false是指upload_file不是必须加的吗?
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"请先登录");
        }
        //判断这个用户是不是管理员
        ServerResponse<String> checkRoleResult = iUserService.checkUserRole(user);
        if (checkRoleResult.isSuccess()){
            //tomcat发布以后,会在webapp下创建一个upload文件夹,与index.jsp和WEB-INF同级
            String path = request.getSession().getServletContext().getRealPath("upload");
            String fileName = iFileService.upload(file, path);
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix","http://img.happymmall.com/")
                    +fileName;
            Map map = Maps.newHashMap();
            map.put("uri",fileName);
            map.put("url",url);
            return ServerResponse.createBySuccess(map);
        } else {
            return ServerResponse.createByError("对不起,您没有管理员权限");
        }
    }

    @RequestMapping("richtext_img_upload.do")
    @ResponseBody
    public Map richtextImgUpload(HttpSession session, @RequestParam(value = "upload_file",required = false) MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map resultMap = Maps.newHashMap();
        User user = (User)session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            resultMap.put("success",false);
            resultMap.put("msg","请登录管理员");
            return resultMap;
        }
        //富文本中对于返回值有自己的要求,我们使用是simditor所以按照simditor的要求进行返回
//        {
//            "success": true/false,
//                "msg": "error message", # optional
//            "file_path": "[real file path]"
//        }
        if(iUserService.checkUserRole(user).isSuccess()){
            String path = request.getSession().getServletContext().getRealPath("upload");
            String targetFileName = iFileService.upload(file,path);
            if(StringUtils.isBlank(targetFileName)){
                resultMap.put("success",false);
                resultMap.put("msg","上传失败");
                return resultMap;
            }
            String url = PropertiesUtil.getProperty("ftp.server.http.prefix")+targetFileName;
            resultMap.put("success",true);
            resultMap.put("msg","上传成功");
            resultMap.put("file_path",url);
            response.addHeader("Access-Control-Allow-Headers","X-File-Name");
            return resultMap;
        }else{
            resultMap.put("success",false);
            resultMap.put("msg","无权限操作");
            return resultMap;
        }
    }

}
